---
title: DDD, Aggregate
categories:
 - DDD
tags:
 - Domain-Driven-Design
---


## What is Aggregate ?

> An **aggregate** is a cluster of associated objects that we treat as a unit for the purpose of data changes.
>
> <cite>Eric Evans (Domain-Driven Design)</cite>

Aggregation is needed to overcome the complexity of the architecture. Complexity in the architecture eventually will cause the complexity in the source code. If the complexity level is gets higher than what human capable to understand then the probability of injecting a buggy code increases. Doesn't matter how professional is the developer.

![No Image](/assets/2018-05-09-ddd-aggregate/BrainCapacityAndCodeComplexity.png)

An Aggregate is a cluster of associated object that we treat as a single unit. Considering every object of the Design separately may and will cause big complication. In order to avoid the complexity the aggregates are used. Here is the example complexity view of the middle sized project:

![No Image](/assets/2018-05-09-ddd-aggregate/tackling_complexity.png)

Lets consider the aggregation in a simple example of organizing following objects: Customer, Address, Product and Component. From the domain knowledge we can say that each customer would have address related information. Correspondingly products are usually made of componenets. Conclude from these domain knowledges we can perform simple aggregation of the objects as follows

![No Image](/assets/2018-05-09-ddd-aggregate/aggregates1.png)

### **Aggregate Root** is the entry point of an aggregate which ensures the integrity of the entire graph
![No Image](/assets/2018-05-09-ddd-aggregate/rootNonRoot.png)

Root object going to have relationship to many children objects by reference or by naming(Guid). In our example the Appointment root object has a 1 to N relationship with following children objects: Client, Exam Room, Patient, Doctor, Appointment Type

![No Image](/assets/2018-05-09-ddd-aggregate/AppointmentContents.png)

If the Aggregation is designed well then cascaded saving operation can be performed easily. For example Appointment root object has saves new appointment. Accordingly we can update related objects with new appointment record.
![No Image](/assets/2018-05-09-ddd-aggregate/cascadingSave.png)



### Simple Tips about Aggregates
- Aggregates can connect only between root aggregates
- Use FK (Foreign Key) to access children of the associated aggregates is possible
- "Aggregate of One": Single object can be a root aggregate (without any childs)
- In the Aggregates:
  - *Entity* can belong to single aggregate only
  - *Value Object* can belong to multiple aggregates
- **Persistent Ignorant Class:** Classes that have no knowledge about how they are persistent


### Relationships Between Aggregates
The root object can hold the pointer to the non-root object, but non-root object should not hold the reference to the root object. Non-root object can hold the foreign case based link to the root object. For example it could have an ID (Guid) number of the root object.

Root-to-Root allowed
![No Image](/assets/2018-05-09-ddd-aggregate/rootToRootAllowed.png)

Root-to-Object is not allowed
![No Image](/assets/2018-05-09-ddd-aggregate/rootNonRootNotAllowed.png)



### **Invariant:** 
Invariant is a condition that must remain (true) to keep aggregate consistent. Depend on the product it must have some specific constraints that should be always remain consistent.

![No Image](/assets/2018-05-09-ddd-aggregate/ProductInvariants.png)

Ex: humonoid-lego product must have 2-arms, 2-hands, 2-legs, 1-pelvis, 1-body, 1-head. If one of those components are missing, then the consistency of humonoid-lego is broken and it cannot be considered as a full product.

Some examples of the Invariants:
![No Image](/assets/2018-05-09-ddd-aggregate/exampleOfInvariants.png)


### **Cascading Deletion Rule**
If the root-aggregate is deleted, then associations should be deleted too. If in our previous example with lego we delete the humonoid-lego object but remain it parts, then they would have no meaning for the system. It will cause the collection of the useless garbage in the system.

![No Image](/assets/2018-05-09-ddd-aggregate/CascadingDeletion.png)

**WARNING: Aggregates are not always the answer to the problem !!!**

