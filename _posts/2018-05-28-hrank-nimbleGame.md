---
title: Hackerrank, Nimble Game
categories:
 - Competitive Programming
tags:
 - hackerrank
---

Original definition of the problem is given at [hackerrank](https://www.hackerrank.com/challenges/nimble-game-1/problem). In order to avoid copyright related issues, I would like to not keep the definition of the problem in here.

# Solution Description
If the cell contains even number of stones then we should ignore those cells in our calculations. Because they would have no effect on the result, every move can be repeated by the opposite player. In our case the move started by First player will be repeated by Second player. Eventually First user looses. Cells that contains odd numbers have an impact to the result of the game. If we filter out the cells with odd numbered stones, then we can start converting this game to basic Nim game. If we convert then the cell index (position) becomes a number of stones in the basic Nim game. Then just apply nim game solution, which is XOR ing all values and if 0 then Second player wins, otherwise First player.

![NoImage](/assets/images/NimbleGameToNimGame.png)


# Solution Source Code

```java
static String nimbleGame(int[] s) {
    int sum = 0;
    for (int i = 0; i < s.length; i++) {
        //sum += s[i];
        if (s[i] % 2 == 1) {
            sum ^= i;            
        }
    }

    if (sum == 0) {
        return "Second";
    } else {
        return "First";
    }
}
```

# Test Case (Purchased by Hackos :))
Input:
```
1
100
383 886 777 915 793 335 386 492 649 421 362 27 690 59 763 926 540 426 172 736 211 368 567 429 782 530 862 123 67 135 929 802 22 58 69 167 393 456 11 42 229 373 421 919 784 537 198 324 315 370 413 526 91 980 956 873 862 170 996 281 305 925 84 327 336 505 846 729 313 857 124 895 582 545 814 367 434 364 43 750 87 808 276 178 788 584 403 651 754 399 932 60 676 368 739 12 226 586 94 539
```
Output:
```
First
```

More TestCases:
[TestCase5_Input](https://hr-testcases-us-east-1.s3.amazonaws.com/21906/input05.txt?AWSAccessKeyId=AKIAJ4WZFDFQTZRGO3QA&Expires=1527475408&Signature=zfFl7i32nm2fo4eLYYrLTpG30oo%3D&response-content-type=text%2Fplain), 
[TestCase5_Output](https://hr-testcases-us-east-1.s3.amazonaws.com/21906/output05.txt?AWSAccessKeyId=AKIAJ4WZFDFQTZRGO3QA&Expires=1527475381&Signature=R1wjEZhw81uJ%2FSYhvvUOERb8Exw%3D&response-content-type=text%2Fplain)
