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
		names = { "--data-dir" },
		required = false,
		description = "Directory where organized genomic is stored/to be stored")
	private String dataDirectory = "data";
	
	@Option(
		names = { "-q", "--query" },
		required = false,
		description = "Query for points of the genome")
	private String query;
	
	@Option(
		names = { "-o", "--output" },
		required = false,
		description = "Output path for query")
	private String output;
	
	@Override
	public void run() {
		if (input != null && !input.isEmpty()) {
			System.out.println("Initialize genomic data organization");
		}
		else if (query != null && !query.isEmpty()) {
			System.out.println("Query genomic data for points");
		}
		else {
			System.out.println("Please input arguments.");
		}
	}
	
	public static void main(String[] args) {
		CommandLine.run(new CliRunner(), System.out, args);
	}
	
}
