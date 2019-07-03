package query.genome;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.google.gson.Gson;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

public class DataOrganizer implements Runnable {
	private Path inputPath;
	private Path dataDirectoryPath;
	
	private static final int BIN_SIZE = 10000000;
	
	public DataOrganizer(String input, String dataDirectory) {
		Path inputPath = Paths.get(input);
		if (!Files.exists(inputPath)) {
			// Handle error
		}
		
		this.inputPath = inputPath;
		this.dataDirectoryPath = Paths.get(dataDirectory);
	}
	
	public void run() {
		Gson gson = new Gson();
		
		try {
			CSVReader reader = setupReader();
			
			int count = 0;
			String[] record;
			while ((record = reader.readNext()) != null) {
				Point point = createPointFromRecord(record);
				
				Path jsonDir = resolvePath(point);
				Files.createDirectories(jsonDir);
				
				Path filePath = Paths.get(jsonDir.toString(),
								point.getStart() + "-" + point.getEnd() + ".json");
				
				BufferedWriter writer = Files.newBufferedWriter(filePath);
				gson.toJson(point, writer);
				writer.close();
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private Path resolvePath(Point point) {
		String chromosome = point.getChromosome();
		String bin = String.valueOf(point.getStart() / BIN_SIZE * BIN_SIZE);
		
		return Paths.get(this.dataDirectoryPath.toString(), chromosome, bin);
	}
	
	private CSVReader setupReader() throws IOException {
		CSVParser parser = new CSVParserBuilder().withSeparator('\t').build();
		CSVReader reader = new CSVReaderBuilder(Files.newBufferedReader(this.inputPath))
						.withCSVParser(parser)
						.withSkipLines(1)
						.build();
		return reader;
	}
	
	private Point createPointFromRecord(String[] record) {
		Point point = new Point();
		
		point.setChromosome(record[0]);
		point.setStart(Integer.parseInt(record[1]));
		point.setEnd(Integer.parseInt(record[2]));
		point.setValue(Double.parseDouble(record[3]));
		
		return point;
	}
}
