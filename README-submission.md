# Team Members

Last Name       | First Name      | GitHub User Name
--------------- | --------------- | --------------------
Sorensen        | Andrew     	  | andrew-sorensen
Thelander       | Emily           | emilythelander
Sullivan        | Zach            | ZSull

# Cache Performance Results
All tests were run using test5.gbk with a BTree degree of 10 (higher degree BTrees tend to build faster) and a sequence length of 7.
Further all tests were run using GeneBankCreateBTree debug option 2, which still creates a dump file but does not create a database.
All tests were run on Andrew's home computer.

Using no cache at all: 229 seconds

With a cache size of 100: 176 seconds

With a cache size of 500: 64 seconds

# BTree Binary File Format and Layout
Our BTree Binary is laid out as follows:
The metadata for the BTree is stored in the first 20 bytes in this order with these data types:

\<root pointer : long\>, \<degree : int\>, \<sequence length : int\>, \<number of nodes : int\>

The rest of the file contains TreeNodes laid out end to end. Each tree node starts with 18 bytes of metadata
in this format using these data types (char is 2 bytes):

\<Node offset : long\>, \<isleaf : char\>, \<number of objects/keys : int\>, \<number of children: int\>

After the node metadata all of the objects/keys for that node are written to file sequentially. First the long representing
the gene subsequence is written. Immediately following that long is an integer that represents the frequency of that gene subsequence

Following the section for the objects, all the node's children pointers are laid out sequentially. A child pointer is simply a long 
that represents the offset of a specific node in the binary file. 

A visual example of the file:
\[BTree Metadata, \[Node\], \[Next Node\]...\]

Where the internals of each node is as follows:
\[Node metadata, All Gene Subsequences(Long then frequency for each), Children pointers...\]

To locate a node, simply call btree.diskread(long) passing in the long that represents a node's offset in the file as the argument.

# COMPILING AND RUNNING
To compile use the following two commands:
javac *.java
jar xf sqlite-jdbc-3.36.0.3.jar

There are three driver programs which can be run as follows:

#### GeneBankCreateBTree:
java GeneBankCreateBTree \<0/1(no/with Cache)\> \<degree\> \<gbk file\> \<sequence length\> \[\<cache size\>\] \[\<debug level\>\]

\<0/1(no/with Cache)\>: determines whether or not a cache will be used, if a cache is used a cache size must be specified.
\<degree\>: the degree of the bTree node, if 0 is specified then the optimal degree will be determined.
\<gbk file\>: The gene bank sequence file that wants to be analyzed.
\<sequence length\>: the sequence length that must be between 1 and 31.
\[\<cache size\>\]: the size of the cache must be between 100 and 500.
\[\<debug level\>\]: the default level will be 0, a value of 1 will produce a dump file. A value of 2 will produce a dump file and no database

#### GeneBankSearchBTree:
java GeneBankCreateBTree \<0/1(no/with Cache)\> \<btree file\> \<query file\> \[\<cache size\>\] \[\<debug level\>\]

\<0/1(no/with Cache)\>: determines whether or not a cache will be used, if a cache is used a cache size must be specified.
\<btree file\>: The binary btree file containing gene bank subsequences to be queried.
\<query file\>: the file containing gene subsequences to search the btree for
\[\<cache size\>\]: the size of the cache. Must be between 100 and 500.
\[\<debug level\>\]: the default level will be 0 - output of query on standard output, 1 - create dump file of the output in a file called 'qdump'

#### GeneBankSearchDatabase:
java GeneBankSearchDataBase \<path_to_SQLite_database\> \<query_file\> \[\<debug_level\>\]

\<btree path_to_SQLite_databasefile\>: The path to the SQLite database to search.
\<query file\>: the file containing gene subsequences to search the database for
\[\<debug level\>\]: the default level will be 0, 1 will produce a dump file of the results called 'dbqdump'

# Additional Notes
Creating a database significantly adds to the runtime of GeneBankCreateBTree. As such, for the ease of testing and grading, debug option 2 has been provided. It will still produce a dump file but will not produce a database. 


