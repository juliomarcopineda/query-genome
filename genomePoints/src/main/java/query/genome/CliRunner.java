package query.genome;

import picocli.CommandLine;
import picocli.CommandLine.Option;

/**
 * CliRunner runs the command-line interface to organize genomic data on disk for
 * fast random access and to query for points of a genome in this organized data.
 * 
 * @author Julio Pineda
 *
 */
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
	
	/**
	 * Runs the following commands given the command-line arguments:
	 * 1. Organize and store input genomic data for future querying of genome points.
	 * 2. Query for points of a genome and writes results to a text file.
	 */
	@Override
	public void run() {
		if (input != null && !input.isEmpty()) {
			new GenomicDataOrganizer(input, dataDirectory).organize();
		}
		else if (queryString != null && !queryString.isEmpty()) {
			GenomicDataReader dataReader = new GenomicDataReader(dataDirectory);
			
			// Set default of output path for results if none is supplied
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
