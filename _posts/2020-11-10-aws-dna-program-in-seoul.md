---
title: AWS DNA Program in Seoul (Korea)
categories:
 - activity
tags:
 - aws, cloud, dna, digital native architects, solution architects, SA
---

AWS DNA(Digitial Native Architects) program has been developed and introduced by DNB team of AWS Korea. They have selected 20 people from various IT companies who actively uses AWS Cloud. Shortly the goal of this program is to teach us how to use AWS as a pro. Five teams were formed out of 20 people, at the end of the course teams supposed to come up with they Case Study and Build-up a prototype that has a stable business model and could become a real startup in a future.

We gathered once a week after a work time (they have provided us with dinner and snacks) and listened lectures from SA(Solution Architects) about advanced topics in AWS. Here are the list of topics we studied during this course:

- Microservice on AWS (IaC, EKS)
    - Amazon EKS
    - AWS CDK
    - CodeCommit, CodeDeploy
    - Container Insight
- Full Serverless App with React and Amplify
    - AWS Amplify
    - Amazon CodeGuru
    - Amazon Athena
    - Amazon Cognito
    - Amazon Pinpoint
    - API Gateway
    - Lambda
    - Amazon DynamoDB
    - Amazon S3
- AWS Hybrid Networking
    - AWS Transit Gateway
    - VPC Traffic Mirroring
    - AWS Systems Manager
    - Cloudformation
    - Amazon ElasticSearch Service
- Optical Character Reconition on AWS (Octember)
    - Amazon Kinesis
    - Amazon S3
    - Amazon API Gateway
    - AWS Lambda
    - Amazon Elasticsearch Service
    - Amazon ElastiCache
    - Amazon DynamoDB
- Recommanded Emotions in the Online Chatting/SMS Applications
    - Amazon SageMaker
    - Amazon EKS
    - AWS Cloud9
- File Traffic Optimized Architecture
    - Amazon CloudWatch
    - AWS CodeStar
    - AWS Lambda
    - Amazon DynamoDB
    - AWS X-ray
- Migration Three-tier Architecture on AWS
    - Amazon Aurora
    - CloudEndure
    - CodePipeline
    - Amazon Elasticsearch Service
    - AWS Database Migration Service

As it can be seen it was fun and very active weeks (starting from the middle of july, to the september) of study. The study was very productive in the following format:
- SA gives a lecture 
- SA provides us with homework HoL(HandOnLab)s. 
- Till next week we do the homework HoLs and if have some troubles ask them on slack channel. 
- Next meeting before starting of the lecture we go through the troubled issues and get answers

The HoLs were very easy to follow. Yeah I mean it, following their handsOnLabs were very fun and easy, because most of them were implemented with CDK, CloudFormation or scripts. So we had to just sit and enjoy the labs, and all source codes were shared.

Solutions Architects were really smart and they allowed us to ask any questions regarding to AWS. When I say any, I mean literally any question regarding to aws. Even if they didn't know, they would find out for the next meeting and explain it with every techincal details.

Every weekend they have organized Office hours for the members of DNA. You could just subscribe for participation in office hour with your name and the topic you want to know in more detail. On weekend the SA man will come and meet you and answer almost any questions you ask.

As I have mentioned, we gathered every week (around Gangnam area) and all meeting were offline, though later on august the COVID-19 level was increased in Korea. So we had to continue our aws-dna meetings online. Here is overal picture of some members from our meeting.

![img](/assets/2020/aws/dna/online-gathered-picture.png)

## Final Project

As a final result five teams prepared their own final prototype cases and it was really interesting. Our team prepared Alexa DevOps helper kind of solution which would help Software companies to handle their DevOps related issues. We named it as Jarvis DevOps (the name Jarvis inspired from Iron Man movie).

Small Introduction about Jarvis DevOps:
- Jarvis is a technical support assistant using Alexa that can better manage Vendors AWS Service and help to provision services.
- As an example: Jarvis can get EC2 CPU-Utilization info, stop/start or run Scaling at the user's command.
- Jarvis is a SaaS service running on AWS, so vendors can use services that fit their needs without developing separate Alexa skills.

The following technology stacks were used in order to develop Jarvis DevOps:
- ASK(Alexa Skill Kit)
- AWS amplify
- Lambda
- Cognito
- DynamoDB
- API Gateway
- Serverless Framework

### Here how Jarvis DevOps operates

![img](/assets/2020/aws/dna/jarvisHowItWorks.png)

### Architecture of Jarvis DevOps Infrastructure

![img](/assets/2020/aws/dna/jarvisDevOpsArchitecture.png)

### Jarvis DevOps Project Demo

<iframe width="560" height="315" src="https://www.youtube.com/embed/HM7ILoL-tkk" frameborder="0" allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture" allowfullscreen></iframe>

Source Codes:

- https://github.com/aws-dna-4team/jarvis-agent (Public repository)
- https://github.com/aws-dna-4team/jarvis-manager-amplify (Private repository)
- https://github.com/aws-dna-4team/jarvis-ask (Private repository)


## Conclusion

As a result we studied very broad topics and developed really good human-network. We still keep our slack channel where time to time people ask questions to Solution Architects and share news about usefull AWS related events.

Here is the interview with some members of the DNA program, where they share their opinion about the program. The interview is in Korean: https://youtu.be/OoF1bV0Z0LU
