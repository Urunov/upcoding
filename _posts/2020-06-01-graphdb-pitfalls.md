---
title: Common Modeling Pitfalls in GraphDB
categories:
 - database
tags:
 - graphdb, neo4j, graphs
---

In order to avoid pitfalls we have to check the design for queryability
- Describe the client or end-user goals that motivate our model
- Rewrite these goals as questions to ask of our domain
- Identify the entities and the relationship that appear in these questions
- Translate these entities and relationships into Cypher path expressions
- Express the questions we want to ask of our domain as graph patterns using path expressions similar to the ones we used to model the domain

# Email Modeling Comparison

![img](/assets/2020/graphDbImages/10-MissingEmailNode.png)

![img](/assets/2020/graphDbImages/11-EmailAsNode.png)

Email as a node expands the domain capability

## Conclusion Elements from Email Example

- Common nouns become labels: "user" and "email"
- Verbs that take an object become relationship name "sent" and "wrote"
- Detail attributes related to noun becomes a properties. For example name of the user, or title of the email

- Avoid Anti-patterns by not Verbing (the language habit whereby a noun is transformed into a verb) can hide the presence of a noun. By other words  think over business jargon 
  - Jargon: "Email one Another", Actually: "Send an Email"
  - Jargon: "Search Google", Actually: "Search for Results"

 ## Fine-Grained versus Generic Relationships

- When designing relationships has trade-offs between using fine-grained relationships versus generic relationships:
  - DELIVERY_ADDRESS, HOME_ADDRESS
  - ADDRESS {type: 'delivery'}, ADDRESS {type: 'home'}

# Model Facts as Nodes

- When two or more domain entities interact for a period of time, a fact emerges
- Fact should be represented as a separate node with connections to each of the entities engaged in that fact

![img](/assets/2020/graphDbImages/12-TimeFactAsANode.png)

```
CREATE (:Person {name:'Ian'})-[:EMPLOYMENT]->
(employment:Job {start_date:'2011-01-05'})
-[:EMPLOYER]->(:Company {name:'Neo'}),
(employment)-[:ROLE]->(:Role {name:'engineer'})
```

# Timeline Trees

![img](/assets/2020/graphDbImages/13-TimelineTrees.png)

- Episodes of the Drama Series can have following type of timeline graph with dates
- Additionally Episode nodes may have relationship edges among themselves with labels “next” and “previous” which would accelerate iteration over episodes

# Iterative and Incremental Development

![img](/assets/2020/graphDbImages/14-IterativeAndIncrementalDevelopment.png)

- Graph databases provide for the smooth evolution of data model
- Migrations and denormalization are rarely an issue
- New facts become new nodes
- New compositions become new relationships
- New and legacy relationships may exist at the same time before the application level codebases are modified


