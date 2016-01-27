import java.util.Iterator;

/**
 **The <tt>Subset</tt> class provides an input mechanism for the shuffling
 *data.
 * <p>
 * Reference:
 * http://coursera.cs.princeton.edu/algs4/assignments/queues.html
 * @author Andrew Lienhard.
 * February 10th, 2015
 * @param <Item>
 */
public class Subset {
    public static void main(String[] args) {
        RandomizedQueue<String> rq = new RandomizedQueue<String>();
        while (!StdIn.isEmpty())
        {
            String s = StdIn.readString();
            rq.enqueue(s);
        }
        int max = 0;
        try {
            max = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            System.err.println("Argument" + args[0] + " must be an integer.");
        }

        Iterator<String> iter = rq.iterator();
        int count = 0;
        while (iter.hasNext() && count++ < max) {
            String obj = iter.next();
            StdOut.println(obj);
        }

    }
}
