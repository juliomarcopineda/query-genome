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
`java -jar genome.jar -i=probes.txt [-d=/path/to/data]`

or

`java -jar genome.jar -i probes.txt [-d /path/to/data]`

### Description

### Future improvements

## Query for points of the genome in single or multiple chromosomes
### Usage
`java -jar genome.jar -q=chr18:0-60000000 [-o=/path/to/results.txt]`

or

`java -jar genome.jar -q chr18:0-60000000 [-o /path/to/results.txt]`

### Description
