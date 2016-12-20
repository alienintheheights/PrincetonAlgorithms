import static org.junit.Assert.*;

import java.util.Iterator;

import org.junit.Test;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;


/**
 * @author andrew
 */
public class KdTreeTest {
    
    private static final String CIRCLE_TEST_FILE = "PATH_TO/data/kdtree/circle10.txt";
    private static final String CIRCLE_TEST_FILE2 = "PATH_TO/data/kdtree/circle10k.txt";

    /**
     * Test method for {@link KdTree#insert(edu.princeton.cs.algs4.Point2D)}.
     */
    @Test
    public void testInsert() {
        // unit testing of the methods (optional) 
        KdTree tree = new KdTree();
        assertTrue(tree.isEmpty());

        tree.insert(new Point2D(0.7, 0.2));
        tree.insert(new Point2D(0.7, 0.1));
        tree.insert(new Point2D(0.5, 0.4));
        tree.insert(new Point2D(0.2, 0.3));
        tree.insert(new Point2D(0.2, 0.5));
        tree.insert(new Point2D(0.4, 0.7));
        tree.insert(new Point2D(0.9, 0.6));
        tree.insert(new Point2D(0.7, 0.2)); // dup
        tree.draw();
        
        int size = tree.size();
        assertEquals(7, size);
   }
    

    /**
     * Test method for {@link KdTree#insert(edu.princeton.cs.algs4.Point2D)}.
     */
    @Test
    public void testInsert2() {
        // unit testing of the methods (optional) 
        KdTree tree = new KdTree();
        assertTrue(tree.isEmpty());
        
        tree.insert(new Point2D(0.5, 0.7)); // v: x (=) right
        tree.insert(new Point2D(0.5, 0.8)); // h: x (=) right
        tree.insert(new Point2D(0.7, 0.8)); // v: y (=) right
        tree.insert(new Point2D(0.8, 0.8)); // h: x right
        
        int size = tree.size();
        assertEquals(4, size);
    }

    /**
     * Test method for {@link KdTree#isEmpty()}.
     */
    @Test
    public void testIsEmpty() {
        KdTree tree = new KdTree();
        assertTrue(tree.isEmpty());
    }

    /**
     * Test method for {@link KdTree#size()}.
     */
    @Test
    public void testSize() {
        KdTree tree = new KdTree();
        assertEquals(0, tree.size());
    }
    

    /**
     * Test method for {@link KdTree#size()}.
     */
    @Test
    public void testSize2() {
        KdTree tree = new KdTree();
        
        tree.insert(new Point2D(0.7, 0.2));
        tree.insert(new Point2D(0.9, 0.6));
        tree.insert(new Point2D(0.7, 0.2)); // dup
        assertEquals(2, tree.size());
    }
    
    @Test
    public void testContains() {
        KdTree tree = new KdTree();
        tree.insert(new Point2D(0.9, 0.5));
        tree.insert(new Point2D(0.6, 0.4));
        tree.insert(new Point2D(0.0, 0.9));
        tree.insert(new Point2D(0.4, 0.8));
     //   assertEquals(tree.nearest(new Point2D(0.1, 0.1)), new Point2D(0.6, 0.4)); //  ==>  (0.6, 0.4)
      //  tree.range(new RectHV(0.0, 0.3, 1.0, 0.3));
        tree.insert(new Point2D(0.0, 0.2));
        tree.insert(new Point2D(0.0, 1.0));
        //tree.range(new RectHV(0.4, 0.1,  0.7, 0.6));
        assertFalse(tree.contains(new Point2D(0.0, 0.5))); // ==>  true        
    }

   
    /**
     * Test method for {@link KdTree#contains(edu.princeton.cs.algs4.Point2D)}.
     */
    @Test
    public void testContains2() {
        KdTree tree = new KdTree();
        tree.insert(new Point2D(0.1, 0.2));
        tree.insert(new Point2D(0.3, 0.5));
        tree.insert(new Point2D(0.4, 0.9));
        assertTrue(tree.contains(new Point2D(0.1, 0.2)));
        assertTrue(tree.contains(new Point2D(0.3, 0.5)));
        assertTrue(tree.contains(new Point2D(0.4, 0.9)));
        assertFalse(tree.contains(new Point2D(0.3, 0.9)));
    }
    
   
    /**
     * Test method for {@link KdTree#nearest(edu.princeton.cs.algs4.Point2D)}.
     */
    @Test
    public void testNearest() {
        KdTree tree = initData(CIRCLE_TEST_FILE);
        Point2D circleTestPoint = new Point2D(0.81, 0.30);
        Point2D nearest = tree.nearest(circleTestPoint);
        
        assertEquals(nearest, new Point2D(0.975528, 0.345492));
        
    }
    

    /**
     * Test method for {@link KdTree#nearest(edu.princeton.cs.algs4.Point2D)}.
     */
    @Test
    public void testNearest2() {
        KdTree tree = initData(CIRCLE_TEST_FILE2);
        Point2D searchPoint = new Point2D(0.203125, 0.880859375);
        Point2D expectedResult = new Point2D(0.206107, 0.904508);
        
        assertEquals(expectedResult, tree.nearest(searchPoint));
       
    }
    
    /**
     * Test method for {@link KdTree#nearest(edu.princeton.cs.algs4.Point2D)}.
     */
    @Test
    public void testNearest3() {
        KdTree tree = initData(CIRCLE_TEST_FILE);
        Point2D circleTestPoint = new Point2D(0.49, 0.9);
        Point2D expectedResult = new Point2D(0.5, 1.0);
        Point2D nearest = tree.nearest(circleTestPoint);
        
        assertEquals(nearest, expectedResult);
    }
    
    /**
     * Test method for {@link KdTree#range(edu.princeton.cs.algs4.Point2D)}.
     */
    @Test
    public void testRange() {
        KdTree tree = initData(CIRCLE_TEST_FILE);
        RectHV rect = new RectHV(0.4, 0.4, 0.5, 0.5);
        Iterable<Point2D> rangeIterable = tree.range(rect);
        Iterator<Point2D> iter = rangeIterable.iterator();
        assertFalse(iter.hasNext());
        
   }
    
    
    /**
     * Test method for {@link KdTree#range(edu.princeton.cs.algs4.Point2D)}.
     */
    @Test
    public void testRange2() {
        KdTree tree = initData(CIRCLE_TEST_FILE);
        RectHV rect =  new RectHV(0.0, 0.0, 0.5, 0.5);
        Iterable<Point2D> rangeIterable = tree.range(rect);
        Iterator<Point2D> iter = rangeIterable.iterator();
        assertTrue(iter.hasNext());
   }

    /**
     * Read data
     * @param args
     * @return
     */
    public KdTree initData(String filename) {
       // StdDraw.clear();
        In in = new In(filename);
        KdTree tree = new KdTree();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            tree.insert(p);
        }
        tree.draw();
        //StdDraw.pause(1500);
        return tree;
    }
    
}

