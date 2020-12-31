---
title: Componsition over Inheritance
categories:
 - java
tags:
 - java, effective java, book, inheritance, composition
---

Inheritance is a powerful feature of the OOP that is used to achieve code reusability. But it is not always the best tool for the job. It is safe to use inheriance within a same package, and the reason is because super and sub classes are under the control of the same team (Or even a same developer). The issues are started when the developer of the super class and sub classes are different teams. It may occur that super class has not been designed to being inherited. So what kind of problems may occur when we extend the class which is not devoted to be inherited. 

## Issues of un-intended inheritance

First of all subclass starts having dependancy on the implementation details of its superclass members and methods. The superclass's implementation may change from release to release, and if it does, the subclass may break, even though its code has not been touched. Developer of the subclass must be always aware of that and everytime when the superclass is upgraded it must be checked for new changes. Tons of test codes must run to check the confidence. At some point it will become almost impossible to trace all the inherited classes. 

As a proove lets give an example from the Effective Java book, where some juggling with HashSet class were performed. Simply we want to track the number of added elements to the set. 

```java
public class InstrumentedHashSet<E> extends HashSet<E> {
    // The number of attempted element insertions
    private int addCount = 0;

    public InstrumentedHashSet() {        
    }

    public InstrumentedHashSet(int initCap, float loadFactor) {
        super(initCap, loadFactor);
    }

    @Override public boolean add(E e) {
        addCount++;
        return super.add(e);
    }

    @Override public boolean addAll(Collection<? extends E> c) {
        addCount += c.size();
        return super.addAll(c);
    }

    public int getAddCount() {
        return addCount;
    }
}
```

If the List with three elements are added then expected result is not received. The following code example:

```java
InstrumentedHashSet<String> s = new InstrumentedHashSet<>();
s.addAll(List.of("Rustam", "Kim", "Park"));
```

After running the above code if the getAddCount() is called the result will be 6 not 3. Because HashSet's addAll method is implemented on top of its add method, means original addAll itself not supposed to increase the counter. There are multiple solutions to avoid this issue after we find out the problem:
1. remove counter incrasement at the addAll() method
2. do not call super.addAll() and just perform by iterating the given argument and performing increasement.

But it should be remembered these solutions might be temporary, until the new version of the parent class is released with new kind of surprise features.


## Composition

In order to avoid all of the problems described above, instead of extending an existing class, give your new class a private field that references an instance of the existing class. This is called composition way of approach. Each instance method in the new class invokes the corresponding method on the contained instance of the existing class and returns the results. It is also known as forwarding methods. The resulting class will be rock solid, with no dependencies on the implementation details of the existing class. Even addition of the new methods in the new version will have no impact on the new class. Above example can be implemented as follows:

```java
// Wrapper class - uses composition in place of inheritance
public class InstrumentedSet<E> extends ForwardingSet<E> {
    private int addCount = 0;
    public InstrumentedSet(Set<E> s) {
        super(s);
    }
    @Override public boolean add(E e) {
        addCount++;
        return super.add(e);
    }
    @Override public boolean addAll(Collection<? extends E> c) {
        addCount += c.size();
        return super.addAll(c);
    }
    public int getAddCount() {
        return addCount;
    }
}

// Reusable forwarding class
public class ForwardingSet<E> implements Set<E> {
    private final Set<E> s;
    public ForwardingSet(Set<E> s) {this.s = s;}

    public void clear()                 { s.clear();}
    public boolean contains(Object o)   { return s.contains(o); }
    public boolean isEmpty()            { return s.isEmpty(); }
    public int size()                   { return s.size(); }
    public Iterator<E> iterator()       { return s.iterator(); }
    public boolean add(E e)             { return s.add(e); }
    public boolean remove(Object o)     { return s.remove(o); }
    public boolean containsAll(Collection<?> c)
                                        { return s.containsAll(c); }
    public boolean addAll(Collection<?> c)
                                        { return s.addAll(c); }
    public boolean removeAll(Collection<?> c)
                                        { return s.removeAll(c); }
    public boolean retainAll(Collection<?> c)
                                        { return s.retainAll(c); }
    public Object[] toArray()           { return s.toArray(); }
    public <T> T[] toArray(T[] a)       { return s.toArray(a); }
    
    @Override public boolean equals(Object o) {return s.equals(o); }
    @Override public int hashCode()     { return s.hashCode(); }
    @Override public String toString()  { return s.toString(); }
}
```

The design of the InstrumentedSet class is enabled by the existence of the Set interface, which captures the functionality of the HashSet class.

Inheritance is appropriate only in circumstances where the subclass really is a subtype of the superclass. To detect this necessity the following questions can be asked:
- Is every B really an A? If this question cannot be truthfully answered yes then B should not extend A. It is better to B should contain a private instance of A and expose a different API: A is not an essential part of B, merely a detail of its implementation.
- Doesthe class that you are trying to extend from has any flaws in its API? if yes, are you ok to propogate those flaws into your class's API?

Composition is less affected even if the super class has a flaw. It also can be easily fixed, exchanged or removed as a composition member. 

## If No Option but Inherit

If it is decided that we must use inheritance then it should be done a right way. The right way when the class is designed and documented for inheritance usage. The class must document precisely the effects of overriding any method. In other words, the class must document its self-use of overridable methods. Documentation must contain sections like *Implementation Requirements*, *Override Side effects* and *Exceptions*. 

The best way to decide what protected members to expose when you design a class for inheritance is to think hard and take best guesses. And then test it by writing subclasses. Experience shows that three subclasses are usually sufficient to test an extendable class. One or more of these subclasses should be written by someone other than who implemented superclass.

Constructor must not invoke overridable methods in anyway. The superclass constructor runs before the subclass constructor, so the overriding method in the subclass will get invoked before the subclass constructor has run.

```java
public class Super {
    // Broken - constructor invokes an overridable method
    public Super() {
        overrideMe();
    }
    public void overrideMe() {}    
}
```

Now lets see the subclass that overrides the *overrideMe* method, which is erroneously invoked by Super's sole constructor:

```java
public final class Sub extends Super {
    // Blank final, set by constructor
    private final Instant instant;
    Sub() {
        instant = Instant.now();
    }

    // Overriding method invoked by superclass constructor
    @Override public void overrideMe() {
        System.out.println(instant);
    }

    public static void main(String[] args) {
        Sub sub = new Sub();
        sub.overrideMe();
    }
}
```

The system will print out null, because overrideMe is invoked by the Super constructor before the Sub constructor has a chance to initialize the instant field. In many usages this kind of code would throw a *NullPointerException* but here System.out.println tolerates null parameters.

As the best conclusion (states from book Effective Java) to this problem is to prohibit subclassing in classes that are not designed and documented to be safely subclassed.

