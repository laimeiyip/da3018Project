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
     
- Count the number of unique identifiers
### cat fulldata.txt | tr '[:blank:]' '\n' | grep . | sort -u | wc -l
00:01 - 00:04. Took only 3 minutes!
Ans: 8084469

- Create random subsets of different sizes using Unix
### cat input.txt | awk 'BEGIN {srand()} !/^$/ { if (rand() <= .01) print $0}' > sample.txt
Created 5 random subsets of size (approx) 640000, 64000, 6400, 640
sample1.txt
sample2.txt
sample3.txt
sample4.txt
