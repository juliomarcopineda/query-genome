package query.genome;

public class Point {
	private String chromosome;
	private int start;
	private int end;
	private double value;
	
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
	
	public double getValue() {
		return value;
	}
	
	public void setValue(double value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		return "Point [chromosome=" + chromosome + ", start=" + start + ", end=" + end + ", value="
						+ value + "]";
	}
}
