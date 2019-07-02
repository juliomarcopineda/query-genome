package query.genome;

import java.util.ArrayList;
import java.util.List;

public class Query {
	private static List<GenomicQuery> parseQueryString(String queryString) {
		List<GenomicQuery> queries = new ArrayList<>();
		
		String[] dashSplit = queryString.split("-");
		// assume that dashSplit.length == 2
		
		
		return queries;
	}
	
	public static void main(String[] args) {
		String queryString = args[0];
		
		String[] dashSplit = queryString.split("-");
		// assume that dashSplit.length == 2
		// TODO write check
		
		String beforeClause = dashSplit[0];
		String afterClause = dashSplit[1];
		
		String[] beforeColonSplit = beforeClause.split(":");
		String[] afterColonSplit = afterClause.split(":");
		
		List<GenomicQuery> test = new ArrayList<>();
		if (afterColonSplit.length == 1) {
			GenomicQuery query = new GenomicQuery();
			query.setChromosome(beforeColonSplit[0]);
			query.setStart(Integer.parseInt(beforeColonSplit[1]));
			query.setEnd(Integer.parseInt(afterColonSplit[0]));
			test.add(query);
		}
		else {
			GenomicQuery query1 = new GenomicQuery();
			query1.setChromosome(beforeColonSplit[0]);
			query1.setStart(Integer.parseInt(beforeColonSplit[1]));
			query1.setEnd(Integer.MAX_VALUE);
			test.add(query1);
			
			GenomicQuery query2 = new GenomicQuery();
			query2.setChromosome(afterColonSplit[0]);
			query2.setStart(0);
			query2.setEnd(Integer.parseInt(afterColonSplit[1]));
			test.add(query2);
		}
		
		
		List<GenomicQuery> queries = parseQueryString(queryString);
	}
}
