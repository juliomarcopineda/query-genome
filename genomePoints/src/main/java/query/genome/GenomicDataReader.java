package query.genome;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GenomicDataReader {
	private Path dataDirectory;
	
	private Map<String, Long> chromosomeIndex;
	
	public GenomicDataReader(String dataDirectory) {
		Path dataDirectoryPath = Paths.get(dataDirectory);
		// Handle errors
		
		this.dataDirectory = dataDirectoryPath;
		this.chromosomeIndex = setupChromosomeIndex();
	}
	
	public List<Point> getPoints(String queryString) {
		List<Point> points = new ArrayList<>();
		
		List<Query> queries = QueryParser.parse(queryString);
		
		Path dataPath = Paths.get(this.dataDirectory.toString(), "data.dat");
		try (RandomAccessFile reader = new RandomAccessFile(dataPath.toFile(), "r")) {
			for (Query query : queries) {
				System.out.println(query);
				String queryChr = query.getChromosome();
				
				// Go to byte position of chromosome
				reader.seek(this.chromosomeIndex.get(queryChr));
				
				String pointChr;
				while (!isEOF(reader) && (pointChr = reader.readUTF()).equals(queryChr)) {
					int pointStart = reader.readInt();
					int pointEnd = reader.readInt();
					double pointValue = reader.readDouble();
					
					if (pointStart >= query.getStart() && pointEnd <= query.getEnd()) {
						Point point = new Point();
						point.setChromosome(pointChr);
						point.setStart(pointStart);
						point.setEnd(pointEnd);
						point.setValue(pointValue);
						points.add(point);
						System.out.println(point);
					}
				}
			}
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		return points;
	}
	
	private Map<String, Long> setupChromosomeIndex() {
		Map<String, Long> chromosomeIndex = new HashMap<>();
		
		Path indexPath = Paths.get(this.dataDirectory.toString(), "index.dat");
		try (RandomAccessFile reader = new RandomAccessFile(indexPath.toFile(), "r")) {
			while (reader.getFilePointer() < reader.length()) {
				chromosomeIndex.put(reader.readUTF(), reader.readLong());
			}
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		return chromosomeIndex;
	}
	
	private boolean isEOF(RandomAccessFile reader) throws IOException {
		return reader.getFilePointer() >= reader.length();
	}
}
