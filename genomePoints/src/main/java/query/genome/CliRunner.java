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
	private String dataDirectory;
	
	@Option(
		names = { "-q", "--query" },
		required = false,
		description = "Query for points of the genome")
	private String query;
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
	
	public static void main(String[] args) {
		CommandLine.run(new CliRunner(), System.out, args);
	}
	
}
