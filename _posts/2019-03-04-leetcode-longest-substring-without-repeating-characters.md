---
title: LeetCode, Longest Substring Without Repeating Characters
categories:
 - Competitive Programming
tags:
 - leetcode, competitive programming
---

Quite simple but interesting problem. First I have been thinking how to solve it, I was considering to apply stacks to hold the substrings while I'm flying around with start and end indexes. Then I have realized that I don't actually have to show the longest substring. I just have to show the length of the longest substring. 


## Problem definition 
Given a string, find the length of the longest substring without repeating characters.[link](https://leetcode.com/problems/longest-substring-without-repeating-characters/)

Example 1:

```
Input: "abcabcbb"
Output: 3 
Explanation: The answer is "abc", with the length of 3. 
```

Example 2:

```
Input: "bbbbb"
Output: 1
Explanation: The answer is "b", with the length of 1.
```

Example 3:

```
Input: "pwwkew"
Output: 3
Explanation: The answer is "wke", with the length of 3. 
             Note that the answer must be a substring, "pwke" is a subsequence and not a substring.
```

## My Approach

I have  decided to keep the characters I've met so far in the Map and if repetition character met then I would shift the start index and remove the character (from the map) that is watched by the start index. So that endIndex-startIndex would be tracking the maximum length. First I have been using an i-index at the for loop for each iteration to see the current character, but then I just used endIndex insted to reduce the additional variable.

```java
class Solution {
    public int lengthOfLongestSubstring(String s) {
        Map<Character, Integer> table = new HashMap<>();
        
        int strLen = s.length();
        int startIndex = 0;
        int endIndex = 0;
        int maxLen = 0;
        for (endIndex = 0; endIndex < strLen;) {
            char ch = s.charAt(endIndex);
            if (!table.containsKey(ch)) {
                table.put(ch, 1);
                endIndex++;
                
                // get maxLen
                if (maxLen < endIndex - startIndex) {
                    maxLen = endIndex - startIndex;
                }
                
            } else {
                table.remove(s.charAt(startIndex));
                startIndex++;
            }
            //System.out.println("startIndex: " + startIndex+ " endIndex: " + endIndex + " maxLen: " + maxLen + " ch: " + ch);
        }
        
        return maxLen;
    }
}
```