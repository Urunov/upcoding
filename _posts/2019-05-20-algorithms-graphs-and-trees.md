---
title: Algorithms, Graphs and Trees
categories:
 - Algorithms
tags:
 - algorithms, graphs, trees, bfs, dfs, cycle, edge, vertex, vertice
---

Graphs are used to represent enourmous number of real-life applications. Basically graphs contain vertices(nodes) and edges(lines that are connecting nodes). Edges may have a direction, or may exist without directions. Following figure is a simple example of undirected graph with 5 vertices.

![undirectedGraph](/assets/images/graphs/undirectedExampleGraph.png)

Basically Graphs have two standard way of representations:
1. Adjacency Matrix
2. Adjacency List

# Adjacency Matrix

Adjacency Matrix is a 2D array of size V x V where V is the number of vertices. If the value of the cell equals to 1, it indicates that there is an edge from vertex i to vertex j. If the graph is undirected then adjacency matrix representation will be symmetric. So only diagonal cut of the matrix can be used. The following is how the representation of above graph looks like in adjacency matrix.

![adjacencyMatrix](/assets/images/graphs/adjacencyMatrix.png)

# Adjacency List

An array of lists is used where the size of the array is equal to the number of vertices. Each entry of array represents the list of vertices adjacent to the i-th vertex. The following is how the representation of above graph looks like in adjacency matrix.

![adjacencyList](/assets/images/graphs/adjacencyList.png)


Both representations of the graph may contain information about weight of edges. Matrix representation may have different number than 1, as a weight value. Adjancency list representation may have additional weight value inside the linked list. Currently it contains only one value which represents id-number of the vertice.

# When to use What kind of representations ?

Adjacency List representation provides a compact way to represent *sparse* graphs - those for which |*E*| (number of edges) is much less than |*V*|^2 (much less than number of vertices).

Adjancency Matrix representation is prefered when the graph is dense - |*E*| is close to |*V*|^2 - or when it is required to quickly tell if there is an edge connecting two given vertices.


# What is the tree then ?

Tree is a special case of the graph where two vertices may have only one path without loops and self-loops. 
- Tree contains exactly one root node and every child have only one parent. 
- Tree is traversed in Pre-Order, In-Order and Post-Order
- Tree come in the category of DAG: Directed Acyclic Graphs is a kind of directed graphy that have no cycles. [Topological-Sort](https://rusyasoft.github.io/algorithms/2018/08/29/graphs-TopologicalSort/)
- Tree always has n-1 edges. (n is the number of vertices)
- Tree has a hierarchical model

Example of the tree:

![exampleOfTree](/assets/images/graphs/binary-tree-to-DLL.png)

For a detail comparison with graph refert to [here](https://freefeast.info/difference-between/difference-between-trees-and-graphs-trees-vs-graphs/)

# Implementation Example of Adjacency List

Since the representation of adjacency list is mostly used in a real-life applications here we will demonstrated the Java code example for it:

```java
import java.util.LinkedList;

public class GFG
{
    // this is the actual Adjacency List representation class
    static class Graph
    {
        int V;
        LinkedList<Integer> adjListArray[];

        // constructor
        Graph(int V)
        {
            this.V = V;
              
            // define the size of array as
            // number of vertices
            adjListArray = new LinkedList[V];
            
            // Create a new list for each vertex
            // such that adjacent nodes can be stored
            for(int i = 0; i < V ; i++){
                adjListArray[i] = new LinkedList<>();
            }
        }
    }
 
    // Adds an edge to an undirected graph
    static void addEdge(Graph graph, int src, int dest) 
    { 
        // Add an edge from src to dest.  
        graph.adjListArray[src].add(dest); 
          
        // Since graph is undirected, add an edge from dest to src also
        // NOTE: if the graph is directed then following line should be removed
        graph.adjListArray[dest].add(src); 
    } 
       
    // A utility function to print the adjacency list  
    // representation of graph 
    static void printGraph(Graph graph) 
    {        
        for(int v = 0; v < graph.V; v++) 
        { 
            System.out.println("Adjacency list of vertex "+ v); 
            System.out.print("head"); 
            for(Integer pCrawl: graph.adjListArray[v]){ 
                System.out.print(" -> "+pCrawl); 
            } 
            System.out.println("\n"); 
        } 
    } 
       
    // Driver program to test above functions 
    public static void main(String args[]) 
    { 
        // create the graph that is given in above figure 
        int V = 5; 
        Graph graph = new Graph(V); 
        addEdge(graph, 0, 1); 
        addEdge(graph, 0, 4); 
        addEdge(graph, 1, 2); 
        addEdge(graph, 1, 3); 
        addEdge(graph, 1, 4); 
        addEdge(graph, 2, 3); 
        addEdge(graph, 3, 4); 
       
        // print the adjacency list representation of  
        // the above graph 
        printGraph(graph); 
    } 
} 
```
