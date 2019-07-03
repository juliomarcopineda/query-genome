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

public class DataReadTest {
	public static void main(String[] args) {
		Path dataPath = Paths.get(System.getProperty("user.home"), "data.dat");
		Path indexPath = Paths.get(System.getProperty("user.home"), "index.dat");
		
		Map<String, Long> chromosomeIndex = new HashMap<>();
		try (RandomAccessFile r = new RandomAccessFile(indexPath.toFile(), "r")) {
			while (r.getFilePointer() < r.length()) {
				chromosomeIndex.put(r.readUTF(), r.readLong());
			}
		}
		catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try (RandomAccessFile r = new RandomAccessFile(dataPath.toFile(), "r")) {
			
			for (String chromosome : List.of("chr15", "chr2", "chr20", "chrX")) {
				List<Point> points = getPoints(r, chromosome, chromosomeIndex);
				System.out.println(chromosome + "\t" + points.size());
			}
			
		}
		catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static List<Point> getPoints(RandomAccessFile r, String chr, Map<String, Long> index)
					throws IOException {
		List<Point> points = new ArrayList<>();
		
		r.seek(index.get(chr));
		String chromosome;
		while (r.getFilePointer() < r.length() && (chromosome = r.readUTF()).equals(chr)) {
			int start = r.readInt();
			int end = r.readInt();
			double value = r.readDouble();
			
			Point point = new Point();
			point.setChromosome(chromosome);
			point.setStart(start);
			point.setEnd(end);
			point.setValue(value);
			points.add(point);
		}
		
		return points;
	}
	
}
