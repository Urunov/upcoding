---
title: Enum and Comparator Usage
categories:
 - refactoring
tags:
 - refactoring, enum, comparator, example
---

It is important to optimally use features of the given programming language. By other words squeezing the maximum out of the language, shows the proficiency and elegancy of the code. Most of the java developers are familiar with enum and comparator. These are very useful features of the java language.

Using enum and comparator together can result in a notably elegant code. In the below code we are defining the sorting order by creating Latest and First enum values. Comparator.comparingInt is the method that requires `ToIntFunction<? super T> keyExtractor`. CardResponse::getCardOrder is the getter method that returns integer value that represents the order of the card. 

Declaration of Enum:

```java
public enum CardSort {
    Latest("-card_order",
            Comparator.comparingInt(CardResponse::getCardOrder).reversed()),
    First("+card_order",
            Comparator.comparingInt(CardResponse::getCardOrder));

    private String ordering;
    private Comparator<CardResponse> cardResponseComparator;

    CardSort(String ordering,
                Comparator<CardResponse> cardResponseComparator) {
        this.ordering = ordering;
        this.cardResponseComparator = cardResponseComparator;
    }

    public String ordering() {
        return ordering;
    }

    public Comparator<CardResponse> cardResponseComparator() {
        return this.cardResponseComparator;
    }
}
```

Usage of Comparator enum within stream:

```java
CardSort cardSortFirst = CardSort.First;
CardSort cardSortLatest = CardSort.Latest;

if (isNaturalSortingOrder) {
    List<CardResponse> items = cardResponseList.stream()
						.sorted(cardSortFirst.cardResponseComparator())
            .collect(Collectors.toList());
    return items;
} else {
    List<CardResponse> items = cardResponseList.stream()
						.sorted(cardSortLatest.cardResponseComparator())
            .collect(Collectors.toList());
    return items;
}
```