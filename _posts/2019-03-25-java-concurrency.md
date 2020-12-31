---
title: Java Concurrency
categories:
 - java
tags:
 - java, effective java, book, concurrency, class
---

The synchronized keyword ensures that only a single thread can execute a method or block at one time. It should not be confused with a mutual exclusion concept, that prevents an object from being seen in an inconsistent state by one thread while it's being modified by another. Proper use of synchronization keyword guarantees that no method will ever observe the object in an inconsistent state. 

The language specification guarantees that reading or writing a variable is atomic unless the variable is of type long or double. Synchronization is required for reliable communication between threads. 

Example that explains best

```java
public class StopThread {
    private static boolean stopRequested;
    public static void main(String[] args) throws InterruptedException {
        Thread backgroundThread = new Thread(() -> {
            int i = 0;
            while (!stopRequested)
                i++;
        });
        backgroundThread.start();

        TimeUnit.SECONDS.sleep(1);
        stopRequested = true;
    } 
}
```

The code above ambigious, because if we run it the result is not guaranteed. It may stop after 1 second, or never stop.  In case of default compiling it will not stop. In the absence of synchronization, it's quite acceptable for the virtual machine to transform this code:

```java
while (!stopRequested)
    i++;
```

into following code:

```java
if (!stopRequested)
    while (true)
        i++;
```

That kind of optimization is called as *hoisting*, and it is precisely what the OpenJDK Server VM does. The problem can be fixed with usage of *synchronize*.

```java
public class StopThread {
    private static boolean stopRequested;

    private static synchronized void requestStop() {
        stopRequested = true;
    }

    private static synchronized boolean stopRequested() {
        return stopRequested;
    }

    public static void main(String[] args) throws InterruptedException {
        Thread backgroundThread = new Thread(() -> {
            int i = 0;
            while (!stopRequested())
                i++;
        });
        backgroundThread.start();

        TimeUnit.SECONDS.sleep(1);
        requestStop();
    }
}
```

We must make it sure to both read and write operations should be wrapped with *synchronize* keyword checks. 

The same result can be enriched with usage of *volatile* modifier. It performs no mutual exclusion, it guarantees that any thread that reads the field will see the most recently written value:

```java
public class StopThread {
    private static volatile boolean stopRequested;

    public static void main(String[] args) throws InterruptedException {
        Thread backgroundThread = new Thread(() -> {
            int i = 0;
            while (!stopRequested)
                i++;
        });
        backgroundThread.start();

        TimeUnit.SECONDS.sleep(1);
        stopRequested = true;
    }
}
```

Even though *volatile* seems ultimate solution, it can be tricky sometimes. The increment/decrement operators (++/--) are not atomic. It actually contains two operations: first reads the value, and then it writes back a new modified value. If second thread reads the field between those operations, then second thread will see the same value as the first and return the same not modified value. That is why *java.util.concurrent.atomic* package has been introduced.

Syncrhonize is the good tool but again it must be used carefully. It can easily turn into deadlock where one, thread calls runs the synchronized method and performs blocking operations. While other threads will wait for current blocked thread. As a rule, we should do as little work as possible inside synchronized regions.

## CountDownLatch

Countdown latches are single-use barriers that allow one or more thread to wait for one or more other threads to do something. The sole constructor for CountDownLatch takes an int that is the number of times the countDown method must be invoked on the latch before all waiting threads are allowed to proceed.

```java
public static long time(Executor executor, int concurrency, Runnable action) throws InterruptedException {
    CountDownLatch ready = new CountDownLatch(concurrency);
    CountDownLatch start = new CountDownLatch(1);
    CountDownLatch done = new CountDownLatch(concurrency);

    for (int i = 0; i < concurrency; i++) {
        executor.execute( () -> {
            ready.countDown(); // Tell timer we're ready
            try {
                start.await(); // Wait till peers are ready
                action.run();                
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                done.countDown(); // Tell timer we're done
            }
        });
    }
    ready.await();      // Wait for all workers to be ready
    long startNanos = System.nanoTime();
    start.countDown();  // And they're off!
    done.await();       // Wait for all workers to finish
    return System.nanoTime() - startNanos;
}
```

The first, ready countDownLatch is used by worker threads to tell the timer thread when they're ready. The worker threads then wait on the second latch, which is start. When the last thread hits the ready.countDown, the timer thread records the time and invokes start.countDown which has only single latch. So the code starts action.run() for all threads. Then the timer thread waits on the third latch, done, until the last of the worker threds finishes running the action and calls done.countDown.

For interval timing, always use System.nanoTime rather than System.currentTimeMillis. System.nanoTime is both more accurate and more precise and is unaffected by adjustments to the system's real-time clock.
