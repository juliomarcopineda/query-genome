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

public class GenomicDataOrganizer implements Runnable {
	private Path inputPath;
	private Path dataDirectoryPath;
	
	public GenomicDataOrganizer(String input, String dataDirectory) {
		Path inputPath = Paths.get(input);
		if (!Files.exists(inputPath)) {
			System.out.println(input + " does not exist. Please input correct genomic data path.");
			System.exit(-1);
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
	
	public void run() {
		Map<String, Long> chromosomeIndex = new LinkedHashMap<>();
		
		// Write data to binary file
		Path dataPath = Paths.get(this.dataDirectoryPath.toString(), "data.dat");
		
		System.out.println("Writing organized genomic data to " + dataPath.toString());
		
		try (RandomAccessFile dataWriter = new RandomAccessFile(dataPath.toFile(), "rw")) {
			int status = 0;
			try (CSVReader inputReader = this.setupReader()) {
				String[] record;
				while ((record = inputReader.readNext()) != null) {
					if (++status % 250000 == 0) {
						System.out.println("Read records: " + status);
					}
					String chromosome = record[0];
					int start = Integer.parseInt(record[1]);
					int end = Integer.parseInt(record[2]);
					double value = Double.parseDouble(record[3]);
					
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
	
	private CSVReader setupReader() throws IOException {
		CSVParser parser = new CSVParserBuilder().withSeparator('\t').build();
		CSVReader reader = new CSVReaderBuilder(Files.newBufferedReader(this.inputPath))
						.withCSVParser(parser)
						.withSkipLines(1)
						.build();
		return reader;
	}
}
