---
title: Allow Github Actions IP Addresses at AWS
categories:
 - github, actions, aws
tags:
 - aws, cloud, github, actions, ci, cd, ip, address, security group
---

It is possible to get IP address range from the github according to [this](https://github.community/t/github-actions-ip-ranges/16824). But the issue is that IP addresses are changed every week, It means we have to do the update every week and handle those new IP addresses.

## Solution
We can approach it with one time solution. Whenever the Github action needs to access the AWS resource (which is filtered by SecurityGroup IP address), we can use aws cli commands and register current IP address of the Github Action hosted Machine. 

In order to get IP address of current hosted runner use [this](https://github.com/marketplace/actions/public-ip) github action 

After getting IP address of github actions hosted runner we have to add SG rule where we need it. Then we can run any steps that are required to be allowed to VPC. For example gradle build which runs test cases that could use connection to EC2. When the step is done rule can be removed from Security Group.

Code snippet example (In this example `sg-mongo-from-github` is the security group name which is created in advance and attached to instance which need to be accessed from github workflow):

```yaml
- name: Public IP
  id: ip
  uses: haythem/public-ip@v1.2

- name: Print Public IP
  run: |
    echo ${{ steps.ip.outputs.ipv4 }}

- name: Allow Security group rule to connect Github Actions to Mongo-port
  run: |
    aws ec2 authorize-security-group-ingress --group-name sg-mongo-from-github --protocol tcp --port 27017 --cidr ${{ steps.ip.outputs.ipv4 }}/32

- name: Build with Gradle
  run: ./gradlew build

- name: Deny Security group rule to connect Github Actions to Mongo-port
  run: |
    aws ec2 revoke-security-group-ingress --group-name sg-mongo-from-github --protocol tcp --port 27017 --cidr ${{ steps.ip.outputs.ipv4 }}/32
  if: always()
```

TODO:

Add a description to the SG Rule. For better visualization

