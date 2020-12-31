---
title: Search In Rotated Sorted Array
categories:
 - algorithms, leetcode
tags:
 - algorithms, leetcode, modified binary-search
---

Another good example of modifiying classical algorithm in order to reach the goal. In this example we have twisted task where binary-search algorithm should be modified with additional edge case checks. [leetcode](https://leetcode.com/problems/search-in-rotated-sorted-array/)

# Given

Suppose an array sorted in ascending order is rotated at some pivot unknown to you beforehand.

(i.e., [0,1,2,4,5,6,7] might become [4,5,6,7,0,1,2]).

You are given a target value to search. If found in the array return its index, otherwise return -1.

You may assume no duplicate exists in the array.

Your algorithm's runtime complexity must be in the order of O(log n).



Example 1:

```
Input: nums = [4,5,6,7,0,1,2], target = 0
Output: 4
```

Example 2:

```
Input: nums = [4,5,6,7,0,1,2], target = 3
Output: -1
```

# Solution 

```java
class Solution {
    public int search(int[] nums, int target) {
        if (nums.length == 0)
            return -1;
        return searchArr(nums, target, 0, nums.length-1);
    }
    
    public int searchArr(int[] nums, int target, int startIndex, int endIndex) {
        
        if (startIndex == endIndex) {
            if (target == nums[startIndex])
                return startIndex;
            
            return -1;
        }
        
        int halfIndex = (endIndex + startIndex) / 2;
        
        if (nums[halfIndex] == target)
            return halfIndex;
        
        if (target < nums[halfIndex]) { // left-side
            if (target < nums[startIndex] && nums[startIndex] <= nums[halfIndex]) { // go-for right side
                return searchArr(nums, target, halfIndex+1, endIndex);
            } else { // go for left
                return searchArr(nums, target, startIndex, halfIndex);
            }
        } else { // right-side
            if (target > nums[endIndex] && nums[endIndex] >= nums[halfIndex]) { // go-for left side                
                return searchArr(nums, target, startIndex, halfIndex);                
            } else { // go-for right
                return searchArr(nums, target, halfIndex+1, endIndex);
            }
        }
    }    
}
```
