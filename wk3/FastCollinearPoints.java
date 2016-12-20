
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Faster Kitty Cat.
 * Cost: n + n*log(n) + n*[n-i + (n-i)*log(n-i) + (n-i)] +  n + 2*n*log(n)  
 * ~ n^2log(n)

 * @author andrew
 */
public class FastCollinearPoints {
    private List<LineSegment> lineSegments;

    /**
     * finds all line segments containing 4 or more points
     * @param points
     */
    public FastCollinearPoints(Point[] points) {
        if (null==points) throw new java.lang.NullPointerException();
        // don't mutate input data
        Point[] mypoints = Arrays.copyOf(points, points.length); // o(N)
        Arrays.sort(mypoints); // pre-sort: n*log(n)

        lineSegments = scan(mypoints); // n^2*log(n)
    }

    public int numberOfSegments() {
        // the number of line segments
        return lineSegments.size();
    }

    public LineSegment[] segments()    {
        return lineSegments.toArray(new LineSegment[lineSegments.size()]);
    }

    // Scans points for collinear line segments
    private List<LineSegment> scan(Point[] mypoints) {
        SegmentTracker tracker = new SegmentTracker();

        int N = mypoints.length;
        for(int i = 0; i< N; i++) { 
            Point p = mypoints[i];
            // validate
            if (null==p) throw new java.lang.NullPointerException(); // null check
            if (i > 0 && p.compareTo(mypoints[i - 1]) == 0) {
                throw new java.lang.IllegalArgumentException(); 
            }

            Comparator<Point> slopeOrdering = p.slopeOrder();
            // get mutable subset of points
            Point[] locusOfPoints = Arrays.copyOfRange(mypoints, i + 1, N); // o(N), storage: N
            // sort using our slopeOrder implementation!
            Arrays.sort(locusOfPoints, slopeOrdering); // o(N*logN)

            int consecutiveSegmentCount = 0; // keep track of consecutive collinear segments
            int M = locusOfPoints.length;
            for (int j = 0; j < M - 1; j++) { // o(N)
                if (slopeOrdering.compare(locusOfPoints[j], locusOfPoints[j + 1]) == 0) {
                    consecutiveSegmentCount++;
                } else {
                    if (consecutiveSegmentCount > 1) { // means we have at least four collinear points
                        tracker.add(p, locusOfPoints[j]);
                    }
                    consecutiveSegmentCount = 0; // reset to enforce continguous
                }
            }
            // check if the last point was collinear too
            if (consecutiveSegmentCount > 1) { 
                tracker.add(p, locusOfPoints[M - 1]);
            }
        }
        return tracker.asList(); // n + 2*n*log(n)
    }


    //---------------- private classes-----------//

    // stores segments, returns maximal segments
    private class SegmentTracker {
        private List<LineSegmentDetail> segmentList;

        SegmentTracker() {
            segmentList = new ArrayList<LineSegmentDetail>();
        }

        public void add(Point p1, Point p2) {
            LineSegmentDetail incoming = new LineSegmentDetail(p1, p2);
            segmentList.add(incoming);
        }

        public List<LineSegment> asList() {
            List<LineSegment> lineSegments = new ArrayList<LineSegment>();
            int N = segmentList.size();
            if (N == 0) {
                return lineSegments;
            }
            // sort by slope, then by last point
            Collections.sort(segmentList); 

            for(int i = 0; i < N ; i++) {
                LineSegmentDetail currentElement = segmentList.get(i);
                if (i > 0) { // look behind
                    LineSegmentDetail lastElement = segmentList.get(i - 1);
                    if (lastElement.compareTo(currentElement) == 0) {
                        continue; // collinear => skip
                    }
                }
                lineSegments.add(currentElement);
            }
            return lineSegments;
        }
    }

    private class LineSegmentDetail extends LineSegment implements Comparable<LineSegmentDetail> {
        private Point endpoint;
        private double slope;

        LineSegmentDetail(Point p1, Point p2) {
            super(p1, p2);
            endpoint = p2;
            slope = p1.slopeTo(p2);
        }

        // Sort by slope and if tied, by last point
        public int compareTo(LineSegmentDetail that) {
            double thatSlope = that.slope;
            if (thatSlope < slope) {
                return 1;
            } else if (thatSlope > slope) {
                return -1;
            } else {
                return endpoint.compareTo(that.endpoint);
            }	
        }
    }
}


