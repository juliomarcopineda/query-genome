package query.genome;

import java.util.ArrayList;
import java.util.List;

/**
 * QueryParser contains helper methods to parse through a query String to build 
 * Query objects for querying points in a genome.
 * 
 * @author Julio Pineda
 *
 */
public class QueryParser {
	/**
	 * Parses the String query and returns a List of Query objects.
	 * This implementation currently assumes that a query has a before clause
	 * and after clause separated by a -.
	 * 
	 * The current implementation handles two types of queries:
	 * 1. Range of points for a single chromosome (e.x. chr18:0-60000000)
	 * 2. Range of points for two chromosomes (e.x. chr3:5000-chr5:8000)
	 * 
	 * Throws IllegalArgumentException if the query syntax is incorrect.
	 * 
	 * @param queryString
	 * @return
	 */
	public static List<Query> parse(String queryString) {
		List<Query> queries = new ArrayList<>();
		
		String[] clauses = queryString.split("-");
		if (clauses.length != 2) {
			System.out.println("Invalid query syntax");
			throw new IllegalArgumentException();
		}
		
		String[] beforeClause = clauses[0].split(":");
		String[] afterClause = clauses[1].split(":");
		
		if (beforeInvalid(beforeClause) || afterInvalid(afterClause)) {
			System.out.println("Invalid query syntax");
			throw new IllegalArgumentException();
		}
		
		String beforeChromosome = beforeClause[0];
		int beforePosition = Integer.parseInt(beforeClause[1]);
		if (afterClause.length == 1) {
			/**
			 * Single chromosome query where afterClause does not
			 * contain chromosome name information
			 */
			int afterPosition = Integer.parseInt(afterClause[0]);
			
			queries.add(buildQuery(beforeChromosome, beforePosition, afterPosition));
		}
		else {
			String afterChromosome = afterClause[0];
			int afterPosition = Integer.parseInt(afterClause[1]);
			
			if (beforeChromosome.equals(afterChromosome)) {
				// beforeClause and afterClause have the same chromosome
				// Treat as a single chromosome query
				queries.add(buildQuery(beforeChromosome, beforePosition, afterPosition));
			}
			else {
				/**
				 * This case is where the query spans two chromosomes.
				 * The assumption is that this multi-chromosome query can be broken up into two
				 * single chromosome query.
				 * 
				 * The beforeClause assumes that we query for all points after the beforePosition
				 * for its chromosome.
				 * On the other hand, the afterClause assumes we query for all points
				 * before the after position.
				 */
				queries.add(buildQuery(beforeChromosome, beforePosition, Integer.MAX_VALUE));
				queries.add(buildQuery(afterChromosome, 0, afterPosition));
			}
		}
		
		return queries;
	}
	
	/**
	 * Returns true if the before clause of the query String is invalid.
	 * 
	 * The before clause is not valid if it has the following:
	 * 1. The String array is not length 2
	 * 2. The first element does not have "chr" to designate a chromosome name
	 * 
	 * @param beforeClause
	 * @return true if beforeClause is not valid, otherwise false
	 */
	private static boolean beforeInvalid(String[] beforeClause) {
		if (beforeClause.length != 2) {
			return true;
		}
		
		if (!beforeClause[0].contains("chr")) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * Returns true if the after clause of the query String is invalid.
	 * 
	 * The after clause is not valid if it has the following:
	 * 1. The String array length is greater than 2
	 * 
	 * @param afterClause
	 * @return true if afterClause is not valid, otherwise false
	 */
	private static boolean afterInvalid(String[] afterClause) {
		if (afterClause.length > 2) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * Builds and return Query given the chromosome name, start position and end position.
	 * 
	 * @param chromosome
	 * @param start
	 * @param end
	 * @return Query
	 */
	private static Query buildQuery(String chromosome, int start, int end) {
		Query query = new Query();
		query.setChromosome(chromosome);
		query.setStart(start);
		query.setEnd(end);
		return query;
	}
	
}
