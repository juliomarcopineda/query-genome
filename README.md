# Organize genomic data for random access
Command-line interface for organizing genomic data into a binary file for fast random access, and for querying points of the genome in single or multiple chromosomes.

## Setup: Build JAR file
Building the JAR file to run the command-line interface is performed using gradle.

```
cd /query-genome/genomePoints
../gradlew shadowJar
```

The built JAR file is located at `builds/libs` with the name `genome.jar`

## Organize input data by serializing and indexing points of the genome
### Usage
`java -jar genome.jar -i=/path/to/probes.txt [-d=/path/to/data/dir]`

Or

`java -jar genome.jar -i probes.txt [-d /path/to/data/dir]`

### Description
This command organizes a tab-delimited genomic data with strict ordering of the following headers: chromosome, start, end and value. Each record or row of the input file is treated as a point of the genome. Additionally, each record is assumed to be grouped/sorted by their chromosomes which is important for the indexing process.

The input data is read using `opencsv` library to iterate through each record of the input. Then, each record is serialized into its byte representation using `RandomAccessFile` writing mode. Serialization allows easy programmatic access to the genome points and storing the serialized data into binary provides efficient memory usage.
The serialized data is stored in a binary file that defaults to `/data/data.dat`.

While each record is read and serialized, the byte buffer offset is stored in a `Map` every time the chromosome changes. This `Map` is the chromosome index which allows fast random access of `data.dat` using the chromosome for queries. 
The index is stored in a separate binary file that defaults to `/data/index.dat` 

## Query for points of the genome in single or multiple chromosomes
### Usage
#### Single Chromosome Query
`java -jar genome.jar -q=chr18:0-60000000 [-o=/path/to/results.txt] [-d=/path/to/data/dir]`

Or

`java -jar genome.jar -q chr18:0-60000000 [-o /path/to/results.txt] [-d /path/to/data/dir]`

#### Multiple Chromosome Query
`java -jar genome.jar -q=chr3:5000-chr5:8000 [-o=/path/to/results.txt] [-d=/path/to/data/dir]`

Or

`java -jar genome.jar -q chr3:5000-chr5:8000 [-o /path/to/results.txt] [-d /path/to/data/dir]`

### Description
This command supports two types of query syntax:
1) A single chromosome query such as `chr18:0-60000000` where it returns and writes each point in Chromosome 18 within the start base position 0 and end position 60000000.
2) A multiple chromosome query such as `chr3:5000-chr5:8000` where it returns and writes each point between base Chromosome 3 base position 5000 and the end of Chromosome 3, and each point between the beginning of Chromosome 5 to Chromosome 5 base position 8000.

The program parses these two types of queries into a `Query` object where each `Query` represents one chromosome and a range from start to end position. Thus, the multiple  chromosome query is represented by a collection of `Query`.

The program loads into memory the chromosome index file (defaults to `/data/index.dat`) into a `Map`. Then, the serialized data file (defaults to `/data/data.dat`) is read using a `RandomAccessFile` such that the entire data file is not loaded into memory. 

For each `Query`, the program takes the byte buffer offset of the chromosome from the index and moves the `RandomAccessFile` pointer to this offset. Then, each point of the particular chromosome is deserialized, and if the point is within the query start and end positions, then it is written into a tab-delimited text file.
The output file path defaults to `<input_query_string.txt>`

## Future Improvements
This current prototype assumes that the input data will only have chromosome, start, end, and value in that exact order; it does not accommodate for more complex data structures. Existing serialization libraries can be considered that store complex data structures in an efficient format and maintain a scheme/protocol for serialization.

Protobuf is an existing library that can serialize data structures using a schema. FlatBuffers and Cap’n Proto are also notable libraries that not only serializes data structures, but also allows random access of these data structures.

