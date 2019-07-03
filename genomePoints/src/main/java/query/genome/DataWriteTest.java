package query.genome;

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

public class DataWriteTest {
	
	public static void main(String[] args) throws IOException {
		Path inputPath = Paths.get(System.getProperty("user.home"), "probes.txt");
		Path outputPath = Paths.get(System.getProperty("user.home"), "data.dat");
		Path indexPath = Paths.get(System.getProperty("user.home"), "index.dat");
		
		CSVReader reader = setupReader(inputPath);
		
		Map<String, Long> chromosomeIndex = new LinkedHashMap<>();
		
		try (RandomAccessFile writer = new RandomAccessFile(outputPath.toFile(), "rw")) {
			String[] record;
			while ((record = reader.readNext()) != null) {
				String chromosome = record[0];
				int start = Integer.parseInt(record[1]);
				int end = Integer.parseInt(record[2]);
				double value = Double.parseDouble(record[3]);
				
				if (!chromosomeIndex.containsKey(chromosome)) {
					chromosomeIndex.put(chromosome, writer.getFilePointer());
				}
				writer.writeUTF(chromosome);
				writer.writeInt(start);
				writer.writeInt(end);
				writer.writeDouble(value);
			}
		}
		
		try (RandomAccessFile writer = new RandomAccessFile(indexPath.toFile(), "rw")) {
			for (Map.Entry<String, Long> entry : chromosomeIndex.entrySet()) {
				writer.writeUTF(entry.getKey());
				writer.writeLong(entry.getValue());
			}
		}
	}
	
	private static CSVReader setupReader(Path inputPath) throws IOException {
		CSVParser parser = new CSVParserBuilder().withSeparator('\t').build();
		CSVReader reader = new CSVReaderBuilder(Files.newBufferedReader(inputPath))
						.withCSVParser(parser)
						.withSkipLines(1)
						.build();
		return reader;
	}
}
