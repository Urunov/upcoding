---
title: Usage of Resilience4j Retry and CircuitBreaker together
categories:
 - java
tags:
 - java, circuit-breaker, resilience4j, retry, spring-boot
---

Very interesting and confusing thing happens when we try to use resilience4j circuit-breaker and retry core modules together. Because both of them have fallback functionality, and it is very easy to get confused how to use it. if you configure wrong the following things may happen:

- retry happens and circuit-never opens, it means retry's fallback is keep triggering
- retry does not happen and right away circuit-breaker's fallback functionality triggers
- retry happens as expected, but after retry exception is thrown to the caller application

Detail descriptions:

- Retry happens but Circuit-never gets opened, because we didn't defined any circuit-breaker annotation.

```java
@SneakyThrows
@Retry(name = "getNotificationRETRY", fallbackMethod = "retryFallback")
public Response<NotificationResponse> getNotification(String userId, Integer contentId) {
    return RetrofitUtil.resilentProcess(notificationCalls.getNotification(userId, contentId));
}

public Response<NotificationResponse> retryFallback(String userId, Integer contentId, Exception ex) {
    log.error(Log.create("getNotification (retryFallback)").add("userId", userId).add("contentId", contentId).toString());
    return getDefaultFallbackResponse("(retryFallback)");
}
```

The following code may ignore the retries at all and calls `cbFallback` as soon as the `mainFunction` triggers exception. In this context "may ignore" has been used for the case when the property `minimumNumberOfCalls` is smaller than number of retries.

```java
@SneakyThrows
@Retry(name = "mainFunctionRETRY", fallbackMethod = "retryFallback")
@CircuitBreaker(name = "mainFunctionCB", fallbackMethod = "cbFallback")
public Object mainFunction(String userId, Integer contentId) {
    return origial.mainFunction(userId, contentId));
}

public Object retryFallback(String userId, Integer contentId, Exception ex) {
    log.error(Log.create("mainFunction (retryFallback)").add("userId", userId).add("contentId", contentId).toString(), ex);
    return getDefaultFallbackResponse("(retryFallback)");
}

public Object cbFallback(String userId, Integer contentId, Exception ex) {
    log.error(Log.create("mainFunction (circuitBreakerFallback)").add("userId", userId).add("contentId", contentId).toString());
    return getDefaultFallbackResponse("(cbFallback)");
}
```

It is retried only `minimumNumberOfCalls` or `permittedNumberOfCallsInHalfOpenState` times. We can try the same code without defining circuit-breaker fallback. It can become a usable solution if we don't have to distinguish the response case between 1) failed while retrying, 2) failed because Circuit was open. Actually it can be distinguished by checking Exception type.

```java
@SneakyThrows
@Retry(name = "getNotificationRETRY", fallbackMethod = "retryFallback")
@CircuitBreaker(name = "getNotificationCB")
public Response<NotificationResponse> getNotification(String userId, Integer contentId) {
    return RetrofitUtil.resilentProcess(notificationCalls.getNotification(userId, contentId));
}

public Response<NotificationResponse> retryFallback(String userId, Integer contentId, Exception ex) {
    log.error(Log.create("getNotification (retryFallback)").add("userId", userId).add("contentId", contentId).toString());
    return getDefaultFallbackResponse("(retryFallback)");
}
```

The following code works as expected if we are in CLOSE state and `minimumNumberOfCalls` greater or equal to the number of retries. If we are in a HALF_OPEN state then number of retries must be greater than `permittedNumberOfCallsInHalfOpenState`, that way we can perform predefined retries. Difference with the above code is that it separates failed retry and open circuit.

```java
@SneakyThrows
@Retry(name = "mainFunctionRETRY", fallbackMethod = "retryFallback")
@CircuitBreaker(name = "mainFunctionCB", fallbackMethod = "cbFallback")
public Object mainFunction(String userId, Integer contentId) {
    return origial.mainFunction(userId, contentId));
}

public Object retryFallback(String userId, Integer contentId, Exception ex) {
    log.error(Log.create("mainFunction (retryFallback)").add("userId", userId).add("contentId", contentId).toString(), ex);
    return getDefaultFallbackResponse("(retryFallback)");
}

public Object cbFallback(String userId, Integer contentId, CallNotPermittedException ex) {
    log.error(Log.create("mainFunction (circuitBreakerFallback)").add("userId", userId).add("contentId", contentId).toString());
    return getDefaultFallbackResponse("(cbFallback)");
}
```

If we are in CLOSED state and exception is triggered, first all retries are performed then we fall into retryFallback. If the cicuit is in OPEN state, then we go to cbFallback right away.

`permittedNumberOfCallsInHalfOpenState` - the number of calls performed in HALF_OPEN state to decide which state it should switch next (open or close state)

`minimumNumberOfCalls` - Configures the minimum number of calls which are required (per sliding window period) before the CircuitBreaker can calculate the error rate. For example, if minimumNumberOfCalls is 10, then at least 10 calls must be recorded, before the failure rate can be calculated.

If only 9 calls have been recorded the CircuitBreaker will not transition to open even if all 9 calls have failed and single one succeeded.

### Example with Details

Lets say we have a following configurations for circuit-breaker property in application.yml:

```
resilience4j.circuitbreaker:
  configs:
    default:
        slidingWindowSize: 21
        permittedNumberOfCallsInHalfOpenState: 3
        automaticTransitionFromOpenToHalfOpenEnabled: true
        waitDurationInOpenState: 30s
        failureRateThreshold: 100
        minimumNumberOfCalls: 15
```

- STATE: CLOSED; 21 exceptions must be triggered in order to switch from CLOSED -> OPEN (because failureRateThreshold = 100%)
- STATE: OPEN; after 30seconds the state is automatically changed from OPEN -> HALF_OPEN
- STATE: HALF_OPEN; at least 3 calls needed to change from HALF_OPEN -> OPEN (if fails) or HALF_OPEN -> CLOSED (if succeeds). [IMPORTANT: 100% out 3 calls should fail in order to change from HALF_OPEN -> OPEN]

![Graphical illustration](/assets/2020/circuit-breaker/grafana-cb-1.png)

### Another Example

If we change two properties from above example as follow:

```yaml
#
#    remaining properties same as previous example
#
        permittedNumberOfCallsInHalfOpenState: 10
        failureRateThreshold: 100
```

and perform experiment where current state is HALF_OPEN, and throw 9 exceptions, but the last one success will switch to the CLOSED state. Because the failure rate threshold is 100% and only 10 consecutive failures may change to the OPEN state.


# References:

- [https://github.com/resilience4j/resilience4j/issues/558](https://github.com/resilience4j/resilience4j/issues/558)