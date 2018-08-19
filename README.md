# SimulatedAnnealingExample
An example Simulated Annealing. Requires a set of libraries, including Apache POI, described in the pom.xml for Maven users. Non maven users should check out the same file for the JARs to download. 

Simply put, the following String is the "problem" to be solved:

1-7-3-4-6-7-3-2-1-4-7-6-Charlie-3-2-7-8-9-7-7-7-6-4-3-Tango-7-3-2-Victor-7-3-1-1-7-8-8-8-7-3-2-4-7-6-7-8-9-7-6-4-3-7-6

SA will generate a String of the same length (assumed Hard Constraint), and will repeatedly replace characters until 
the end time condition/s are met.

In this example there is a single simple Soft Constraint (SC) which considers fitness as similarity to the solution 
in terms of number of characters correct thus far. A better example might be to generate random Strings of random
lengths, and have a soft constraint to consider length. This could also be further advanced with unicode consideration.
And of course, less "insider knowledge" for the SC. 

This also contains some output using the Apache POI library, fairly useful for examining results from the SA.
For more optimised statistical analysis the Output.java file should be modified for outputting CSV files,
for use in R etc. 
