---
title: Quick Code snippets for Gatling
categories:
 - stress-test, gatling
tags:
 - stress, test, gatling, scala
---

Gatling is another trendy stress-test tool, which can be used for simple scenarios, as well as for complex scenarios. Quick start tutorial can be followed from [here](https://gatling.io/docs/current/quickstart)


## Two Scnarios Run Concurently

```java
class TwoScnarios extends Simulation {
    val httpProtocol = http
        .baseURL("http://computer-database.gatling.io")
        .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
        .acceptEncodingHeader("gzip, deflate")
        .acceptLanguageHeader("de,en-US;q=0.7,en;q=0.3")
        .userAgentHeader("Mozilla/5.0 blah blah blah")

    object Search {
        val search = exec(http("request_0")
            .get("/search"))
            .pause(12)
            .exec(http("request_1")
                .get("/computers?f=macbook"))
            .pause(2)
            .exec(http("request_2")
                .get("/computers/144704"))
    }

    object Browse {
        val browse  = exec(http("request_0")
            .get("/browse")
    }

    object Edit {
        val edit  = exec(http("request_0")
            .get("/edit")
    }

    val usersScn = scenario("Users").exec(Search.search, Browse.browse)
    val adminsScn = scenario("Admins").exec(Search.search, Browse.browse, Edit.edit)

    setUp(
        usersScn.inject(rampUsers(10) over (10 seconds)),
        adminsScn.inject(rampUsers(2) over (10 seconds))
    ).protocols(httpProtocol)
}
```

As a result of above code we will have two kind of users (10 Users and 2 Admins) will be running together with two predefined scenarios (userScn and adminScn accordingly). `over (10 seconds)` means ramping up number of users over given time. If explain in a more clear english, in 10 seconds we would like to see 2 admins are running (or in 10 seconds 10 users must be running). `rampUsers` function does not guarantees the number of concurent users before given time and after given time (actually plus/minus few seconds can be allowed). For detail you can check [video](https://youtu.be/gOZvtBYzIVc?t=1115) presentation of Niko Kobler.


## Global Assertions

Global assertions can be defined and if the assertion fails then the whole scenario stops with assertion exception

```
setUp(
    scn.inject(atOnceUsers(1))
)
    .protocols(httpConf)
    .assertions(
        global.responseTime.max.lessThan(100),
        global.successfulRequests.percent.greaterThan(95)
    )
```

above assertions are doing followings:
- if the maximum response time becomes greater than 100ms then exception is thrown
- if the successful request percentation decreases 95% then exception is thrown

## Check and Failure Management

By default Gatling checks if the http response status is 20x or 304. To demonstrate failure management we will introduce a check on a condition that fails randomly

```java
import java.util.concurrent.ThreadLocalRandom // 1

val edit = exec(http("Form")
  .get("/computers/new"))
  .pause(1)
  .exec(http("Post")
    .post("/computers")
    .check(status.is(session => 200 + ThreadLocalRandom.current.nextInt(2)))) // 2
```

Explanations:
- First we import ThreadLocalRandom, to generate random values.
- We do a check on a condition that’s been customized with a lambda. It will be evaluated every time a user executes the request and randomly return 200 or 201. As response status is 200, the check will fail randomly.

To handle this random failure we use the tryMax and exitHereIfFailed constructs as follow:

```java
val tryMaxEdit = tryMax(2) { // 1
  exec(edit)
}.exitHereIfFailed // 2
```

Explanations:
- tryMax tries a given block up to n times. Here we try a maximum of two times.
- If all tries failed, the user exits the whole scenario due to exitHereIfFailed.

## Real Time Monitoring

Gatling supports graphite protocol which can store values in time-series data store (such as influxdb). After that we can use Grafana to visualize time-series data on it.

## Distributed Testing

Distributed testing can be reached by applying following technologies

- Build-Slaves
- Fat-jars
- Influx (& Grafana)

Otherwise frontline cloud solution can be used, but which will cost you some money