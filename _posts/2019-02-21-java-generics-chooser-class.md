---
title: Java Generics (Chooser Class Example from Effective Java Book)
categories:
 - java
tags:
 - java, effective java, book, generics, class
---

Last time when I read the book Effective Java I met that example about generics from Item-28. Authors have introduced quite a good example with Chooser class, where they are trying to convert it to generic from non-generic class. This time when I'm reading book again I met this example and decided to put it here. 

## Definition of the Example

Suppose we have to write a Chooser class with a constructor that takes a collection, and a single method that returns an element of the collection chosen at random. Depending on what collection we pass to the constructor, we have to be able to choose random element out of the collection. It could be game die, magic 8-ball, or data source for a Monte Carlo simulation. 

## Approaches

Right away the simple implementation of non-generic solution is given:

```java
public class Chooser {
  private final Object[] choiceArray;
  
  public Chooser(Collection choices) {
    choiceArray = choices.toArray();
  }
  
  public Object choose() {
    Random rnd = ThreadLocalRandom.current();
    return choiceArray[rnd.nextInt(choiceArray.length)];
  }
}
```

On the other side to use this class we have to do explicit type casting. If we choose wrong type while performing a type casting then the runtime exception will be thrown.

```java
List<Integer> myList = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4, 5));
Chooser chooser = new Chooser(myList);
Integer resultInteger = (Integer)chooser.choose();  // compiles well, runs well
String resultString = (String)chooser.choose();  // compiles well, runtime exception is thrown
```

Lets try to make it generic class:

```java
public class Chooser<T> {
  private final T[] choiceArray;
  
  public Chooser(Collection<T> choices) {
    choiceArray = choices.toArray();
  }
  
  public Object choose() {
    Random rnd = ThreadLocalRandom.current();
    return choiceArray[rnd.nextInt(choiceArray.length)];
  }
}
```

If we try to compile this class, it will throw the compile time error:

```
Chooser.java:9: error: incompatible types: Object[] cannot be converted to T[]
        choiceArray = choices.toArray();
                              ^
  where T is a type-variable:
    T extends Object declared in class Chooser 
```

this error can be easily removed by casting:

```java
  choiceArray = (T[]) choices.toArray();
```

Even though the error is disappeared the warning appears about "unchecked cast". Warning could be supperessed and some comment could be written. Thats how most of us usually solve this kind of problem. Authors finally gives a typesafe generic solution:

```java
public class Chooser<T> {
  private final List<T> choiceList;
  
  public Chooser(Collection<T> choices) {
    choiceList = new ArrayList<>(choices);
  }
  
  public Object choose() {
    Random rnd = ThreadLocalRandom.current();
    return choiceList.get(rnd.nextInt(choiceList.size()));
  }
}
```

## Short Analysis

This is how we are going to use Chooser class on the other side.

```java
List<Integer> myList = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4, 5));
Chooser chooser = new Chooser(myList);
Integer resultInteger = chooser.choose();  // compiles well, runs well
String resultString = chooser.choose();  // compiles time error is thrown
```

The last version is more verbose and slower, but it's worth for a peace of mind. We won't get ClassCastException at runtime. If there is something wrong with assignment it will throw error at compile time.

Conclusion: Arrays and generics are not quite friendly, so If possible we should try to use lists instead of arrays
