package query.genome;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.opencsv.CSVWriter;

/**
 * GenomicDataReader randomly accesses the organized genomic data stored in a binary format. This
 * class allows querying of points of the genome in a range of positions in a single chromosome or
 * multiple chromosome.
 * 
 * This class writes the results of the query into a tab-delimited text file.
 * 
 * @author Julio Pineda
 *
 */
public class GenomicDataReader {
	private Path dataPath;
	
	private Map<String, Long> chromosomeIndex;
	
	/**
	 * Given the path to the organized genomic data directory, constructs the GenomicDataReader class.
	 * 
	 * Throws IllegalArgumentExcpetion if the provided data directory does not contain the data binary file.
	 * 
	 * @param dataDirectory
	 */
	public GenomicDataReader(String dataDirectory) {
		Path dataPath = Paths.get(dataDirectory, "data.dat");
		if (!Files.exists(dataPath)) {
			System.out.println(dataPath.toString() + " does not exist");
			throw new IllegalArgumentException();
		}
		
		this.dataPath = dataPath;
		this.chromosomeIndex = setupChromosomeIndex(dataDirectory);
	}
	
	/**
	 * Queries the organized genomic data binary file for genome points.
	 * 
	 * The current implementation handles two types of queries:
	 * 1. Range of points for a single chromosome (e.x. chr18:0-60000000)
	 * 2. Range of points for two chromosomes (e.x. chr3:5000-chr5:8000)
	 * 
	 * Writes the results into a tab-delimited file at the given output path.
	 * 
	 * Throws IllegalArgumentExcpetion if the output path is not writable.
	 * 
	 * @param queryString
	 * @param output
	 */
	public void queryPoints(String queryString, String output) {
		Path outputPath = Paths.get(output);
		if (!Files.isWritable(outputPath)) {
			System.out.println(output + " is not writable");
			throw new IllegalArgumentException();
		}
		
		List<Query> queries = QueryParser.parse(queryString);
		
		try (CSVWriter writer = setupWriter(outputPath)) {
			String[] headers = { "Chromosome", "Start", "End", "Value" };
			writer.writeNext(headers);
			
			try (RandomAccessFile reader = new RandomAccessFile(this.dataPath.toFile(), "r")) {
				for (Query query : queries) {
					String queryChr = query.getChromosome();
					
					// Move file pointer to byte offset of the query chromosome
					reader.seek(this.chromosomeIndex.get(queryChr));
					
					/**
					 * The reader iterates through each point of the genome by reading the bytes
					 * in this defined order: chromosome, start, end, value.
					 * 
					 * The goal is to seek to a particular chromosome determined by the query, 
					 * iterate over all the points, and only write points that satisfy the query.
					 */
					String pointChr;
					while (!isEOF(reader) && (pointChr = reader.readUTF()).equals(queryChr)) {
						int pointStart = reader.readInt();
						int pointEnd = reader.readInt();
						double pointValue = reader.readDouble();
						
						if (pointStart >= query.getStart() && pointEnd <= query.getEnd()) {
							String[] record = { pointChr, String.valueOf(pointStart),
											String.valueOf(pointEnd), String.valueOf(pointValue) };
							writer.writeNext(record);
						}
					}
				}
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Reads the chromosome index binary file to determine the byte offsets of each chromosome.
	 * Loads this chromosome index to a map.
	 * 
	 * @param dataDirectory
	 * @return Returns the map of chromosome name and byte offset.
	 */
	private Map<String, Long> setupChromosomeIndex(String dataDirectory) {
		Path indexPath = Paths.get(dataDirectory.toString(), "index.dat");
		if (!Files.exists(indexPath)) {
			System.out.println(indexPath.toString() + " does not exist");
			throw new IllegalArgumentException();
		}
		
		Map<String, Long> chromosomeIndex = new HashMap<>();
		
		try (RandomAccessFile reader = new RandomAccessFile(indexPath.toFile(), "r")) {
			while (reader.getFilePointer() < reader.length()) {
				chromosomeIndex.put(reader.readUTF(), reader.readLong());
			}
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		return chromosomeIndex;
	}
	
	/**
	 * Returns true if the random access file is at the end of the file.
	 * 
	 * @param reader
	 * @return true if file pointer is at the end of the file, otherwise false
	 * @throws IOException
	 */
	private boolean isEOF(RandomAccessFile reader) throws IOException {
		return reader.getFilePointer() >= reader.length();
	}
	
	/**
	 * Sets up the writer to write tab-delimited results of a query.
	 * 
	 * @param outputPath
	 * @return CSVWriter that writes tab-delimited records
	 * @throws IOException
	 */
	private CSVWriter setupWriter(Path outputPath) throws IOException {
		BufferedWriter bufferedWriter = Files.newBufferedWriter(outputPath);
		CSVWriter writer = new CSVWriter(bufferedWriter, '\t', CSVWriter.NO_QUOTE_CHARACTER,
						CSVWriter.DEFAULT_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END);
		return writer;
	}
}
