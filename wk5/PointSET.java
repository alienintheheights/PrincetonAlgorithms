import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdDraw;

/**
 * PointSET represents a set of points in the unit square. 
 * Implement the following API by using a red-black BST 
 * (using either SET from algs4.jar or java.util.TreeSet).
 * 
 * Corner cases.  
 * Throw a java.lang.NullPointerException if any argument is null. 
 * 
 * Performance requirements.  
 * Your implementation should support insert() and contains() in time 
 * proportional to the logarithm of the number of points in the set in the 
 * worst case; it should support nearest() and range() in time proportional 
 * to the number of points in the set.

 * @author andrew
 */
public class PointSET {
    
    private SET<Point2D> squidly;
    
    public PointSET() {
        // construct an empty set of points 
        squidly = new SET<Point2D>();
    }
    
    public boolean isEmpty() {
        return squidly.isEmpty();
        // is the set empty? 
    }
    
    public int size() {
        return squidly.size();
        // number of points in the set 
    }
    
    public void insert(Point2D p) {
        // add the point to the set (if it is not already in the set)
        squidly.add(p);
    }
    
    public boolean contains(Point2D p) {
        return squidly.contains(p);
        // does the set contain point p? 
    }
    
    public void draw() {
        // draw all points to standard draw 
        Iterator<Point2D> pointIter = squidly.iterator();
        while (pointIter.hasNext()) {
            Point2D item = pointIter.next();
            StdDraw.point(item.x(), item.y());
         }
    }
    
    public Iterable<Point2D> range(RectHV rect) {
        List<Point2D> matches = new ArrayList<Point2D>();
        Iterator<Point2D> pointIter = squidly.iterator();
        while (pointIter.hasNext()) {
           Point2D item = pointIter.next();
           if (rect.contains(item))
               matches.add(item);
        }
        return matches;
        // all points that are inside the rectangle 
    }
    
    public Point2D nearest(Point2D p) {
        Iterator<Point2D> pointIter = squidly.iterator();
        double distance = Double.MAX_VALUE;
        Point2D closestPoint = null;
        while (pointIter.hasNext()) {
           Point2D item = pointIter.next();
           double currentDistance = item.distanceSquaredTo(p);
           if (currentDistance < distance) {
               distance = currentDistance;
               closestPoint = item;
           }
        }
        return closestPoint;
        // a nearest neighbor in the set to point p; null if the set is empty 
    }

    public static void main(String[] args) {
        // unit testing of the methods (optional) 
    }
}