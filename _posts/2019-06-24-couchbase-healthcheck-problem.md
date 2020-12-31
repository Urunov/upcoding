---
title: Couchbase health check failed
categories:
 - couchbase, nosql
tags:
 - couchbase, nosql, db, health check
---


I have been facing the "Couchbase health check failed" error, which I have never seen so far:

```
2019-06-24 20:28:48.026 WARN  1854950 --- [ http-nio-8300-exec-4 ] o.s.b.a.c.CouchbaseHealthIndicator : Couchbase health check failed
java.lang.NullPointerException: null
	at org.springframework.boot.actuate.couchbase.CouchbaseHealth.describe(CouchbaseHealth.java:63)
	at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:193)
	at java.util.ArrayList$ArrayListSpliterator.forEachRemaining(ArrayList.java:1382)
	at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:481)
	at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:471)
	at java.util.stream.ReduceOps$ReduceOp.evaluateSequential(ReduceOps.java:708)
	at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
	at java.util.stream.ReferencePipeline.collect(ReferencePipeline.java:499)
	at org.springframework.boot.actuate.couchbase.CouchbaseHealth.applyTo(CouchbaseHealth.java:46)
	at org.springframework.boot.actuate.couchbase.CouchbaseHealthIndicator.doHealthCheck(CouchbaseHealthIndicator.java:52)
    ...
```

The search in google didn't help much, because it was keep bringing [link](https://github.com/spring-projects/spring-boot/issues/14685), which talked about timeouts. 

# TLDR

The port 8093 was not accessible from the application server. [link](https://docs.couchbase.com/server/4.1/install/install-ports.html) says that couchbase uses this port for query services. But I'm not sure then why does the error doesn't say about it anything.

