---
title: Rich versus Anemic Domain Model
categories:
 - DDD
tags:
 - DDD, algorithms, model, anemic
---

Rich domain model, much of the domain logic implemented within the entities at the core of the application. The entities provide methods to change state and only allow changes that are valid according to the business rules. Many of the business rules are located in the entities instead of the use case implementation.


Anemic domain models are very thin. They usually only provide fields to hold the state and getter and setter methods. They don't contain any domain logic. This means that the domain logic is implemented in use case classes. Use case classes are responsible for validating business rules, changing the state of the entities, and passing them into the outgoing ports responsible for storing them in the database. Here the key word is "use case classes" which may confuse most of us. Use cases represents services in our code (In this context services is the service layer, which is located below  controller and above repository layers)


Another disadvantage of the anemic model has related to dependencies. Dependencies on methods that we do'nt need in our context to make the code harder to understand and to test. Lets see the following method call flow (in the original book this example was used for another purpose, but I have decided to use it for explaining anemic model)

![image](/assets/2020/ddd/figure-6-2.png)

If we need to write a unit test for RegisterAccountService, we have to know which methods in AccountRepository interface do we have to create a mock for. Of course if we are an original creator of this project (or this part of the project) and not long time passed since we worked with this project, then we can do it quickly and may be even without checking the source code. But imagine another developer who need to this job (in this context another developer could be we ourselves after few month), then some research of the source code is needed. Because not knowing how methods are binded there will be tedious errors triggered. It is good to mention Martin C.Robert's words here:

>
> Depending on something that carries baggage that you don't need can cause you
> troubles that you didn't expect. (Clean Architecture by Robert C.Martin, page 86)
>

Here how it can be resolved by applying outgoing ports:

![image](/assets/2020/ddd/figure-6-3.png)

Now each service only depends on the methods it actually uses. The questions like "What should we mock in order to test RegisterAccountService" can be answered very easily (answer is CreateAccountPort). Of course we may say it doesn't solve our problem completely, but it shrinks down the scope of our analysis. Means we still have to research method list in CreateAccountPort. But still it is much better than research on bigger and common interface like AccountRepository (from previous figure).


Unit Test case should test business logic, not the implementation detail. This can be served as a good indicator for knowing the quality of the test case. If the test case fails when we change to code, but didn't change the business logic, then this test case implementation is wrong.