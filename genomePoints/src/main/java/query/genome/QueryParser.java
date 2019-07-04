package query.genome;

import java.util.ArrayList;
import java.util.List;

public class QueryParser {
	public static List<Query> parse(String queryString) {
		List<Query> queries = new ArrayList<>();
		
		String[] clauses = queryString.split("-");
		if (clauses.length != 2) {
			// Handle error
		}
		
		String[] beforeClause = clauses[0].split(":");
		String[] afterClause = clauses[1].split(":");
		
		// Check clauses first
		
		String beforeChromosome = beforeClause[0];
		int beforePosition = Integer.parseInt(beforeClause[1]);
		if (afterClause.length == 1) {
			int afterPosition = Integer.parseInt(afterClause[0]);
			
			queries.add(buildQuery(beforeChromosome, beforePosition, afterPosition));
		}
		else {
			String afterChromosome = afterClause[0];
			int afterPosition = Integer.parseInt(afterClause[1]);
			
			if (beforeChromosome.equals(afterChromosome)) {
				queries.add(buildQuery(beforeChromosome, beforePosition, afterPosition));
			}
			else {
				queries.add(buildQuery(beforeChromosome, beforePosition, Integer.MAX_VALUE));
				queries.add(buildQuery(afterChromosome, 0, afterPosition));
			}
		}
		
		return queries;
	}
	
	private static Query buildQuery(String chromosome, int start, int end) {
		Query query = new Query();
		query.setChromosome(chromosome);
		query.setStart(start);
		query.setEnd(end);
		return query;
	}
	
}
