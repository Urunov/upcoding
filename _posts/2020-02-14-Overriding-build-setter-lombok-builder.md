---
title: Override build and setter in Lombok's builder
categories:
 - java, lombok
tags:
 - java, lombok, clean-code
---

Sometimes we may need to override build functionality of lombok builder. It could be helpful to avoid additional call of some functionality after calling build(). Here is the example of usage:

```java
@Builder
@Getter
public class Person {
    private String name;
    private String address;

    /**
     * Uses custom builder class
     */
    public static PersonBuilder builder() {
        return new CustomPersonBuilder();
    }

    /**
     * Cusotm  builder class
     */
    private static class CustomPersonBuilder extends PersonBuilder {
        @Override
        public Person build() {
            // Validates required fields
            Validate.notBlank(super.name, "Persons NAME cannot be null or empty!");
            return super.build();
        }
    }
}
```

More advanced example with accessing to class fields can be seen below: 


```java
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Person {
    private String name;
    private String address;

    public static PersonBuilder builder() {
        return new PersonBuilder(){
            @Override
            public Person build() {
                prebuild();
                return super.build();
            }
        };
    }

    public static class PersonBuilder  {
        void prebuild(){
						// additional processing with class fields can be performed here
						// this.name, this.address
        }
    }
}
```

CAUTIONS: while accessing to the class fields must check their existence, because it may be that caller omit their initialization while using builder pattern.

Here another example for overriding field initialization

```java
import lombok.Builder;
import lombok.ToString;

import java.util.Map;

@ToString
@Builder
public class Person<R extends Animal> {
    private String name;
    private String address;
    private R being;


    public static class PersonBuilder<R extends Animal> {
        public PersonBuilder being(R being) {
            this.being = being;
            System.out.println("Here could be your code!");
            return this;
        }
    }
}
```

When the caller tries to initialize "being"  with builder pattern being(R being) is triggered

```java
Animal goodOne = new Animal();
Person person = Person.builder().name("rustam").address("Ragnarok").being(goodOne);
```

## References

- [https://github.com/rzwitserloot/lombok/issues/1144](https://github.com/rzwitserloot/lombok/issues/1144)
- [https://projectlombok.org/features/Builder](https://projectlombok.org/features/Builder)
- [https://stackoverflow.com/questions/42379899/use-custom-setter-in-lomboks-builder](https://stackoverflow.com/questions/42379899/use-custom-setter-in-lomboks-builder)
