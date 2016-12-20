import static org.junit.Assert.*;

import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

/**
 * 
 */

/**
 * @author andrew
 *
 */
public class PointSETTest {
    
    private static final String CIRCLE_TEST_FILE = "PATH_TO/data/kdtree/circle10.txt";
    private static final String CIRCLE_TEST_FILE2 = "PATH_TO/data/kdtree/circle10k.txt";


    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
    }

    /**
     * Test method for {@link PointSET#isEmpty()}.
     */
    @Test
    public void testIsEmpty() {
        PointSET pset = new PointSET();
        assertTrue(pset.isEmpty());
    }

    /**
     * Test method for {@link PointSET#size()}.
     */
    @Test
    public void testSize() {
        PointSET pset = new PointSET();
        assertEquals(0, pset.size());
    }
    
    /**
     * Test method for {@link PointSET#size()}.
     */
    @Test
    public void testSize2() {
        PointSET pset = new PointSET();
        pset.insert(new Point2D(0.7, 0.2));
        pset.insert(new Point2D(0.9, 0.6));
        pset.insert(new Point2D(0.7, 0.2)); // dup
        assertEquals(2, pset.size());
    }

    /**
     * Test method for {@link PointSET#insert(edu.princeton.cs.algs4.Point2D)}.
     */
    @Test
    public void testInsert() {
        // unit testing of the methods (optional) 
        PointSET pset = new PointSET();
        assertTrue(pset.isEmpty());

        pset.insert(new Point2D(0.7, 0.2));
        pset.insert(new Point2D(0.7, 0.1));
        pset.insert(new Point2D(0.5, 0.4));
        pset.insert(new Point2D(0.2, 0.3));
        pset.insert(new Point2D(0.2, 0.5));
        pset.insert(new Point2D(0.4, 0.7));
        pset.insert(new Point2D(0.9, 0.6));
        pset.insert(new Point2D(0.7, 0.2)); // dup
        pset.draw();
        
        int size = pset.size();
        assertEquals(7, size);
    }

    /**
     * Test method for {@link PointSET#contains(edu.princeton.cs.algs4.Point2D)}.
     */
    @Test
    public void testContains() {
        PointSET pset = new PointSET();
        pset.insert(new Point2D(0.1, 0.2));
        pset.insert(new Point2D(0.3, 0.5));
        pset.insert(new Point2D(0.4, 0.9));
        assertTrue(pset.contains(new Point2D(0.1, 0.2)));
        assertTrue(pset.contains(new Point2D(0.3, 0.5)));
        assertTrue(pset.contains(new Point2D(0.4, 0.9)));
    }
    
    @Test
    public void testContains2() {
        PointSET set = new PointSET();
        set.insert(new Point2D(0.9, 0.5));
        set.insert(new Point2D(0.6, 0.4));
        set.insert(new Point2D(0.0, 0.9));
        set.insert(new Point2D(0.4, 0.8));
     //   assertEquals(tree.nearest(new Point2D(0.1, 0.1)), new Point2D(0.6, 0.4)); //  ==>  (0.6, 0.4)
      //  tree.range(new RectHV(0.0, 0.3, 1.0, 0.3));
        set.insert(new Point2D(0.0, 0.2));
        set.insert(new Point2D(0.0, 1.0));
        //tree.range(new RectHV(0.4, 0.1,  0.7, 0.6));
        assertFalse(set.contains(new Point2D(0.0, 0.5))); // ==>  true        
    }

    /**
     * Test method for {@link PointSET#range(edu.princeton.cs.algs4.RectHV)}.
     */
    @Test
    public void testRange() {
        PointSET pset = initData(CIRCLE_TEST_FILE);
        RectHV rect = new RectHV(0.4, 0.4, 0.5, 0.5);
        Iterable<Point2D> rangeIterable = pset.range(rect);
        Iterator<Point2D> iter = rangeIterable.iterator();
        assertFalse(iter.hasNext());
    }

    /**
     * Test method for {@link PointSET#range(edu.princeton.cs.algs4.RectHV)}.
     */
    @Test
    public void testRange2() {
        PointSET pset = initData(CIRCLE_TEST_FILE);
        RectHV rect =  new RectHV(0.0, 0.0, 0.5, 0.5);
        Iterable<Point2D> rangeIterable = pset.range(rect);
        Iterator<Point2D> iter = rangeIterable.iterator();
        assertTrue(iter.hasNext());
    }

    /**
     * Test method for {@link PointSET#nearest(edu.princeton.cs.algs4.Point2D)}.
     */
    @Test
    public void testNearest() {
        PointSET pset = initData(CIRCLE_TEST_FILE);
        Point2D circleTestPoint = new Point2D(0.81, 0.30);
        Point2D nearest = pset.nearest(circleTestPoint);
        
        assertEquals(nearest, new Point2D(0.975528, 0.345492));
    }

    /**
     * Test method for {@link PointSET#nearest(edu.princeton.cs.algs4.Point2D)}.
     */
    @Test
    public void testNearest2() {
        PointSET pset = initData(CIRCLE_TEST_FILE2);
        Point2D searchPoint = new Point2D(0.203125, 0.880859375);
        Point2D expectedResult = new Point2D(0.206107, 0.904508);
        
        assertEquals(expectedResult, pset.nearest(searchPoint));
    }

    /**
     * Read data
     * @param args
     * @return
     */
    public PointSET initData(String filename) {
        // TODO Auto-generated method stub
        In in = new In(filename);
        PointSET tree = new PointSET();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            tree.insert(p);
        }
        tree.draw();
        return tree;
    }
}
