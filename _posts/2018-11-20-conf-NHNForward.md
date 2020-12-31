---
title: NHN Forward 2018
categories:
 - Conference
tags:
 - conference, java, database, mysql, mybatis, spa
---



Conference organized by NHN Enterprise (Intercontinental Seoul Parnas 19.11.2018) has encompassed many different topics. I have especially was interested in java related topics. How the big tech companies like NHN itself would approach the issue regarding to Oracle recent declarations about Java becoming non-free anymore.

# Agenda

![nhnForward1](/assets/2018-11-19-nhn-conference/program.jpg)

# MyBatis to JPA ( 신동민 Dooray 개발자)

The recent google trend has shown the JPA is more used than Mybatis.

![nhnForward1](/assets/2018-11-19-nhn-conference/1.jpg)

Korea and China are the countries where most of the users are actively using myBatis. 

MyBatis: Sql and codes are separated, that is why calling complicated queries becomes easy. In 2010 it was recognized as a most sql framework.


MyBatis Domain Example

![nhnForward](/assets/2018-11-19-nhn-conference/2.jpg)

Lets try to create a rest api to access this sql from myBatis.

![nhnForward](/assets/2018-11-19-nhn-conference/3.jpg)

![nhnForward](/assets/2018-11-19-nhn-conference/4.jpg)

DataSource manager is used to create sqlSessionFactory beans. 

![nhnForward](/assets/2018-11-19-nhn-conference/5.jpg)

Sql Mapper should be defined. From the source code the sql mapper functions are called. This is the stage where the sql mapper file and source code is binded.

All CRUD operations should be implemented by developer one by one. If we change the DB from Oracle to MySQL, then developer must change the sql queries one by one.


Why should we use JPA ?
The developer doesn’t have to implement the sql by him/herself. The JPA does generate those queries automatically. Another important part of the JPA is code sustaining while lifecycle of the project. If new field is added …


JPA is an ORM

Hibernate, EclipseLink, DataNucleus.

What kind of JPAs are supported by Spring:
- Spring Data
- Spring Data JPA 

![nhnForward](/assets/2018-11-19-nhn-conference/6.jpg)

MyBatis to JPA

What should we do to move from MyBatis to JPA.

Creation of following Beans needed:
- EntityManagerFactory
- TransactionManager

![nhnForward](/assets/2018-11-19-nhn-conference/7.jpg)

EntityManager is created by EntityManagerFactory which contains methods that are perform Sql queries to DB. @Entity, @Table, @ID, @Column, @GeneratedValue, @EmbeddedId, @IdClass, @Temporal, @Transient annotations are used to to declare the Entity.

**Relational Setting**

@JoinColumn
@OneToOne
@OneToMany
@MnayToOne
@ManyToMany

@mappedBy ??? Two side join columns

![nhnForward](/assets/2018-11-19-nhn-conference/8.jpg)

JPA repository already contains Pagination, Sorint and CRUID methods. 

![nhnForward](/assets/2018-11-19-nhn-conference/9.jpg) 
JPA creates queries based on the method names. For example in above picture we can see the red characters in the method names. Those names are used to query DB by the JPA.


More complicated queries can be created by third party libraries, such as:
- QueryDsl
- jOOQ

Comparison of the OrderService insert vs MyBatis

![nhnForward](/assets/2018-11-19-nhn-conference/10.jpg)


As a conclusion the number of the routine codes can be reduced when the JPA is used. 

- Sometimes JPA’s language JPQL cannot generate all possible SQL queries, that is why ordinary SQL query can be used. 
- CQRS also possible with JPA

# Java tools for Java (이상민)

JVM Diagnosis
- Real time 
- After run diagnosis
- 
Performance Diagnosis
TDA, Thread Logic, IBM Heap Analyzer, MAT


real time performance diagnosis:  jvm-tools, icmd jhsdb


Service real time performance analysis tools:
APM: Scouter, pinpoint


Jmvtop: CPU, GitHub.com/patric-r/jvmtop   <— almost dead project, last commit performed 3 years ago
Cannot be used in JDK 9

![nhnForward](/assets/2018-11-19-nhn-conference/11.jpg)

 Jvmtop runtime CPU performance

./jvmtop.sh —profile <pid>
 Shows which method, uses how much presentation of the CPU

Java Attach API - is an api which helps to attach real-time running jvm process

![nhnForward](/assets/2018-11-19-nhn-conference/12.jpg)

Above shown how the Attach API works


 Jvm-tools options: ttop, hh, stcap, stcpy, ssa, frame, mx, jps, 

![nhnForward](/assets/2018-11-19-nhn-conference/13.jpg)

stcap is used to create the profiling of the process, and then using **flame graph** tool we can generate the graphically viewed  diagram (Histogram), 


jcmd is similar too jvm-tools uses attach API. 

jcmd hidden function  - VM.log

![nhnForward](/assets/2018-11-19-nhn-conference/14.jpg)
![nhnForward](/assets/2018-11-19-nhn-conference/15.jpg)
![nhnForward](/assets/2018-11-19-nhn-conference/16.jpg)
![nhnForward](/assets/2018-11-19-nhn-conference/17.jpg)

pwdx <pid> (Linux only)

Check the original starting path of the process with given <pid>

Native Memory Tracking (NMT)


It is another hidden function of the jcmd. It tracks which memory has been when allocated and when it is used.

jhsdb - Java Hotspot Debugger. It is provided from java 9. Should not be run on production server, because when you run the jhsdb by attaching to some java process, the java process gets frozen. 

OpenJDK tools
asmtools, brace, deflate, Friday-stats, check, jcov, jcstress, jmh, jol,  …

jcstress: Java Concurrency Stress test


# Why do we use OpenJDK ? (Taking Photo or recording has been forbidden by Presenter)

2018.06.21 Java subscription to Enterprises has been introduced by Oracle (pay money)

Oracle JDK = Open JDK    (is still free) almost same

Java SE License :
- Java SE 6~10  Oracle Binary Code License (BCL)
- Java SE 11 Oracle Technology Network License

Java SE

BCL can be used for free
- If for research and development
- To catch the dev direction

No Embedded devices are included for free usage (must pay a fee)

Types of OpenJDK:
- Oracle OpenJDK
- Azul Zulu
- AdoptOpenJDK
- RedHatOpenJDK
- Eclipse OpenJ9
- Amazon Correto (No-cost, multiplatform)

Naver Going to use AdoptOpenJdk
Kakao decided to build OpenJDK by itself

TCK == Technology Compatibility Kit (20억)
Is something that you can build your own JDK 

github.com/AdoptOpenJDK/vmbenchmarks is JVM benchmarking tool



