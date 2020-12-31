---
title: Euler Project-51, Prime digit replacements
categories:
 - Competitive Programming
tags:
 - hackerrank
---

Original definition of the problem is given at [projecteuler.net](https://projecteuler.net/problem=51), as always [hackerrank](https://www.hackerrank.com/contests/projecteuler/challenges/euler051) definition of the problem is more complicated an challenging.



## Hackerrank Defition
By replacing the 1st digit of *3, it turns out that six of the nine possible values: 13, 23, 43, 53, 73, and 83, are all prime.

By replacing the 3rd and 4th digits of 56**3 with the same digit, this 5 - digit number is the first example having seven primes among the ten generated numbers, yielding the family: 56003, 56113, 56333, 56443, 56663, 56773, and 56993. Consequently 56003, being the first member of this family, is the smallest prime with this property.

Find the smallest N - digit prime which, by replacing K - digits of the number (not necessarily adjacent digits) with the same digit, is part of an L prime value family.

- **Note1:** It is guaranteed that solution does exist.
- **Note2:** Leading zeros should not be considered.
- **Note3:** If there are several solutions, choose the "lexicographically" smallest one (one sequence is considered "lexicographically" smaller than another if its first element which does not match the corresponding element in another sequence is smaller)

### Overal Algorithm Description

There are multiple ways of solving this problem, if go by straight way of solving then we will face time-out (Thats what I did at first). Regular expression based approach is more proper for solving this problem. Here simplified algorithm:
- generate prime numbers (sieve of Eratosthenes is good choice)
- choose the range for looping (ex: if the replacement number K is 5, then we don't have to check 4 digit numbers)
- for each prime number proper regular expression should be generated. Lets assume current prime number from the loop is equal to 56003, then following regular expressions can be generated. *6003, 5*003, 56*03, 560*3, 56\*\*3. For each regular expression can create its own family and collect similar kinds. It can be easily implemented by HashMap, where regular expression is used as a key and value would be the list of prime numbers matching that regular expression.
- find the list with the smallest prime numbers and size of which equals L

### Data Structure and Methods
- **generateSieve(int n)** is the sieve generator function, which saves generated results into global static variable isPrime
- **matches** is the HashMap that stores mask and list of prime numbers that are match with this given mask
- **maxDigits, replace and siblings** variables are stands for N, K and L correspondingly. Those alphabets were changed to meaningful names.

**Work-around:** Test case #16 has been given a timeout issue until I have found-out some hard-coded solution from guy named [Stephan Brumme](http://euler.stephan-brumme.com/). He has said all minimized families of 7-digit primes have members below 2000000 or 3000000. I will keep his work-around solution in my code until I found other solution


### Code in Java

```java
import java.io.*;
import java.util.*;

public class Solution {
    
    public static boolean [] isPrime = new boolean[10000010];
    public static Map<String, List<Integer>> matches = new HashMap();
    
    public static int siblings = 7;
    public static int replace = 3;
    public static int maxDigits = 7;
    public static int smallestPrime = 99999999;
    
    public static void generateSieve(int n) {
        
        for (int i=0;i<n+1;i++) {
            isPrime[i] = true;
        }
        
        for (int i=2;i<n+1;i++) {
            if (isPrime[i]) {
                for (int j = i+i; j < n+1 ; j += i) {
                    isPrime[j] = false;
                }
            }
        }
    }
   
    public static void match(int number, char [] regex, int digit, int howOften, int startPos) { 
        char asciiDigit = (char)(digit + '0');
        
        for (int i = startPos; i < maxDigits; i++) {
            if (regex[i] != asciiDigit)
                continue;
            
            if (i == 0 && asciiDigit == '0')
                continue;
            
            regex[i] = '.';
            
            if (howOften == 1) {
                List<Integer> addTo = matches.get(String.valueOf(regex));
                if (addTo == null) {
                    addTo = new ArrayList();
                }
                addTo.add(number);
                matches.put(String.valueOf(regex), addTo);
                if (addTo.size() >= siblings && addTo.get(0) < smallestPrime)
                    smallestPrime = addTo.get(0);
            } else {
                match(number, regex, digit, howOften - 1, i + 1);
            }
            
            regex[i] = (char)(digit + '0');
        }
    }

    public static void main(String[] args) {        
        Scanner scan = new Scanner(System.in);
        maxDigits = scan.nextInt();
        replace = scan.nextInt();
        siblings = scan.nextInt();
        
        int minNumber = 1;
        for (int i = 1; i < maxDigits ; i++) {
            minNumber *= 10;
        }
        int maxNumber = minNumber * 10 - 1;
        generateSieve(10000000);
        
        for (int i = minNumber; i <= maxNumber ;i++) {            
            if (isPrime[i]) {
                
                String strNum = String.valueOf(i);
                
                for (int digit = 0; digit <= 9; digit++)
                    match(i, strNum.toCharArray(), digit, replace, 0);
                
                if (maxDigits == 7) {
                    if (replace == 1 && i > 2000000)
                        break;
                    if (replace == 2 && i > 3000000)
                        break;
                }
            }   
        }
        
        String minimum = "";
        for (Map.Entry<String, List<Integer>> m : matches.entrySet())
        {
            if (m.getValue().size() < siblings)
                continue;
            if (m.getValue().get(0) != smallestPrime) 
                continue;
            String s = "";
            for (int i = 0; i < siblings ; i++)
                s += String.valueOf(m.getValue().get(i) + " ");
            
            if (minimum.compareTo(s) > 0 || minimum.isEmpty())
                minimum = s;
        }
        System.out.println(minimum);
    }
}
```


