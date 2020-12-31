---
title: Algorithms, Medians and Order Statistics
categories:
 - Algorithms
tags:
 - algorithms, cormen, optimization, medians, statistics
---

The *i* th **order statistic* of a set of *n* elements is the *i* th smallest element. Simply saying the *minimum* of a set of elements is the first order statistic (i = 1) and *maximum* is the *n*th order statistic (i = n). *Median* is the informally considered as a "halfway point" of the set.

## Minimum and Maximum

How many comparisons are necessary to determine the minimum of a set of *n* elements? The answer is straightforward: *n - 1* comparisons.

```
MINIMUM(A):
min = A[1]
for i = 2 to A.length
   if min > A[i]
      min = A[i]
return min
```

It is possible to find both the minimum and the maximum using at most 3*floor(n/2) comparisons. By processing elements in pairs, we compare pairs of elements from the input first with each other, and then we compare the smaller with the current minimum and the larger to the current maximum, at a cost of 3 comparisons for every 2 elements.

```C
#include<stdio.h>
 
/* structure is used to return two values from minMax() */
struct pair 
{
  int min;
  int max;
};  
 
struct pair getMinMax(int arr[], int n)
{
  struct pair minmax;     
  int i;  
 
  /* If array has even number of elements then 
    initialize the first two elements as minimum and 
    maximum */
  if (n%2 == 0)
  {         
    if (arr[0] > arr[1])     
    {
      minmax.max = arr[0];
      minmax.min = arr[1];
    }  
    else
    {
      minmax.min = arr[0];
      minmax.max = arr[1];
    }
    i = 2;  /* set the startung index for loop */
  }  
 
   /* If array has odd number of elements then 
    initialize the first element as minimum and 
    maximum */
  else
  {
    minmax.min = arr[0];
    minmax.max = arr[0];
    i = 1;  /* set the startung index for loop */
  }
   
  /* In the while loop, pick elements in pair and 
     compare the pair with max and min so far */   
  while (i < n-1)  
  {          
    if (arr[i] > arr[i+1])          
    {
      if(arr[i] > minmax.max)        
        minmax.max = arr[i];
      if(arr[i+1] < minmax.min)          
        minmax.min = arr[i+1];        
    } 
    else        
    {
      if (arr[i+1] > minmax.max)        
        minmax.max = arr[i+1];
      if (arr[i] < minmax.min)          
        minmax.min = arr[i];        
    }        
    i += 2; /* Increment the index by 2 as two 
               elements are processed in loop */
  }            
 
  return minmax;
}    
 
/* Driver program to test above function */
int main()
{
  int arr[] = {1000, 11, 445, 1, 330, 3000};
  int arr_size = 6;
  struct pair minmax = getMinMax (arr, arr_size);
  printf("nMinimum element is %d", minmax.min);
  printf("nMaximum element is %d", minmax.max);
  getchar();
}
```

*(The "C" implementation is taken from (geekforgeeks-method3)[https://www.geeksforgeeks.org/maximum-and-minimum-in-an-array/] )*

Analysis of the total of comparisons. If *n* is odd, then we perform 3*floor(n/2) comparisons. If *n* is even, we perform 1 initial comparison followed by 3*(n-2)/2 comparisons, for a total of 3*n/2 - 2. Thus either case, the total number of comparisons is at most 3*floor(n/2)


## Selection in expected linear time

The general selection problem appears more difficult than the simple problem of finding a minimum. The problem of selection could be stands as we have to find *i* th smallest object. The first algorithmic reduction way of the solution of this problem is  use *sorting* algorithm first and then just go for *i*th element of the sorted array. But then it will take at average *O(n lg n)*.

There is another solution for such type of problems and its called Randomized selection algorithm. RANDOMIZED-SELECT uses the procedure RANDOMIZED-PARTITION introduced in a quicksort algorithm. 

```
RANDOMIZED-SELECT(A, p, r, i)
if p == r
   return A[p]
q = RANDOMIZED-PARTITION(A, p, r)
k = q - p + 1
if i == k         // the pivot value is the answer
   return A[q]
else if i < k
   return RANDOMIZED-SELECT(A, p, q - 1, i)
else
  return  RANDOMIZED-SELECT(A, q + 1, r, i - k)
```



Randomized select form (coursera)[https://www.coursera.org/lecture/algorithms-divide-conquer/randomized-selection-algorithm-aqUNa]

Good example demonstration is given at (here)[https://www.youtube.com/watch?v=AHaaFVmAsvA]




