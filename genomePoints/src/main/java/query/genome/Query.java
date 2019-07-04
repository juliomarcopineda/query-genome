package query.genome;

/**
 * Query represents a query for points of a genome in a singular chromosome for a given range.
 * 
 * @author Julio Pineda
 *
 */
public class Query {
	private String chromosome;
	private int start;
	private int end;
	
	/**
	 * Gets chromosome name.
	 * 
	 * @return chromosome name as String
	 */
	public String getChromosome() {
		return chromosome;
	}
	
	/**
	 * Sets chromosome name.
	 * 
	 * @param chromosome
	 */
	public void setChromosome(String chromosome) {
		this.chromosome = chromosome;
	}
	
	/**
	 * Gets start position
	 * 
	 * @return start position as int
	 */
	public int getStart() {
		return start;
	}
	
	/**
	 * Sets start position
	 * 
	 * @param start
	 */
	public void setStart(int start) {
		this.start = start;
	}
	
	/**
	 * Gets end position
	 * 
	 * @return end position as int
	 */
	public int getEnd() {
		return end;
	}
	
	/**
	 * Sets end position
	 * 
	 * @param end
	 */
	public void setEnd(int end) {
		this.end = end;
	}
	
	@Override
	public String toString() {
		return "Query [chromosome=" + chromosome + ", start=" + start + ", end=" + end + "]";
	}
	
}
