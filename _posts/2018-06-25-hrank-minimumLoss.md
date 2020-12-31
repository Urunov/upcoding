---
title: Hackerrank, Minimum Loss
categories:
 - Competitive Programming
tags:
 - hackerrank
---
Here another weird problem given by hackerrank, though It was fun to solve this problem as well. This problem is good example of importance of understanding the given task. Sometimes it may have a straight logic from the definition of the task. Why is the Lauren wants to sell the houses and make a loss, not profit. Isn't the profit is main purpose of any business, so guys be careful when reading the definition of the task.

**Problem**

Lauren has a chart of distinct projected prices for a house over the next several years. She must buy the house in one year and sell it in another, and she must do so at a loss. She wants to minimize her financial loss.
For example, the house is valued at *price=[20, 15, 8, 2, 12]* over the next *n = 5* years. She can purchase the home in any year, but she must resell the house at a loss in one of the following years. Her minimum loss would be incurred by purchasing in year 2 at *price[1]=15* and reselling in year 5 at *price[4]=12*.
Find and print the minimum amount of money Lauren must lose if she buys the house and resells it within the next *n* years.
Note: It's guaranteed that a valid answer exists.

The original  task is given at [hrank](https://www.hackerrank.com/challenges/minimum-loss/problem). 

**Input Format**

The first line contains an integer *n*, the number of years of house data. 
The second line contains *n* space-separated long integers describing each *price[i]*.

**Constraints**

- 2 <= n <= 2 x 10^5

- 1 <= price[i] <= 10^16

- All the prices are distinct

- A valid answer exists

**Subtasks**

- 2 <= n <= 1000 for 50% of the maximum score.

**Output Format**

Print a single integer denoting the minimum amount of money Lauren must lose if she buys and resells the house within the next *n* years.

**Sample Input 0**

```
3
5 10 3
```

**Sample Output 0**

```
2
```


**Explanation**
Lauren buys the house in year 1 at *price[0]=5* and sells it in year *3* at *price[2]=3* for a minimal loss of 5 - 3 = 2.

**Sample Input 1**

```
5
20 7 8 2 5
```

**Sample Output 1**

```
2
```

**Explanation 1**
Lauren buys the house in year 2 at *price[1]=7* and sells it in year *5* at *price[4]=5* for a minimal loss of 7 - 5 = 2.


**Solution: Java**

```java
import java.io.*;
import java.math.*;
import java.security.*;
import java.text.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.regex.*;

public class Solution {

    // Complete the minimumLoss function below.
    static int minimumLoss(long[] price) {
        Map<Long,Long> posMap = new HashMap();
        int n = price.length;
        for (int i = 0; i < n; i++) {
            posMap.put(price[i], (long)i);
        }
        
        long minDiff = Long.MAX_VALUE;
        Arrays.sort(price);
        
        for (int i = 1; i < n; i++) {
            if ( posMap.get(price[i]) < posMap.get(price[i-1]) ) {
                if (price[i] - price[i-1] < minDiff && price[i] - price[i-1] > 0) {
                    minDiff = price[i] - price[i-1];
                }
            }
            
        }
        
        return (int)minDiff;

    }

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));

        int n = scanner.nextInt();
        scanner.skip("(\r\n|[\n\r\u2028\u2029\u0085])?");

        long[] price = new long[n];

        String[] priceItems = scanner.nextLine().split(" ");
        scanner.skip("(\r\n|[\n\r\u2028\u2029\u0085])?");

        for (int i = 0; i < n; i++) {
            long priceItem = Long.parseLong(priceItems[i]);
            price[i] = priceItem;
        }

        int result = minimumLoss(price);

        bufferedWriter.write(String.valueOf(result));
        bufferedWriter.newLine();

        bufferedWriter.close();

        scanner.close();
    }
}
```
