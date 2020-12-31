---
title: Cloud Native Infrastructure (Book Review)
categories:
 - Book-review
tags:
 - books, review, cloud-native, microservices, design, Justin Garrison, Kris Nova
---

The book was what I have expected from infrastructure book. There were no source codes, except some configuration scripts. The book contains the collection of experiences about cloud-native solutions. It mostly talks about development and deployment processes on the cloud-native. 

The book starts with an explanation of what does Cloud Native Infrastructure means, benefits and philosophy. 

Another interesting chapter was about Testing Cloud Native Infrastructure. Where usual topics regarding testing were covered. Things like: Infrastructure Assertion (Usage of Assert commands), Integration Testing, Unit Testing, Mock Testing. Among these testing approaches Chaos Testing was something new to me. Intuitively most of us thought about such testing, but here it was kind of mentioned and more like organized.

### Chaos Testing

It's testing to demonstrate that unpredictable events in infrastructure can occur, without jeopardizing the stability of the infrastructure. Chaos Testing performed by intentionally breaking and disrupting infrastructure, and measuring how the system responds to the catastrophe. Chaos Testing could be performed by writing a software that is designed to break the production system in unexpected ways. The author gives some example of performing Chaos Testing without actually implementing any software. He modifies the network configuration by entering wrong network IP address which belongs to the outside of the reach of the current network. Then he demonstrates how the response time latency increases. 

#### Netflix's Simian Army

Another interesting fact given by the author is regarding Chaos testing at Netflix. Netflix has introduced what it calls the Simian Army to cause chaos in its systems. Monkeys, apes and other animals in the simian family are each responsible for causing chaos in different ways. Netflix explains how one of these tools, Chaos Monkey, works:

> This was our philosophy when we built Chaos Monkey, a tool that randomly
> disables our production instances to make sure we can survive this common
> type of failure without any customer impact. The name comes from the idea
> of unleashing a wild monkey with a weapon in your data center (or cloud 
> region) to randomly shoot down instances and chew through cables — all the
> while we continue serving our customers without interruption. By running 
> Chaos Monkey in the middle of a business day, in a carefully monitored 
> environment with engineers standing by to address any problems, we can 
> still learn the lessons about the weaknesses of our system, and build 
> automatic recovery mechanisms to deal with them. So next time an instance
> fails at 3 am on a Sunday, we won’t even notice. [Details](https://medium.com/netflix-techblog/the-netflix-simian-army-16e57fbab116)




