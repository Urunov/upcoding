---
title: Continuous Delivery with Docker and Jenkins (Book Review)
categories:
 - Book-review
tags:
 - books, review, deployment, jenkins, gradle, docker, cloud-native, microservices, design, Rafat Leszko
---

It took a while to finish up this book, since it contains so many interesting topics. I would strongly recommend to service developers(especially the one who work with microservices), QAs, Infra and Engineering Managers. The books covers wide range of topics: Jenkins, Docker, Docker Swarm, Ansible, CI/CD, Automated Acceptance Testing.

Each chapter described interestingly, and book reading is quite clear. Most chapters are depend on each other, but reading separate interested chapter also possible. I have especially loved the last chapter-9 (Advanced Continuous Delivery). It talks about how to reach zero-downtime deployment. Not only describing it theoretically but also gives an real examples such as Blue-Green and Canarary deployments.


# Some notes

- Jenkins Swarm allows you to dynamically add slaves without the need to configure them in Jenkins master
- There are a lot of tools available to perform the test coverage analysis; for Java, the most popular are JaCoCo, Clover and Cobertura
- Development workflow is the way how team puts the code into the repository. It depends, of course, on many factors such as the source control management tool, the project specifics, or the team size. Can be classified into three types: trunk-based workflow, branching workflow, forking workflow
- Feature toggle is a technique that is an alternative to maintaining multiple source code branches such that the feature can be tested before it is completed and ready for release. 

```java
if (feauture_togle) {
    // do the new feature works
}
```

- Types of environments: Production, Staging, QA, Development
- Usability Engineering, Jakob Nielsen, writes that 1.0 second is about the limit for the user's flow of thought to stay uninterrupted. Imagine that our system, with the growing load, starts to exceed that limit. Users can stop using the service just because of its performance.
- Security should also always be a part the explanatory testing process, in which testers and security experts detect security holes and add new testing scenarios.

- Draining the node means asking the manager to move all tasks out of a given node and exclude it from receiving new tasks
- Flyway is used to create and migrate databases from one cluster to another one.
