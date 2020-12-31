---
title: Possible Issues with ElasticBeanstalk Platform Upgrade
categories:
 - devops
tags:
 - devops, aws, ElasticBeanstalk, amazon, deployment, ci, cd
---

When AWS ElasticBeanstalk informs about upcoming platform upgrades, we must take it seriously and prepare our CI scripts. Because when I first time saw the warning that says "Platform update scheduled", didn't pay much attention. When clicked for details it said:

```
Managed updates overview
A new platform version is available. A platform update has been has been scheduled to run during the next maintenance window, between Wednesday, June 10th 8:00 PM and Wednesday, June 10th 10:00 PM (+0900 GMT). To perform the update immediately, choose Apply Now.
```

one week later after ignoring AWS message two of my services started having a trouble while calling "eb init":

```
ERROR: InvalidParameterValueError - Platform 'arn:aws:elasticbeanstalk:**************::platform/Java 8 running on 64bit Amazon Linux/2.10.7' does not exist.
```

I didn't expect that kind of error message. Thats kind of little tough way of pushing us to upgrade our platform in ElasticBeanstalk. On the other hand that is a good way. Since now aws suggests 2.10.8 it simply says that previous version doesn't exist. Which is automatically prevents us from trying to install older version.

If tight scheduled deployment is planned, then you should prepare in advance. Deployment on many instances would take longer than you expect, because first you have to apply new platform changes to ElasticBeanstalk environment. After that proceed with your new deployment, as a result deployment time is doubled.

Note: If you are not using `eb init` explicitly in your deployment script, but just declaring it using .elasticbeanstalk/config.yml, then don't forget to upgrade there too.

# Some Examples

```
eb init --platform "arn:aws:elasticbeanstalk:ap-southeast-1::platform/Java 8 running on 64bit Amazon Linux/2.10.8" --region ap-southeast-1 MyApplication

eb use MyApplicationEnvironment
eb deploy -l $project_version -m "`git show -s --format=%s HEAD`"
```


# Reference

[ElasticBeanstalk script commands](https://docs.aws.amazon.com/elasticbeanstalk/latest/dg/eb3-cmd-commands.html)