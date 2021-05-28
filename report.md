---
output:
  html_document: default
  pdf_document: default
---
## Report 

### Introduction

The purpose of this report is to document the work done to provide some statistical information about the set of DNA segments (also known as contigs) connected as a *undirected* graph where the segments (contigs from now on) are vertices. If two contigs have significant overlaps between them, they are connected to each other and in the graph, there exists an edge between them. For a given contig $i$, the number of contigs connected to it defines its degree. Further, a component is made up of a chain of contigs connected to one another. In this report, the reader will find 
 - degree distribution 
 - number of components
 - component size distribution.

### Exploratory work

There is a total of 64056772 edges in the data set. As written in the instructions sheet, columns are 3, 4, 5 and 9 are not relevant for this lab assignment. So I first remove them from the data set. Then I proceed to resolve the _containment_ issue by removing the contained edges. The way I remove contained edges is as follows:

```r
if {overlap.end - overlap.start == contig.length} 
  then contig.remove
```

Finally, since this is an undirected graph, I also remove _commutative_ edges. By commutative, I mean $A$ connected to $B$ is the same as $B$ connected to $A$.

Exploratory work is all done in the bash using unix commands. There are very efficient unix commands that can accomplish all the above tasks quickly, in particular the `awk` command. The one-liner I use takes only 4 minutes to do it. 

After completing the above "cleaning" work, there are 22167487 edges left in the data set. This will be the working data set from now on.


### Preparatory work

The contigs are constructed with a sequence of letters, special characters and integers. They are too long to work with as identifiers because they will take up too much memory. Using the hint in the lab instructions, I translate the contigs to integers by assigning an integer to each unique contig. First, I collect a list of unique contigs, then I assign an integer to each and every one of them. After assignment of ID numbers, I match the contigs in the edge set to the ID list. 

Again, all these tasks are done in the bash using unix commands. Unix has very powerful search-and-match commands and it has taken quite some research time to find out which one is the fastest. The `awk` command turns out to be the fastest and is able to complete the assignment of IDs to the full edge under 5 minutes. This is the command:
`awk 'NR=FNR {A[$2]=$1; next} {print A[$2], $1}' <ID file> <edge.set file> > <output file>`

The edge set is now ready to be used.

### Computation 

A Java programme is written to accomplish the three statistical tasks set out in the introduction. From now on, I use _left_ vertices to denote the contigs in column 1 of the edge set and _right_ vertices denote the contigs in column 2.

The edge set is stored as a hash map with the left vertex as key and an array of right vertices as value. I use hash map as its data structure suits a graph representation and the `HashMap` library in Java has a comprehensive set of methods that facilitate the computations that I want to do. While a binary tree and a heap can be used to represent the graph, I do not think they are most suited as resources are wasted to enforce the heap property on an edge set of a graph, which is not important for the tasks we set out to do. I find the hash map being the most intuitive and convenient data structure for graph representation.

The methods implemented in the Java programme have linear time complexities. To illustrate, I implement the depth-first-search traversal algorithm with recursive calls which helps to keep the time complexity of traversing a component to $O(\text{size of component})$. 

However, for a data set as large as 2GB, the Java programme runs into memory problems both at hash map construction process and component traversal. These problems are resolved when I increase the heap size to 7GB and stack to 1GB. I try my best to be thrifty with memory, for example, by keeping variables local instead of global as much as possible.

### Results

Figure 1 shows the degree distribution of the graph. It is clearly very skewed with a very long tal and most of the vertices having very few neighbours. In fact, close to 33% of the vertices have only one neighbour and around 20% of them have two neighbours. 

<ins>Figure 1: Degree distribution</ins>
<img src="da3018proj_degdist.png" alt="Degree Distribution" width="50%"/> 

There are 961874 components in the graph. Figure 2 shows the component size distribution which essentially has the same shape as the degree distribution. Take note that Figure 2 does not include the biggest component which has more than 2 million vertices in it. This is done so that the x-axis of the figure does not explode, which prevents the data points on the left hand side from being squashed together. The single enormous component is made up of more than 30% of the whole vertex set. Otherwise, more than 55% of the components are of size 2, i.e. they are made up of only two vertices, while approximately 16% of them are of size 3.

<ins>Figure 2: Component size distribution</ins>
<img src="da3018proj_compsizedistn.png" alt="Component Size Distribution" width="50%"/> 