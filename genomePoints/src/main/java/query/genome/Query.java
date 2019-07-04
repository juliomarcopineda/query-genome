package query.genome;

public class Query {
	private String chromosome;
	private int start;
	private int end;
	
	public String getChromosome() {
		return chromosome;
	}
	
	public void setChromosome(String chromosome) {
		this.chromosome = chromosome;
	}
	
	public int getStart() {
		return start;
	}
	
	public void setStart(int start) {
		this.start = start;
	}
	
	public int getEnd() {
		return end;
	}
	
	public void setEnd(int end) {
		this.end = end;
	}
	
	@Override
	public String toString() {
		return "Query [chromosome=" + chromosome + ", start=" + start + ", end=" + end + "]";
	}
	
}
