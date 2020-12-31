---
title: Git Commands
categories:
 - git
tags:
 - git, hacks, repository
---

Here I try to put up some of the usefull git commands. Refer to it whenever quick check is needed.

# Copy Repository

If we need to copy the repository to a new repository then it can be simply pushed while staying at the source repository. For example we have an old-repo and we want to copy its contents to new-repo.

1. Create a new-repo (in github for example)
2. cd to your local copy of the old-repo you want to extract from, which is set up to track the new-project branch that will become the new-repo's master
3. ```$ git push https://github.com/accountname/new-repo.git +new-project:master```

if you have to enter ssh keys then the step-3 should be done in a following way:
```$ git push git@github.com:accountname/new-repo +new-project:master```

If you don't want to make a complications regarding to branches and projects then simply ```$ git push https://github.com/accountname/new-repo.git master``` would work too.


# Push Into Branch

First of all we have to find out what branch is the active one right now, to do that ```git branch``` command can be used. The result will be something like: 
  master
* branch1
  branch2
  branch3
  branch4

The `*` sing stands for current branch. 

- To create a branch ```git branch <branchName>```
- To delete a branch ```git branch -d <branchName>```
- To force delete a branch ```git branch -D <branchName>```
- To rename current branch to newBranchName ```git branch -m <newBranchName>```
- List all remote branches ```git branch -a```
- to push current branch to the branchName ```git push origin branchName```

# References
- [Git Branch](https://www.atlassian.com/git/tutorials/using-branches)
