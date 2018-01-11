To run the executable jar file, run the command:
java –cp ./oop.jar ie.gmit.sw.Runner

Link for github:
https://github.com/Raftery93/FileComparissonProject

This project contains classes:
Runner.java
UI.java
FileParser.java
Shingle.java
MinHasher.java
Poison.java

When the program is ran, it will prompt the user for 2 documents (.txt files)
I have included 2 files to test: 
test1.txt
test2.txt
These files are exactly the same and should give a jaccard value of aproximately
1. The program is compatible with any text file aslong the user enters ".txt"

The program will then prompt the user for the shingle size.

Within this folder, I have submitted a UML Diagram (design.png).
This folder also contains a series of JavaDocs which contains comments from the source code.

I have left old code within my source code, just to show multiple attempts at trying
to acomplish different things. I have also left 'debug' code inside the source code
(commented out ofcourse). This is to show that I spent a bit of time debugging my program.

I have also used maps in this program, see MinHasher.java