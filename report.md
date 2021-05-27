## Report 

### Introduction

The purpose of this report is to document the work done to provide some statistical information about the set of DNA segments (also known as contigs) connected as a *undirected* graph where the segments (contigs from now on) are vertices. If two contigs have significant overlaps between them, they are connected to each other and in the graph, there exists an edge between them. For a given contig $i$, the number of contigs connected to it defines its degree. Further, a component is made up of a chain of contigs connected to one another. In this report, the reader will find 
 - degree distribution 
 - number of components
 - component size distribution.

### Exploratory work

There is a total of 64056772 edges in the data set. As written in the instructions sheet, columns are 3, 4, 5 and 9 are not relevant for this lab assignment. So I first remove them from the data set. Then I proceed to resolve the _containment_ issue by removing the contained edges. The way I remove contained edges is as follows:

```r
if {end of overlap - start of overlap == length.contig} 
  then remove
```

Finally, since this is an undirected graph, I also remove _commutative_ edges. By commutative, I mean $A$ connected to $B$ is the same as $B$ connected to $A$.

Exploratory work is all done in the bash using unix commands. There are very efficient unix commands that can accomplish all the above tasks quickly, in particular the `awk` command. The one-liner I use takes only 4 minutes to do it. 

After completing the above "cleaning" work, there are 22167487 edges left in the data set. This will be the working data set from now on.


### Preparatory work

The contigs are constructed with a sequence of letters, special characters and integers. They are too long to work with as identifiers because they will take up too much memory. Using the hint in the lab instructions, I translate the contigs to integers by assigning an integer to each unique contig. First, I collect a list of unique contigs, then I assign an integer to each and every one of them. After assignment of ID numbers, I match the contigs in the edge set to the ID list. 

Again, all these tasks are done in the bash using unix commands. Although unix has very powerful search-and-match commands, it still take a very long time to assign ID numbers to the contigs in the edge set. The `awk` command is very fast but unfortunately is not suited for this task as it returns only the subset of ID numbers whose contigs intersect with those in the edge set. In the edge set, there are surely cases where a given contig is connected to several other contigs. When `awk` is used, it returns one hit of this contig instead of the number of times it appears in the edge set. Hence, I try out two methods:

1. `grep -wf <edge set> <ID list>`
This method `grep` the contig as a word in the ID list and returns the matching line in the ID list.

2. `while` loop combined with `grep`
The method reads the edge set file line by line and grep each line with the ID list. It returns the matching line in the ID list.

The edge set is now ready to be used.

### Computation 

A Java programme is written to accomplish the three statistical tasks set out in the introduction. From now on, I use _left_ vertices to denote the contigs in column 1 of the edge set and _right_ vertices denote the contigs in column 2.

The edge set is stored as a hash map with the left vertex as key and an array of right vertices as value. I use hash map as its data structure suits a graph representation and the `HashMap` library in Java has a comprehensive set of methods that facilitate the computations that I want to do. While a binary tree and a heap can be used to represent the graph, I do not think they are most suited as resources are wasted to enforce the heap property on an edge set of a graph, which is not important for the tasks we set out to do. I find the hash map being the most intuitive and convenient data structure for graph representation.

The methods implemented in the Java programme have linear time complexities. To illustrate, I implement the depth-first-search traversal algorithm with recursive calls which helps to keep the time complexity of traversing a component to $O(\text{size of component})$. 

### Results

