
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class BruteCollinearPoints {

	private ArrayList<LineSegment> segments;

	/**
	 * Examines four points at a time.
	 * @param points
	 */
	public BruteCollinearPoints(Point[] points) {
		// finds all line segments containing 4 points
		if (null==points) throw new java.lang.NullPointerException();
		
		// copy and pre-sort
		int N = points.length;
		Point[] mypoints = Arrays.copyOf(points, N);
		Arrays.sort(mypoints); // n*log(n)

		// determine the line segments: o(n^4)
		segments = new ArrayList<LineSegment>();
		for(int i = 1; i< N ;i++) { 
			Point p = mypoints[i];
			if (null==p) throw new java.lang.NullPointerException(); // null check
			if (p.compareTo(mypoints[i - 1]) == 0) {
				throw new java.lang.IllegalArgumentException(); 
			}
			Comparator<Point> slopeOrder = p.slopeOrder();
			for (int j=i - 1; j >= 0; j--) { 
				for (int k=j-1; k>=0; k--) { 
					if (slopeOrder.compare(mypoints[j], mypoints[k]) == 0) {
						for (int l = k - 1; l >= 0; l--) { 
							if (slopeOrder.compare(mypoints[j], mypoints[l]) == 0) {
								segments.add(new LineSegment(mypoints[i], mypoints[l]));
							}
						}
					}
				}
			}
		}
	}

	public int numberOfSegments() {
		// the number of line segments
		return segments.size();
	}

	/**
	 * To check whether the 4 points p, q, r, and s are collinear, 
	 * check whether the three slopes between p and q, between p and r, 
	 * and between p and s are all equal.
	 * p->q->r->s => p->s only
	 * @return
	 */
	public LineSegment[] segments() {
		// the line segments
		return segments.toArray(new LineSegment[segments.size()]);
	}


}