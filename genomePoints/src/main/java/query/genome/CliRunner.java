package query.genome;

import picocli.CommandLine;
import picocli.CommandLine.Option;

public class CliRunner implements Runnable {
	
	@Option(
		names = { "-i", "--init" },
		required = false,
		description = "Initialize genomic data for querying points using input TSV file")
	private String input;
	
	@Option(
		names = { "-d", "--data-dir" },
		required = false,
		description = "Directory where organized genomic is stored/to be stored")
	private String dataDirectory = "data";
	
	@Option(
		names = { "-q", "--query" },
		required = false,
		description = "Query for points of the genome")
	private String queryString;
	
	@Option(
		names = { "-o", "--output" },
		required = false,
		description = "Output path for query")
	private String output;
	
	@Override
	public void run() {
		if (input != null && !input.isEmpty()) {
			new GenomicDataOrganizer(input, dataDirectory).run();
		}
		else if (queryString != null && !queryString.isEmpty()) {
			GenomicDataReader dataReader = new GenomicDataReader(dataDirectory);
			if (output == null || output.isEmpty()) {
				output = queryString.replace(':', '_') + ".txt";
			}
			
			dataReader.queryPoints(queryString, output);
		}
		else {
			System.out.println("Please input arguments.");
		}
	}
	
	public static void main(String[] args) {
		CommandLine.run(new CliRunner(), System.out, args);
	}
	
}
