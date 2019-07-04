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

public class GenomicDataReader {
	private Path dataDirectory;
	
	private Map<String, Long> chromosomeIndex;
	
	public GenomicDataReader(String dataDirectory) {
		Path dataDirectoryPath = Paths.get(dataDirectory);
		// Handle errors
		
		this.dataDirectory = dataDirectoryPath;
		this.chromosomeIndex = setupChromosomeIndex();
	}
	
	public void queryPoints(String queryString, String output) {
		Path outputPath = Paths.get(output);
		// Files.isWritable(outputPath);
		// TODO check writable output path and handle
		
		List<Query> queries = QueryParser.parse(queryString);
		
		try (CSVWriter writer = setupWriter(outputPath)) {
			String[] headers = { "Chromosome", "Start", "End", "Value" };
			writer.writeNext(headers);
			
			Path dataPath = Paths.get(this.dataDirectory.toString(), "data.dat");
			try (RandomAccessFile reader = new RandomAccessFile(dataPath.toFile(), "r")) {
				for (Query query : queries) {
					String queryChr = query.getChromosome();
					
					// Go to byte position of chromosome
					reader.seek(this.chromosomeIndex.get(queryChr));
					
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
	
	private Map<String, Long> setupChromosomeIndex() {
		Map<String, Long> chromosomeIndex = new HashMap<>();
		
		Path indexPath = Paths.get(this.dataDirectory.toString(), "index.dat");
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
	
	private boolean isEOF(RandomAccessFile reader) throws IOException {
		return reader.getFilePointer() >= reader.length();
	}
	
	private CSVWriter setupWriter(Path outputPath) throws IOException {
		BufferedWriter bufferedWriter = Files.newBufferedWriter(outputPath);
		CSVWriter writer = new CSVWriter(bufferedWriter, '\t', CSVWriter.NO_QUOTE_CHARACTER,
						CSVWriter.DEFAULT_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END);
		return writer;
	}
}
