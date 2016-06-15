# Einstein's five-houses riddle / Einsteins Rätsel - CSP
- HAW Hamburg, SS2016

This riddle had to be solved as an exercise for IS (intelligent systems / Intelligente Systeme) by using the 
full look ahead algorithm. 

The algorithms in my sheets were taken from: http://ktiml.mff.cuni.cz/~bartak/constraints/index.html

## Priority
For this purpose I used the programming language Kotlin and this is also the first project I got use Kotlin. 
Generelly I tried to use some Kotlin features that I don't have in Java to solve my problems with few lines
of code to see how it performs. I didn't intent to check for errors in this project, because at first I
just wanted to get a glance of Kotlin.

## Structure
I have made 3 classes: Vertex, Edge and Graph. A Vertex corresponds to a variable in a csp (constraint solving problem)
and has a name, a domain (some values it can take) and any amount of unary constraints. Two vertices and a binary constraint
form an edge and and any amount of vertices and edges form the graph.

The Start.kt file is used to create the graph and write the output. In this case I have 26 constraints to represent
the hints given from the riddle and 100 unequal constraints to ensure that their is no one having two
nationalities or something similar.

I chose the nummerical representation of the variables instead of having the strings (like "red", "blue", ...) 
in the domains, because it's much easier to model the constraints with numbers.

The solving itself consists of four steps:

1) nc() for node consistency (takes care of unary constraints)

2) ac3() for edge consistency (takes care of binary constraints)
Unfortunately even with node and edge consistency we still not necessarily have a clear result.

3) instantiateRec(...) for variable instantiation and backups, if...

4) ac3la(...) ...fails. Checks whether the instantiation would lead to a consistent state,
or to be more precise: After the instantiation of vertex x, we check all vertices
pointing to vertex x with a higher index. If it fails, we have to revert the instantiation
and try the next one on the same vertex. 
    
    This was by the way the hardest part of the exercise. All the algorithms were given and
    Kotlin is a very easy and expressive language so that I didn't have much problems with that.
    It was much more difficult to implement the backup and restoration in the instantiation,
    because one have to take care whether references or values were copied.
    
    After all, yes it works and it works correctly.

For more information just read the comments.

## Mistakes and difficulties
- I like my classes and there are some elegant solutions in there, but it's not very easy to test, or at least it should
be easier.
- The vertices shouldn't be visible to the outside at all (except the string representation in graph.toString()).
addVertex should be replaced with addEdge calls. 
- Get a vertex outside an ArrayList by it's name is very far away from beeing a nice solution.
- The backup should've been much easier. I have to rethink it.

## Stuff I have done right
- Their are many functions that are written really shortly and elegant
- The code is pretty good commented
- The given algorithms are directly implemented and one can see them pretty good inside the code.
- I have learned a good amount of Kotlin.

## What I've learned, ideas
- Kotlin
- I need to have a library for table console output. It would be pretty handy.
- Some short cuts for Jave would be nice, especially for list creations.
- It would be nice to have the with of Kotlin (and visual basic) in Jave. I am looking forward to try to implement it.
- I need to think about testability directly from the start. It should be easy to write some tests for my classes.
- I am nood quite sure, but I assume it should be possible to replace Vertex and Edge with data classes.
It could be shorter and better and it could make backup much easier.
