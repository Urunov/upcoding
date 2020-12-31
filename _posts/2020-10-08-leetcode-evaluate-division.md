---
title: Evaluate Division (LeetCode)
categories:
 - leetcode
tags:
 - leetcode, java, algorithm
---

You are given an array of variable pairs `equations` and an array of real numbers `values`, where `equations[i] = [Ai, Bi]` and `values[i]` represent the equation `Ai / Bi = values[i]`. Each `Ai` or `Bi` is a string that represents a single variable.

You are also given some `queries`, where `queries[j] = [Cj, Dj]` represents the `jth` query where you must find the answer for `Cj / Dj = ?`.

Return the answers to *all queries*. If a single answer cannot be determined, return `-1.0`.

*Note*: The input is always valid. You may assume that evaluating the queries will not result in division by zero and that there is no contradiction.

```
Example 1:
Input: 
equations = [["a","b"],["b","c"]], values = [2.0,3.0], queries = [["a","c"],["b","a"],["a","e"],["a","a"],["x","x"]]
Output: 
[6.00000,0.50000,-1.00000,1.00000,-1.00000]
Explanation: 
Given: `a / b = 2.0, b / c = 3.0`
queries are: `a / c = ?, b / a = ?, a / e = ?, a / a = ?, x / x = ?`
return: `[6.0, 0.5, -1.0, 1.0, -1.0 ]`

Example 2:
Input: 
equations = [["a","b"],["b","c"],["bc","cd"]], values = [1.5,2.5,5.0], queries = [["a","c"],["c","b"],["bc","cd"],["cd","bc"]]
Output: 
[3.75000,0.40000,5.00000,0.20000]

Example 3:
Input: 
equations = [["a","b"]], values = [0.5], queries = [["a","b"],["b","a"],["a","c"],["x","y"]]
Output: 
[0.50000,2.00000,-1.00000,-1.00000]
```

**Constraints**:
- `1 <= equations.length <= 20`
- `equations[i].length == 2`
- `1 <= Ai.length, Bi.length <= 5`
- `values.length == equations.length`
- `0.0 < values[i] <= 20.0`
- `1 <= queries.length <= 20`
- `queries[i].length == 2`
- `1 <= Cj.length, Dj.length <= 5`
- `Ai, Bi, Cj, Dj` consist of lower case English letters and digits.

For original task refer to [leetcode](https://leetcode.com/problems/evaluate-division/)

###  Approach
- Divisions are represented in a Graph form. `A / B` means we have graph that starts from node A and goes to node B
- From mathematics we can derive reverse equations. Means `B / A` will be equal to `1 / ( A / B)`, and graph starts from node B and goes to node A
- By following two presumption above we can create a tree (look for implementation at createTree() method)
- After creating a tree we just have to traverse the tree by given parameters.
    - query contains starting and ending node names.
    - traversing the graph performed using DFS recursive algorithm (check traverse() method)
    - visitMap is used to track already visited nodes


**Time Complexity: O(V + E)**, where V is the number of varibles (like A,B,C ...) and E is the number of equations 

**Space Complexity: O(V)**, we need additional visitMap with the maximum size V

## Solution (Java)

```java
class Solution {
    
    class Tree {
        String name;
        List<Tree> child;
        List<Double> cost;
        
        Tree(String name) {
            this.name = name;
            this.child = new ArrayList<>();
            this.cost = new ArrayList<>();
        }
    }
    
    public double[] calcEquation(List<List<String>> equations, double[] values, List<List<String>> queries) {
        
        Map<String, Boolean> visitMap = new HashMap<>();
        Map<String, Tree> mainTree = createTree(equations, values, visitMap);
        
        double[] resPath = new double[queries.size()];
        
        for (int i = 0; i < queries.size(); i++) {
            resPath[i] = traverse(queries.get(i).get(0), queries.get(i).get(1), mainTree, visitMap);
        }
        return resPath;
    }
    
    private double traverse(String x, String y, Map<String, Tree> mainTree, Map<String, Boolean> visitMap) {
        if (!mainTree.containsKey(x) || !mainTree.containsKey(y)) {
            return -1.0;
        }
        
        if (x.equals(y)) return 1.0;
        
        Tree treeX = mainTree.get(x);
        
        visitMap.put(x, true);
        double pathCost = -1.0;
        for (int i = 0; i < treeX.child.size(); i++) {
            Tree nextX = treeX.child.get(i);
            if (visitMap.get(nextX.name) == false) {
                pathCost = traverse(nextX.name, y, mainTree, visitMap);
                if (pathCost != -1.0) {
                    pathCost *= treeX.cost.get(i);
                    break;
                }
            }
        }
        visitMap.put(x, false);
        return pathCost;
    }
    
    private Map<String, Tree> createTree(List<List<String>> equations, double[] values, Map<String, Boolean> visitMap) {
        Map<String, Tree> mainTree = new HashMap<>();
        for (int i = 0; i < equations.size(); i++) {
            List<String> equ = equations.get(i);
            double divRes = values[i];
            
            Tree treeA = mainTree.getOrDefault(equ.get(0), new Tree(equ.get(0)));
            Tree treeB = mainTree.getOrDefault(equ.get(1), new Tree(equ.get(1)));
            
            
            treeA.child.add(treeB);
            treeA.cost.add(divRes);
            
            treeB.child.add(treeA);
            treeB.cost.add(1.0 / divRes);
            
            mainTree.put(equ.get(0), treeA);
            mainTree.put(equ.get(1), treeB);
            
            visitMap.putIfAbsent(equ.get(0), false);
            visitMap.putIfAbsent(equ.get(1), false);
        }
        return mainTree;
    }
}
```