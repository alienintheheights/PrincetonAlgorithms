import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;


/**
 * Simple unit tests
 * @author andrew
 *
 */
public class KdTreeUtil {
    
    private static final String CIRCLE_TEST_FILE = "/Users/andrew/Documents/MOOC/Software/Princeton/algorithmsclass/git/data/kdtree/circle10.txt";

    private static final Point2D circleTestPoint = new Point2D(0.81, 0.30);
    
    /**
     * Main method
     * @param args
     */
    public static void main(String[] args) {

        KdTreeUtil test = new KdTreeUtil();

        // test creation of tree
        // test.testInsert();

        KdTree testTree = test.initData(args[0]);

        // test drawing
        test.drawTree(testTree);

        // test nearest search
       
        test.nearestSearch(testTree, circleTestPoint);
        
       // test.distTest();
    }
    
    /**
     * Read data
     * @param args
     * @return
     */
    public KdTree initData(String filename) {
        // TODO Auto-generated method stub
        In in = new In(filename);
        StdOut.println("Loading " + filename);

        KdTree kdtree = new KdTree();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            kdtree.insert(p);
        }
        StdOut.println("Loaded new KdTree, size is " + kdtree.size());
        return kdtree;
    }
    
    
    public Point2D nearestSearch(KdTree kdtree, Point2D searchPoint) {
        Point2D winner =  kdtree.nearest(searchPoint);
        StdOut.println("Closest point in tree to " + searchPoint.toString() + " is " + winner.toString());
        winner.draw();
        return winner;
    }
 
    

    public void drawTree(KdTree kdtree) {
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        StdOut.println(kdtree.size());
        kdtree.draw();
    }


    public void distTest() {
        Point2D q = new Point2D(0.81, 0.30);
        Point2D x1 = new Point2D(0.793893, 0.095492);
        Point2D x2 = new Point2D(0.793893, 0.095492);
        Point2D x3 = new Point2D(0.975528, 0.345492);
        StdOut.println("Distance from q to x1 is " + q.distanceSquaredTo(x1));
        StdOut.println("Distance from q to x2 is " + q.distanceSquaredTo(x2));
        StdOut.println("Distance from q to x3 is " + q.distanceSquaredTo(x3));
    }


    /**
     * Debug method.
     * @param point
     * @param rect
     * @param isVertical
     */
    private void drawRect(Point2D point, RectHV rect, boolean isVertical) {
        StdDraw.pause(2000);
        if (isVertical)  StdDraw.setPenColor(StdDraw.RED);
        else  StdDraw.setPenColor(StdDraw.BLUE);
        StdDraw.setPenRadius();
        double midx = rect.xmin() + 0.5 * (rect.xmax() - rect.xmin());
        double midy = rect.ymin() + 0.5 * (rect.ymax() - rect.ymin());
        StdDraw.rectangle(midx, midy, 0.5 * rect.width(), 0.5 * rect.height() );

        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        StdDraw.point(point.x(), point.y());
        StdDraw.pause(5000);
        StdDraw.clear();
    }

}
