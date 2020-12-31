---
title: How to prepare for AWS Solutions Architect Associate Exam
categories:
 - certification, aws, saa
tags:
 - aws, cloud, saa
---

There are number of courses and guidances about how to pass one of the most popular exams: AWS Certified Solutions Architect Associate. AWS is one of the top cloud providers of our days, and having one of the AWS certificates (yeah there are many kinds) would only help in Your carrier. I personally not big fan of exams, because while preparing for exam you become obsessed to pass the exam and may simply start memorizing everything. I prefer study everything deeply, aws is the real rabbit-hole if you start digging deeply. In this short post would like to share my experience how did I prepare and passed AWS Solutions Architect Associate Exam.


Here are study materials I have used:
- **[Cloud Guru Course from Udemy](https://www.udemy.com/course/aws-certified-solutions-architect-associate/)**. This course was short and compact. As far as I know Cloud-Guru is very popular among aws certification preparation courses. Almost anyone who I have asked for advice told me they studied through this course. But, In my personal opinion it wasn't enough. May be in order to fairly conclude I had to take other courses from Cloud-Guru, but studying this course only would not be enough for me to pass the exam. 
- **[DolfinED's course from Udemy](https://www.udemy.com/course/aws-certified-solutions-architect-associate-exam/)**. This is very long course (more than 84 hours) and really good enough for the exam. The speaker is very professional and explains every pitfalls of the aws-resources one-by-one. It was really fun to study with this course, because an author is really concentrating to the important parts of the aws and also tells the possible pitfalls. He teaches how to wear an architect's hat and approach the problem in a right way. He goes through the questions by synthesizing them and what should you pay attention in the question. Definitely recommend this course for exam preparation, but calculate Your time right. As I said this course is very long and interesting.
- **[Study Guide book](https://www.amazon.com/Certified-Solutions-Architect-Study-Guide/dp/111950421X)**. If you prefer reading rather than watching online courses then this book is your friend. 
- **[Tutorials DoJo](https://tutorialsdojo.com/links-to-all-aws-cheat-sheets/)**. This tutorial may be one of the best among the aws tutorials. Every aws resource explained in very details, with exam orientation

## Topics You have to prepare

- VPC
    - Subnets, Route Tables, Internet Gateway [*You definetly need to know these stuffs, because it is like a butter and bread in AWS Cloud*]
    - Elastic IP Address
    - Security Groups and N.ACLs [*Please make it sure you know what are these and clearly differentiates them*]
    - NAT Instance, NAT Gateway [*Make sure you know the difference of these two*]
    - VPC Peering, VPN, Direct Connect (DX), Transit Gateway [*I met about three questions regarding to DX*]
    - VPC Endpoints(PrivateLink)
- EC2, Auto-scaling
- ELB
    - ALB, NLB, CLB
- EBS, EFS, FSx, FSx for Lustre
- S3
    - Glacier
    - S3 SELECT
- RDS
    - AWS Aurora
    - AWS Aurora Serverless
- SNS
- SQS
- DynamoDB, DAX
- IAM (Identity and Access Management)
    - ID Federation
    - SSO
- Cloud Watch, Cloud Trail
- Route53
    - Different Policies of DNS Routing
    - Geolocation vs Geoproximity [*Clearly [distinguish](https://tutorialsdojo.com/latency-routing-vs-geoproximity-routing-vs-geolocation-routing/) these two policies*]
- CloudFront
    - Geo Restrictions in CloudFront
- Global Accelerator
- WAF (Web Application Firewalls)
- Lambda
    - Configuration
    - Invocation (Supported Triggers)
    - Scaling limits
    - Monitoring and Pricing
    - Lambda Edge
- API Gateway
- EMR (Elastic Map Reduce)
- ElasticCache
    - Redis
    - Memcached
- Amazon Kinesis
    - Kinesis Streams
    - Kinesis Firehose
    - Kinesis Analytics
- Redshift
- ECS (Elastic Container Service)
- ECR (Elastic Container Registry)
- AWS Active Directory Services
    - MS AD
    - AD Connector
    - Simple AD

- Storage Gateway
    - File Gateway
    - Volume Gateway
    - Tape Gateway: VTL (Tapes), VTS (Glacier)

## Feeling about Exam
In total exam was organized very professionally. I have taken an exam at one of the Pearson VUE (in Seoul around Gangnam area) places. The exam application was running as a Windows Application (Or at least it felt that way), which slightly shoked me. Feeling about questions: almost 10% percent of questions were related to AWS Storage Gateway (may be because I wasn't much prepared for that question) and another 10% were about dealing with connection on-Premise and AWS Cloud.

