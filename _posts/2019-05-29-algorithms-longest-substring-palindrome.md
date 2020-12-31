---
title: Longest Palindromic Substring
categories:
 - Algorithms, Leetcode
tags:
 - algorithms, leetcode, competitive programming, dynamic programming, dp, palindrome, substring
---

This is one of the medium level problem regarding to palindromes, where we have to find longest substring palindrome of the given string.[leetcode](https://leetcode.com/problems/longest-palindromic-substring/)

There are multiple uproaches to solve this problem and popular Dynamic Programming approach takes O(n^2) while brute force takes O(n^3)


# Dynamic Programming Approach

We can use palindrome checker dp pattern:

```python
def isPalindrome(si, ei, a):
    if (si == ei):  # if we stand on same character it is palindrome
        return true
    if (si == ei-1):    #   if elements are neighbour
        return a[si] == a[ei]   # and neighbour elements are equal, then its palindrome too
    
    return a[si] == a[ei] and isPalindrome(si + 1, ei - 1)  # if two elements are equal they may be palindrome only if the substring betwee them is palindrome
```

My personal solution is not an elegant code, which i have reached after multiple optimizations. I was keep getting "Time Limit Exceed", until i have stoped using a hash-map and reduced useless substring operations.

```java
class Solution {
    
    public boolean[][] palindromeTable;
    
    public int maxSize;
    public String maxString;
    
    public boolean trueReturn(int si, int ei, String inputS) {
        if (ei-si >= maxSize) {
            maxSize = ei-si+1;
            maxString = inputS.substring(si, ei+1);;
        }
        
        palindromeTable[si][ei] = true;
        return true;        
    }
    
    public boolean isPalindrome(int si, int ei, String inputS) {        
        if (palindromeTable[si][ei]) {
            return true;
        }
            
        if (si == ei)
            return trueReturn(si, ei, inputS);
        
        if (si == ei-1)
            return inputS.charAt(si) == inputS.charAt(ei) ? trueReturn(si, ei, inputS) : false;
        
        if (inputS.charAt(si) == inputS.charAt(ei) && isPalindrome(si+1, ei-1, inputS))
            return trueReturn(si, ei, inputS);
        else 
            return false;
            
    }
    
    public String longestPalindrome(String s) {
        
        // edge case
        if (s.length() <= 1)
            return s;
        
        int slength = s.length();
        
        palindromeTable = new boolean[slength][slength];
            
        for (int i = 0; i < slength-1;i++)
            for (int j = i; j < slength;j++)
                isPalindrome(i,j,s);
        
        return maxString;
    }
}
```

Basically  I have done wrapping the true operation into the Maximum value tracker and of course memoization for not redoing already performed tasks. Memoization performed based on indexes, so eventually we are memoizing not substring itself but their location(result of dp operations). It is interesting to note that before my this approach i have tried the sub-string based memoization, but substring operations took too long so i got "Time Limit Exceed". But I think in a real cases stil the sub-string based memoization would be better approach. Because we may have repeated substrings on a different locations. 
