import java.util.Iterator;

import edu.princeton.cs.algs4.StdOut;

/**
 **The <tt>Deque</tt> class models the data structure for selecting
 *and removing the header and trailer elements from a doubly Linked List.
 *  header -> ITEM -> ITEM -> ... -> ITEM -> trailer
 * <p>
 * Reference:
 * http://coursera.cs.princeton.edu/algs4/assignments/queues.html
 * @author Andrew Lienhard.
 * February 10th, 2015
 * @param <Item>
 */
public class Deque<Item> implements Iterable<Item> {

    /** pointer to the header element. **/
    private Node header;

    /** pointer to the trailer element. **/
    private Node trailer;

    /** Number of items in deck2. **/
    private int numItems;

    /**
     *  construct an empty deck2.
     * @return
     */
    public Deque() {
        header = new Node();
        trailer = new Node();
        header.setNext(trailer);
        trailer.setPrevious(header);
        numItems = 0;
    } 

    /**
     *  is the deque empty?
     * @return
     */
    public boolean isEmpty()  {
        return (numItems == 0);
    }               

    /**
     * return the number of items on the deque
     * @return
     */
    public int size()  {
        return numItems;
    }  

    /**
     * insert the item at the front
     * @param item
     */
    public void addFirst(Item item) {
        if (null == item) {
            throw new java.lang.NullPointerException();
        }
        Node newFirst = new Node();
        newFirst.setItem(item);
        newFirst.setPrevious(header);
        newFirst.setNext(header.getNext());
        header.getNext().setPrevious(newFirst);
        header.setNext(newFirst);
        
       
        numItems++;
    } 

    /**
     * insert the item at the end
     * @param item
     */
    public void addLast(Item item) {
        if (null == item) {
            throw new java.lang.NullPointerException();
        }
       
        Node newLast = new Node();
        Node previousLast = trailer.getPrevious();
        newLast.setPrevious(previousLast);
        newLast.setNext(trailer);
        newLast.setItem(item);
        previousLast.setNext(newLast);
        trailer.setPrevious(newLast);
        numItems++;
    }

    /**
     * delete and return the item at the front
     * @return
     */
    public Item removeFirst() {
        if (isEmpty()) {
            throw new java.util.NoSuchElementException();
        }
        Node previousFirst = header.getNext();
        Item item = previousFirst.getItem();
        Node second = previousFirst.getNext();
        if (null != second) {
            header.setNext(second);
            second.setPrevious(header);
        }
        numItems--;
        return item;
    }          

    /**
     * delete and return the item at the end
     * @return
     */
    public Item removeLast() {
        if (isEmpty()) {
            throw new java.util.NoSuchElementException();
        }
        Node tmpLast = trailer.getPrevious();
        Item item = tmpLast.getItem();
        Node prev = trailer.getPrevious();
        if (prev.getPrevious() != null) {
            trailer.setPrevious(prev.getPrevious());
            prev.getPrevious().setNext(trailer);
        }
        numItems--;
        return item;
    }

    /**
     * The Internal data structure for the deck2.
     *
     */
    private class Node {
        private Item item;
        private Node next;
        private Node previous;
        public Item getItem() { return item; }
        public void setItem(Item in) { item = in; }
        public Node getNext() { return next; }
        public void setNext(Node in) { next = in; }
        public Node getPrevious() { return previous; }
        public void setPrevious(Node in) { previous = in; }
    }

    /**
     *  return an iterator over items in order from front to end
     */
    public Iterator<Item> iterator()  {
        return new DequeIterator();
    }  

    private class DequeIterator implements Iterator<Item> {
        /** current node. **/
        private Node current;
        
        public DequeIterator() {
            current = header.getNext();
        }

        /**
         * 
         */
        public boolean hasNext() {
            return current.getNext() != null;
        }

        /**
         * 
         */
        public Item next() {
            if (current.getNext() == null) {
                throw new java.util.NoSuchElementException();
            }
            Item item = current.getItem();
            current  = current.getNext();
            return item;
        }

        /** 
         * Not supported.
         */
        public void remove() {
            throw new java.lang.UnsupportedOperationException();
        }
    }

    /**
     * Unit testing.
     * @param args
     */
    public static void main(String[] args)  {
        
        Deque<String> deck = new Deque<String>();
        deck.addFirst("Love");
        deck.addLast("You");
        deck.addFirst("I");
        deck.addFirst("Hello");
        Iterator<String> iter = deck.iterator();
        while (iter.hasNext()) {
            StdOut.println(iter.next());
        }
      
        StdOut.println("All Done");
        Deque<Integer> deck2 = new Deque<Integer>();
        
        deck2.addLast(101);
        deck2.removeFirst();
        deck2.addFirst(34);
        deck2.addFirst(67);
        deck2.addFirst(29);
        deck2.addFirst(765);
        deck2.removeLast();
        deck2.removeLast();
        deck2.addLast(43);
        deck2.addLast(83);
        deck2.addLast(84);
        deck2.addLast(546);
        deck2.addLast(356);
        deck2.removeLast();
        deck2.removeFirst();
        deck2.removeLast();
        deck2.removeLast();
        deck2.removeLast();
        deck2.removeLast();
        //deck2.removeLast();
        Iterator<Integer> iter2 = deck2.iterator();
        while (iter2.hasNext()) {
            int num = iter2.next();
            StdOut.println(num);
        }
        StdOut.println("All Done");
        
        deck2.addFirst(1); // 1
        StdOut.println(deck2.removeLast()); // 1=out, next last =null
        deck2.addFirst(3); // 3
        deck2.addFirst(4); // 4, 3
        iter2 = deck2.iterator();
        while (iter2.hasNext()) {
            int num = iter2.next();
            StdOut.println(num);
        }
        StdOut.println("Finished.");
    }
}
