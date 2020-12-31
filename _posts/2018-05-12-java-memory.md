---
title: Java Memory Related Notes
categories:
 - Java
tags:
 - Java, GC, Garbage Collector, JVM
---
Some usefull collection of notes regarding to Java memory management has been collected. You can refer to some of them for quick remainder

# Unmodifiable Collections
Unmodifiable collections are important while returning collection from inside function should be locked for any kind of changes

```java
private Map<String, Customer> records;

public Map<String, Customer> getCustomers() {
   return Collections.unmodifiableMap(this.records)
}
```


We have to be careful with escaping references, which allow to caller object do mutation operations over a controlled object. Escaping references can be protected by covering the class with interfaces. For example, the original class has getObject, setObject methods. But we can cover this class with the interface where the only getObject method will be declared. That way user can't call setObject method directly. This is a basic trick used for protecting object and make it as a read-only object.



# Internalized Strings
When the two strings are created under the same scope two strings are same, then Java is smart enough to create the only single object and share the reference between those two objects. It is called internalized strings, shown below:

```java
public static void main() {
   String one = "hello";
   String two = "hello";
}
```
Generally, it happens with literal strings that are defined statically. Java stores them inside the string pools. If the string is coming out as a result of some calculations, then it is not the case.

But sometimes we can enforce the java to save some calculation result string into string pool. So later we can reuse this string inside our code scope. In order to enforce the java to save the string into string pool, we should use intern() method. Here is an example:

```java
String three = new Integer(13).toString().intern();
String four = "13";
Assert.assertEquals(three, four);  
```

# GC: Generational Garbage Collection
Separate the objects located in the heap into partition of young and old generation based on their time of allocation. Depend on the age of the object it can move older generation partition.

![Generational GC](/assets/2018-05-12-java-memory/GenerationalGarbageCollection.png)

GC operation starts with different interval on those partition. For the older generation the GC process runs has bigger interval (Major collection) compared to younger partition. Major collection could take few seconds rather than a fraction of the seconds. The behavior of the GC can be visualized by using *jvisualvm* tool, by installing *Visual GC* plugin.



# Some System and Runtime tricks

**System.gc()** is the method that suggests the GC to run and do some cleanup. Since it is just suggestion there is no guarantee that GC will run. 

**Runtime.getRuntime().freeMemory()** is the method that returns *long* type with remaining memory (in KBytes) allowed to use for current application.

**finalize()** is the method that is called by GC before disposing the current object from the system. It is also ambiguous to use, because if we create about 1000s objects and then close the scope to let GC do its job, not all of them may be removed from the system right away. As I understood it is unstable to use finalize(), especially for closing some database connections, could cause a big problem!

## Tuning the VM
- **-Xmx**: set the maximum heap size
- **-Xms**: set the starting heap size
example: *-Xmx512m -Xms150m*
- **-Xmn**: set the size of the young generation
- **-XX:MaxPermSize**: set the size of permgen (exist only in java6 and java7)
example: *-XX:MaxPermSize=256M*
- **-XX:HeapDumpOnOutOfMemory**: creates a heap dump file when the out of memory error happens
- **-verbose:gc**: print to the console when a GC takes place

# Tools
- **jvisualvm**: Java VisualVM is an intuitive graphical user interface that provides detailed information about Java technology-based applications (Java applications) while they are running on a specified Java Virtual Machine (JVM). The name Java VisualVM comes from the fact that Java VisualVM provides information about the JVM software visually.
- **MAT (Memory Analyzer)**: java heap memory anlyzer tool that helps us to find out memory leaks and reduce memory consumption


# Terminologies
- **Memory Leak**: Objects that are not freed continue to consume memory. Usually, they are not consumed because some reachable references on them still hold and GC didn't clean them out.
- **Eligible for GC(Garbage Collection)**: Any object on the heap which cannot be reached through a reference from the stack. References inside the heap are not counted. It should be reachable from stack otherwise bye bye object, GC will eat you out :P
- **GC Algorithm: Mark and Sweep**: Algorithm that searches the objects that are reachable from the stack and marks them. When marking is finished it removes all objects that are not marked. 

