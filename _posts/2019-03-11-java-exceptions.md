---
title: Java Exceptions
categories:
 - java
tags:
 - java, effective java, book, exceptions, class
---

Exceptions improve a program's readability, reliability and maintainability. Basically there are three kinds of throwables: checked exceptions, runtime exceptions and errors. The main rule of using checked exception is this: *use checked exceptions for conditions from which the caller can reasonably be expected to recover*. When the checked exception is thrown, the caller forced to handle the exception in a catch clause or to propagate it outward(forward the exception to upper caller).

Runtime exceptions should be used to indicate programming errors, such as precondition violations. For example the contract for array access specifies that the array index must be between zero and the array length minus one, inclusive. Ex: *ArrayIndexOutOfBoundsException* indicates that this precondition was violated.


## Commonly reused exceptions:

| Exception                       | Occasion for Use                                                             |
|---------------------------------|------------------------------------------------------------------------------|
| IllegalArgumentException        | Non-null parameter value is inappropriate                                    |
| IllegalStateException           | Object state is inappropriate for method invocation                          |
| NullPointerException            | Parameter value is null where prohibited                                     |
| IndexOutOfBoundsException       | Index parameter value is out of range                                        |
| ConcurrentModificationException | Concurrent modification of an object has been detected where it is prohbited |
| UnsupportedOperationException   | Object does not support method                                               |


## Important Notes

- Try to use standard existing exceptions (that way other developers who would have hand on your code could follow easily)
- Higher layers should catch exceptions from lower-level and throw exception from higher level that can be explained from the higher level abstraction
- Documentation should contain exception information (should be documented by @throws tag)
- Failure should leave proper detail message (should be careful to contain password, encryption key information in the log)
- Failure atomicity should be provided (if possible rollback plan should be planned)
- Never leave emtpy catch block 

If it is impossible to prevent exceptions from lower layers, the next best thing is to have the higher layer silently work around these exception. It  means the lower layer does nothing but throwing error and forwarding it to upper layer. Under these circumstances it is appropriate to log the exception using some appropriate logging facility such as *java.util.logging*. This allows programmers to investigate the problem, while insulating client code and the users from it.

Sometimes it may be really needed to leave the catch block empty. In that case the catch block should contain a comment explaining why it is appropriate to do so, and the variable should be named ignored:

```java
Future<Integer> f = excec.submit(planarMap::chromaticNumber);
int numColors = 4; // Default; guaranteed sufficient for any map

try {
    numColors = f.get(1L, TimeUnit.SECONDS);
} catch (TimeoutException | ExecutionException ignored) {
    // Use default: minimal coloring is desirable, not required
}
```









