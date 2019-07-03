package query.genome;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

public class DataOrganizer implements Runnable {
	private Path inputPath;
	private Path dataDirectoryPath;
	
	public DataOrganizer(String input, String dataDirectory) {
		Path inputPath = Paths.get(input);
		if (!Files.exists(inputPath)) {
			// Handle error
		}
		this.inputPath = inputPath;
		this.dataDirectoryPath = Paths.get(dataDirectory);
	}
	
	public void run() {
		try {
			CSVReader reader = setupReader();
			String[] record;
			while ((record = reader.readNext()) != null) {
				String chromosome = record[0];
				int start = Integer.parseInt(record[1]);
				int end = Integer.parseInt(record[2]);
				double value = Double.parseDouble(record[3]);
				
				System.out.println(chromosome + " " + start + " " + end + " " + value);
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
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
