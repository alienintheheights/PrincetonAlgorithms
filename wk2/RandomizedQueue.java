import java.util.Iterator;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.Stopwatch;

/**
 *The <tt>RandomizedQueue</tt> class models the data structure for selecting
 *random elements from the queue.
 * <p>
 * Reference:
 * http://coursera.cs.princeton.edu/algs4/assignments/queues.html
 * @author Andrew Lienhard.
 * February 10th, 2015
 * @param <Item>
 */
public class RandomizedQueue<Item> implements Iterable<Item> {

    /** internal data array. **/
    private Item[] s;

    /** Number of items in queue. **/
    private int numItems;

    /** Number of items in queue. **/
    private int lastEnd;

    /**
     * Construct an empty randomized queue
     */
    public RandomizedQueue() {
        s = initArray(2);
        lastEnd = 0;
    } 

    /**
     * is the queue empty?
     * @return
     */
    public boolean isEmpty() {
        return numItems == 0;
    }

    /**
     * Return the number of items on the queue
     * @return
     */
    public int size() {
        return numItems;
    }

    /**
     * add the item
     * @param item
     */
    public void enqueue(Item item) {
        if (null == item) {
            throw new java.lang.NullPointerException();
        }
        s[lastEnd++] = item; // stomp last end value if remove has fired
        numItems++;
        if (numItems > 0 && numItems == s.length) {
            resize(2*numItems);
        }
    }

    /**
     * delete and return a random item
     * @return
     */
    public Item dequeue() {
        if (isEmpty()) {
            throw new java.util.NoSuchElementException();
        }
        int rand = selectRandomNode();
        Item item = s[rand];
        s[rand] = s[lastEnd-1];
        s[lastEnd-1] = null;
        numItems--;
        lastEnd--;
        if (numItems > 0 && numItems == s.length/4) {
            resize(s.length/2);
        }
        return item;

    }

    /**
     * return (but do not delete) a random item
     * @return
     */
    public Item sample() {
        if (isEmpty()) {
            throw new java.util.NoSuchElementException();
        }
        return s[selectRandomNode()];
    }

    /**
     * Fetches random element in o(N) time.
     * TODO needs to be constant time
     * @return
     */
    private int selectRandomNode() {
        int rand = StdRandom.uniform(numItems);
        return rand;
    }

    /**
     * Rebuilds array. Omits nulls.
     * @param capacity
     */
    private void resize(int capacity) {
        Item[] temp = initArray(capacity);
        int count = 0;
        // build sans removals
        for (int i = 0; i < s.length; i++) {
            if (null != s[i]) {
                temp[count++] = s[i];
            }
        }
        s = temp;
        lastEnd = count;
    }

    //@SuppressWarnings("unchecked")
    private Item[] initArray(int size) {
        return (Item[]) new Object[size];
    }

    /**
     *  return an iterator over items in order from front to end
     */
    public Iterator<Item> iterator()  {
        return new RandomizedIterator();
    }  

    private class RandomizedIterator implements Iterator<Item> {
        /** current node. **/
        private Item[] randomizedArray;

        private int currentIndex = 0;

        public RandomizedIterator() {
            // initial a random array
            resize(numItems);
            randomizedArray = s;
            StdRandom.shuffle(randomizedArray);

        }

        /**
         * 
         */
        public boolean hasNext() {
            return (currentIndex < randomizedArray.length);
        }

        /**
         * 
         */
        public Item next() {
            if (currentIndex >= randomizedArray.length) {
                throw new java.util.NoSuchElementException();
            }
            Item nextItem = randomizedArray[currentIndex++];
            return nextItem;
        }

        /** 
         * Not supported.
         */
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    /**
     * unit testing
     * @param args
     */
    public static void main(String[] args) {

        RandomizedQueue<String> rq = new RandomizedQueue<String>();
        try {
            rq.dequeue();
            rq.sample();
        } catch (java.util.NoSuchElementException e) {
            StdOut.println("Caught exception " + e);
        }
        Iterator<String> iter0 = rq.iterator();
        while (iter0.hasNext()) {
            String obj = iter0.next();
            StdOut.println(obj);
        }
        try {
            iter0.next();
        } catch (java.util.NoSuchElementException e) {
            StdOut.println("Caught exception " + e);
        }

        
        
        StdOut.println("End empty test");
        Stopwatch timer = new Stopwatch();
        rq = new RandomizedQueue<String>();
        rq.enqueue("Hello");
        rq.dequeue();
        rq.enqueue("I");
        rq.enqueue("I1");
        rq.enqueue("I2");
        rq.enqueue("I3");
        rq.enqueue("I4");
        rq.enqueue("I5");
        rq.enqueue("I6");
        rq.enqueue("I7");
        rq.enqueue("I8");
        rq.enqueue("I9");
        rq.dequeue(); 
        rq.enqueue("Love");
        rq.dequeue();
        rq.dequeue();
        rq.dequeue();
        rq.enqueue("You"); 
        rq.enqueue("And");
        rq.enqueue("Only");
        rq.dequeue(); // I, I1, I6, You, And


        StdOut.println("Random sample " + rq.sample());


        Iterator<String> iter = rq.iterator();
        while (iter.hasNext()) {
            String obj = iter.next();
            StdOut.println(obj);
        }

        StdOut.println("Run completed in " + timer.elapsedTime() + " seconds");

        int size = 1000;
        for (int k = 1; k < 10000; k = 2*k) {
            // Stock the Queue
            timer = new Stopwatch();
            RandomizedQueue<Integer> rq2 = new RandomizedQueue<Integer>();
            for (int i = 0; i < k*size; i++) {
                rq2.enqueue(Integer.valueOf(i) );
            }
            // Dequeue it
            timer = new Stopwatch();
            for (int j = 0; j < k*size; j++) {
                rq2.dequeue();
            }   
            StdOut.println("N= " 
                    + k*size 
                    + "\tDequeue Ops T(N)=" 
                    + timer.elapsedTime());
        }
    }
}
