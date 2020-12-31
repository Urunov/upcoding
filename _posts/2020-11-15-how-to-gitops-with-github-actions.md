---
title: How to GitOps with Github Actions
categories:
 - github, gitops, devops, argocd
tags:
 - aws ecr, github, github actions, devops, gitops, kubernetes, docker, image, deployment, yaml, argocd
---

The original definition of GitOps was taken from [gitops.tech](https://www.gitops.tech/) and it says that GitOps is a way of implementing Continuous Deployment for cloud native applications. It usually focused on developers who can easily setup CI/CD environments by using familiar tools such as Git and CD tools. There are common patterns for CI/CD setup, but each time when we setup new environment with CI/CD some customization is required. It is like, every our new project somehow similar with our previous project but not the same. We can say same thing about CI/CD process. In this article I would like to describe a GitOps setup which is used to deploy application to the kubernetes. 

## What We Have?

Lets assume the following components as given (already settled):
- Kubernetes cluster and all related infrastructures
- AWS ECR (container repository)
- ArgoCD is installed in the Kubernetes cluster to manage the deployments and services
- Microservice Application (let's name it "Product-Service"). Contains exclusively application and business logics
- GitOps repo (let's name it gitops-repo). Contains kubernetes deployment and service YAML files.
- ArgoCD application for Product-Service (let's name it gitops-product-service) which is staring at gitops-repo repository for changes and when some changes happens, does the syncing (Auto-sync can be enabled)

## What do We Want?

Since the control of kubernetes happens in a declarative way, deployment of new version of our application can happen through updating deployment yaml configuration file. Lets formalize what we want in a requirement style:

- We want to do deployment when our application code is commited into special branch
- We want to handle deployments and operations in a separate repository
- We want to deploy image with the same tag (for example, sandbox tag is attached to the image which is deployed to sandbox environment. It relieves us from version hell)

![img](/assets/2020/github-actions/gitOps-flow.jpg)

### Challenges caused by Requirements

First two requirements causing a challenge, since we have to bind to repositories in our CI/CD tool. If deployment operations code and application codes are located under the same repository it would be very easy. 

Third issue is another challenge because we don't want to just change the tag name of the `containers.image` every time when we do the sandbox deployment. That means we have to do some other change to the deployment YAML file. Adding a changelog as a comment at the bottom of the file didn't help. I have tried and it didn't trigger the un-sync at the ArgoCD.

## Communication between Repos

Github has [repository_dispatch](https://developer.github.com/v3/repos/#create-a-repository-dispatch-event) event, that is triggered from outside by sending an HTTP POST request to `https://api.github.com/repos/:owner/:repo/dispatches`. 

This endpoint expects 2 input parameters:
- event_type: This will be received in the workflow event payload as the action field.
- client_payload: JSON object with any custom information that you want to propagate to the workflow, this will be available in the client_payload field of the event payload.

Here is how the endpoint can be invoked by using cURL:
```bash
curl -X POST https://api.github.com/repos/:owner/:repo/dispatches \
-H 'Accept: application/vnd.github.everest-preview+json' \
-u $your_user:$your_personal_access_token \
--data '{"event_type": "$your_event", "client_payload": { "customField": "customValue" }}'
```

When the push happens to sandbox or master branch we send the repository_dispatch message with commit message and branch name, as follows: 

```bash
- name: Trigger new Operation-Resource actions for Deployment
  run: |
    commitMsg=$(git log --format=%B -n 1 ${{ github.event.after }} --pretty=oneline --abbrev-commit)
    branchName="${GITHUB_REF##*/}"
    echo "commitMsg: $commitMsg"
    echo "branchName: $branchName"
    curl -X POST https://api.github.com/repos/kakao-webtoon/operation-resource/dispatches \
    -H 'Accept: application/vnd.github.everest-preview+json' \
    -u ${{ secrets.OPERATION_RESOURCE_REPOSITORY_ACCESS_TOKEN }} \
    --data '{"event_type": "product", "client_payload": { "commitmsg": "'"$commitMsg"'", "baseRef":"'"$branchName"'" }}'
```

Here `secrets.OPERATION_RESOURCE_REPOSITORY_ACCESS_TOKEN` contains `userName:ACCESS_TOKEN`, where userName is the user's name who has access to the target repository, and ACCESS_TOKEN contains personal access token which is generated at github [web console](https://docs.github.com/en/free-pro-team@latest/github/authenticating-to-github/creating-a-personal-access-token).

The following git command is used to get the last commit message in a oneline abbreviated format:
```bash
git log --format=%B -n 1 ${{ github.event.after }} --pretty=oneline --abbrev-commit
```

## Change Container Environment

At the gitops-repo (receiving side) we setup the workflow which is triggered by `repository_dispatch` message. The payload sent from Application repository can be found under the `github.event.client_payload` variable. Event type can be read as `github.event.action`

```bash
- name: Event Information
  run: |
    dirName=${{ github.event.action }}
    receivedMessage="${{ github.event.client_payload.commitmsg }}"
    baseRef=${{ github.event.client_payload.baseref }}
    echo "receivedMessage=> $receivedMessage , path=> $dirName/$baseRef/change.log , baseRef: $baseRef"

    if [ $baseRef != 'sandbox' ] && [ $baseRef != 'qa' ] && [ $baseRef != 'master' ]
    then
        echo ">>>>>>>>>>>>>>>>>>>> This Base branch is $baseRef, does not follow policy! <<<<<<<<<<<<<<<"
        exit 1
    fi

## these are the examplar values
# dirName=product
# receivedMessage=commited message
# baseRef=sandbox
```

The above script is storing global github variables into local shell-script environment variables and prints them out. Additionally the script checks the branch, if the branch is not expected one (sandbox, qa, master) then error is triggered.

Next we have to checkout the current branch codes into local repo, it is done through an existing github action:

```yaml
- name: Checkout repo
  uses: actions/checkout@v2
  with:
    ref: ${{ github.event.client_payload.baseref }}
```

Important to remember, the steps do not share the local variables thats why we have to access through `github.event.client_payload.baseref`.

As I have mentioned in one of the above sections, deployment YAML file must be modified in order to initiate ArgoCD deployment process. Modification should be performed to the container part of the deployment configuration file. There are many ways of doing that, I have decided to store commit messages as a container environment. Just storing it as an environment variables value could be easy solution (then we would need YAML file editor kind of solution). Here we have decided to use SED command, and to do it more exact way, the html style of tagging has been used as follows:
```
<lastCommitMsg>Last git commit is shown in here</lastCommitMsg>
```

This is how it looks like under the containers section of the deployment yaml (product/sandbox/sandbox-deployment.yaml):
```yaml
containers:
    - env:
        - name: SPRING_PROFILES_ACTIVE
            value: sandbox
        - name: LAST_COMMIT_MSG
            value: "<lastCommitMsg>Last git commit is shown in here</lastCommitMsg>"
        image: XXXXXXXXXXXX.dkr.ecr.ap-northeast-2.amazonaws.com/product-service:sandbox
        imagePullPolicy: Always
        name: product-service
        ports:
            - containerPort: 8080
        resources:
            limits:
                cpu: 1024m
                memory: 1024Mi
            requests:
                cpu: 1024m
                memory: 1024Mi
```

Github action code snippet that changes last commit message value, and that way initiates synchronization at ArgoCD.

```yaml
- name: Replace commit message environment
  run: |
    dirName=${{ github.event.action }}
    receivedMessage="${{ github.event.client_payload.commitmsg }}"
    baseRef=${{ github.event.client_payload.baseref }}
    commitTime=`date`
    commitMsg="$commitTime,  Kakaowebtoon GitOps: $receivedMessage"
    sed -i -E "s|(<lastCommitMsg>).*(<\/lastCommitMsg>)|\1$commitMsg\2|" $dirName/$baseRef/$baseRef-deploy.yaml
```

The above SED command looks for tags `<lastCommitMsg>..</lastCommitMsg>` and puts the commit message into those tags.

Now we have to commit the changes, and `EndBug/add-and-commit@v5` github action is used for that purpose:

```yaml
- name: Commit changes
  uses: EndBug/add-and-commit@v5
  with:
    author_name: "repository_${{ github.event.action }}"
    author_email: gitops@kakaopage.com
    branch: ${{ github.event.client_payload.baseref }}
    message: "App commit msg: ${{ github.event.client_payload.commitmsg }}"
    add: "*"
  env:
    GITHUB_TOKEN: ${{ secrets.GITHUB_TOKEN }}
```

