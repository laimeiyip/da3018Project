## 24-05-21

Main tasks:
1) Understand contents of the data frame
2) "Clean" the dataframe, e.g remove irrelevant columns, remove "useless" edges, remove commutative pairs
3) Create test sets

- Remove irrelevant columns from the entire data frame.
- Remove "useless" edges
- Remove commutative pairs
- write a one-liner unix command
### awk '{print $1, $2, $6, $7, $8, $10, $11, $12}' overlaps.m4 | 
### awk '{if ($4-$3!=$5 && $7-$6!=$8) print $0}' | 
### awk '{if ($1<$2) print $2" "$1; else print $1" "$2}' | sort -u > fulldata.txt
23:40 - 23:44. Took only 4 mins!


- Remove "useless" edges? This is the idea:

- Case 1: Contig A is contained in contig b. Then:
    overlapA.end - overlapA.start = contigA.length
    
- Case 2: Contig B is contained in contig A. Then:
    overlapB.end - overlapB.start = contigB.length

Pseudo code
overlapA.end = 4
overlapA.start = 3
contigA.length = 5
overlapB.end = 7
overlapB.start = 6
contigB.length = 8
for line in range(len(overlap_dataframe)):
  this.line = overlap_dataframe[line]
  if this.line[overlapA.end] - this.line[overlapA.start] == this.line[contigA.length] or 
     this.line[overlapB.end] - this.line[overlapB.start] == this.line[contigB.length]:
     remove.this.line
     
- Count the number of unique contigs
### cat fulldata.txt | tr '[:blank:]' '\n' | grep . | sort -u | wc -l
00:01 - 00:04. Took only 3 minutes!
Ans: 8084469

- Create random subsets of different sizes using Unix
### cat input.txt | awk 'BEGIN {srand()} !/^$/ { if (rand() <= .01) print $0}' > sample.txt
Created 5 random subsets of size (approx) 640000, 64000, 6400, 640

sample1.txt, sample2.txt, sample3.txt, sample4.txt

## 25-05-21

Removal of commutative pairs means that 
1) Sort column 1 and count the number of times a contig repeats itself
2) Sort column 2 and count the number of times a contig repeats itself.
For a given contig i, sum the numbers in (1) and (2) to get number of neighbours for contig i.

How to effectively assign unique ID numbers to contigs?
Since I have the list of unique contigs (sorted), starting from integer 1, I just run down the list and assign one after another an integer.
Very cheap to create a seq of integers with bash.
### seq 1 8084469 > IDs.txt
Write the unique contigs list to a txt file: uniq_contig.txt
paste this list of IDs as a new column into the unique contigs list.
Tried to do both operations by one-liner. Did not managed to.
### paste -d" " IDs.txt uniq_contig.txt > contigsWithID.txt

How to effectively match the unique ID numbers to contigs in the edge list?
- Construct a hash map of ID-contigs in java
- Separate the two columns in edge list into two lists of contigs
### awk '{print $1}' < fulldata.txt > left_neigh
### awk '{print $2}' < fulldata.txt >> right_neigh
These two operations are very fast to execute in bash. 1 minute in total.
- Match contigs lists with getValue() in ID hash map. 
- I get two lists of vertices that form edges between them. 
- I use them to create graphs using Java's HashMaps.





