---
title: ComponentScan vs scanBasePackages
categories:
 - spring boot, component, intellij
tags:
 - spring, spring-boot, component, scan-base packages, 
---

There are saying that "if you understand component scan, you understand spring framework". It is quite true since main advantage of spring framework is lies on dependency injection. To do the dependency injection the packages must be scanned for necessary beans. Obviously when the project is big (mostly multiple projects are used together) there are many beans are necessary for scan. There are two methods for bean scanning in spring:

```
@SpringBootApplication(scanBasePackages = "io.rusyasoft.project")
```

vs

```
@ComponentScan("io.rusyasoft.project")
```

My search from google shows these two annotations are same, but the result of trying it on IntelliJ IDEA 2018.2.5 didn't work well. Official answer say that scanBasePackages is the upgraded version of ComponentScan.

`scanBasePackages` - @SpringBootApplication annotation parameter was showing the error line under the bean with Autowiring annotation. Says it couldn't find the bean. But interestingly gradle bootrun command works well and runs without any problem.

`@ComponentScan` - this annotation for scanning beans work as expected and IntelliJ recognizes well and all the errors disappeared.



Tried IntelliJ version:

```
IntelliJ IDEA 2018.2.5 (Ultimate Edition)
Build #IU-182.4892.20, built on October 16, 2018
Licensed to WITH INNOVATION Corp / 루 스탐
You have a perpetual fallback license for this version
Subscription is active until November 8, 2019
JRE: 1.8.0_152-release-1248-b19 x86_64
JVM: OpenJDK 64-Bit Server VM by JetBrains s.r.o
macOS 10.13.6
```
