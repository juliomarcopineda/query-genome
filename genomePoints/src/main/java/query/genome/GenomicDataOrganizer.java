package query.genome;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

/**
 * GenomicDataOrganizer reads in a tab-delimited genomic data that contains points of the genome, and
 * organizes this data for fast random access. This organization is achieved by writing byte representations
 * of each field of a point (chromosome name, start position, end position, value) and stored as a binary file.
 * 
 * The genomic data is also indexed by the chromosome name for faster querying of the data.
 * 
 * @author Julio Pineda
 *
 */
public class GenomicDataOrganizer {
	private Path inputPath;
	private Path dataDirectoryPath;
	
	/**
	 * Given the path to input tab-delimited genomic data and the data directory path 
	 * to storing organized data, constructs the GenomicDataOrganizer.
	 * 
	 * Ensures that all parent directories of the data directory path is created.
	 * 
	 * Throws IllegalArgumentExcpetion if file path of the input does not exist.
	 * 
	 * @param input
	 * @param dataDirectory
	 */
	public GenomicDataOrganizer(String input, String dataDirectory) {
		Path inputPath = Paths.get(input);
		if (!Files.exists(inputPath)) {
			System.out.println(input + " does not exist. Please input correct genomic data path.");
			throw new IllegalArgumentException();
		}
		
		Path dataDirectoryPath = Paths.get(dataDirectory);
		try {
			Files.createDirectories(dataDirectoryPath);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		this.inputPath = inputPath;
		this.dataDirectoryPath = dataDirectoryPath;
	}
	
	/**
	 * Organizes the tab-delimited genomic data by converting the chromosome name, start position,
	 * end position and value into byte representations in their respective order. The byte representations
	 * of the data are stored in a binary file.
	 * 
	 * The genomic data is also indexed by their chromosome names and stored in a separate binary file. This
	 * index can be used for faster access of the binary data file.
	 */
	public void organize() {
		Map<String, Long> chromosomeIndex = new LinkedHashMap<>();
		
		Path dataPath = Paths.get(this.dataDirectoryPath.toString(), "data.dat");
		
		System.out.println("Writing organized genomic data to " + dataPath.toString());
		
		// Write data to binary file
		try (RandomAccessFile dataWriter = new RandomAccessFile(dataPath.toFile(), "rw")) {
			int status = 0; // for displaying the number of records read
			try (CSVReader inputReader = this.setupReader()) {
				String[] record;
				while ((record = inputReader.readNext()) != null) {
					if (++status % 250000 == 0) {
						System.out.println("Read records: " + status);
					}
					
					// Assume that the tab-delimited file has the following order:
					// [chromosome, start, end, value]
					String chromosome = record[0];
					int start = Integer.parseInt(record[1]);
					int end = Integer.parseInt(record[2]);
					double value = Double.parseDouble(record[3]);
					
					// Assume that records in the tab-delimited file is grouped by
					// chromosome name
					if (!chromosomeIndex.containsKey(chromosome)) {
						chromosomeIndex.put(chromosome, dataWriter.getFilePointer());
					}
					dataWriter.writeUTF(chromosome);
					dataWriter.writeInt(start);
					dataWriter.writeInt(end);
					dataWriter.writeDouble(value);
				}
			}
			System.out.println("Read records: " + status);
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		// Write index of data to binary file
		Path indexPath = Paths.get(this.dataDirectoryPath.toString(), "index.dat");
		
		System.out.println("Writing chromosome index to " + indexPath.toString());
		
		try (RandomAccessFile indexWriter = new RandomAccessFile(indexPath.toFile(), "rw")) {
			for (Map.Entry<String, Long> entry : chromosomeIndex.entrySet()) {
				indexWriter.writeUTF(entry.getKey());
				indexWriter.writeLong(entry.getValue());
			}
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("Done.");
	}
	
	/**
	 * Builds and returns the CSVReader that can parse tab-delimited files.
	 * 
	 * @return
	 * @throws IOException
	 */
	private CSVReader setupReader() throws IOException {
		CSVParser parser = new CSVParserBuilder().withSeparator('\t').build();
		CSVReader reader = new CSVReaderBuilder(Files.newBufferedReader(this.inputPath))
						.withCSVParser(parser)
						.withSkipLines(1)
						.build();
		return reader;
	}
}
