package abgabe02;

/**
 * 
 * @author Mieke Narjes, Luka Hartwig, David Hoeck<br><br>
 * 
 *<b>fixed size ringbuffer (FIFO)</b><br><br>
 *it offers the following methods:<br>
 * <ul><li>enqueue: to add new elements to the queue.
 * <li>dequeue: to consume the first element of the queue and remove it.
 * <li>clear:   to empty the queue
 * <li>first:   to return the first element of the queue without removing it.</ul>
 *    
 * @param <E>
 */
public interface ICircularBuffer<E> {
	
	/**
	 *<b>Preconditions:</b><ul><li>buffer has capacity left.</ul>
	 *<b>Postconditions:</b><ul><li>the added element is the new tail of the queue.  
	 *<li>no overwriting if the buffer is full.</ul>
	 *    
	 */
	 void enqueue(E element);
	
	
	/**
	 *<b>Preconditions:</b><ul><li>buffer is not empty.</ul>
	 *<b>Postconditions:</b><ul><li>always returns the first element of the queue.
	 *<li>head is removed.
	 *<li>second element is set as new head.</ul>
	 */          
	 E dequeue();
	
	
	/**
	 * <b>Postconditions:</b><ul><li>buffer is empty</ul>
	 */
	 void clear();
	
	
	/**
	 * <b>Preconditions:</b><ul><li>buffer is not empty.</ul>
	 * <b>Postconditions:</b><ul><li>always returns the first element of the queue.</ul>
	 */
  E first();

}
