package abgabe01;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/**
 * 
 *
 */
public class CircularBufferTest {

	CircularBuffer<Integer> two, five, ten;
	int[] testAry01;

	@Before
	public void setUp() throws Exception {
		two = new CircularBuffer<>(2);
		five = new CircularBuffer<>(5);
		ten = new CircularBuffer<>(10);
		testAry01 = new int[5];
		testAry01[0] = 1;
	}

	/**
	 * test if 
	 * <ol><li>number of elements increases by one, 
	 * <li>added element is new tail.</ol>
	 */
	@Test
	public void testEnqueue() {
		two.enqueue(1);
		assertArrayEquals(new Integer[] { 1, 0 }, two.getAll());
	}

	/**
	 * test if
	 * <ol><li>there is no overwriting if buffer is full.
	 * <li>---- assertionError expected-----</ol>
	 */
	@Test(expected = AssertionError.class)
	public void testEnqueueFull() {
		two.enqueue(1);
		two.enqueue(2);
		two.enqueue(3);
	}

	/**
	 * test if
	 * <ol><li>error is thrown if buffer is empty.</ol>
	 */
	@Test(expected = AssertionError.class)
	public void testDequeueEmpty() {
		two.dequeue();
	}

	/**
	 * test if 
	 * <ol><li>first element is returned.
	 * <li>head is removed and second element is set as new head.</ol>
	 */
	@Test
	public void testDequeue() {
		two.enqueue(1);
		two.enqueue(2);
		assertTrue(two.dequeue() == 1);
		assertTrue(two.first() == 2);
	}

	/**
	 * test if
	 * <ol><li>buffer is empty</ol>
	 */
	@Test
	public void testClear() {
		two.enqueue(1);
		two.enqueue(2);
		two.clear();
		assertArrayEquals(new Integer[2], two.getAll());
	}

	/**
	 * test if 
	 * <ol><li>error is thrown if buffer is empty.</ol>
	 */
	@Test(expected = AssertionError.class)
	public void testFirstEmpty() {
		two.first();
	}

	/**
	 * test if
	 * <ol><li>first element is returned.</ol>
	 */
	@Test
	public void testFirst() {
		two.enqueue(1);
		two.enqueue(2);
		assertTrue(two.first() == 1);
	}

}
