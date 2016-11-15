package abgabe02;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author Mieke Narjes, Luka Hartwig, David Hoeck
 *
 */
public class ArrayCircularTest {

	private ArrayCircular<Integer> testBuffer;

	@Before
	public void setUp() throws Exception {
		testBuffer = new ArrayCircular<>(1000);
	}

	@Test
	public void testEnqueueOnEmptyQueue() {
		testBuffer.enqueue(1);
		Integer expected = 1;
		Integer actual = testBuffer.first();
		assertEquals(expected, actual);
	}

	@Test
	public void testEnqueueWithOverflow() {
		for (int i = 0; i < 1000; i++) {
			testBuffer.enqueue(i);
		}
		for (Integer i = 0; i < 1000; i++) {
			assertEquals(i, testBuffer.dequeue());
		}
		for (int i = 0; i < 1000; i++) {
			testBuffer.enqueue(i);
		}
		for (Integer i = 0; i < 1000; i++) {
			assertEquals(i, testBuffer.dequeue());
		}
	}

	@Test(expected = AssertionError.class)
	public void testEnqueueFull() {
		for (int i = 0; i <= 1000; i++) {
			testBuffer.enqueue(i);
		}
	}

	@Test(expected = AssertionError.class)
	public void testDequeueEmpty() {
		testBuffer.dequeue();
	}

	@Test
	public void testClear() {
		for (int i = 0; i < 500; i++) {
			testBuffer.enqueue(i);
		}
		testBuffer.clear();
		testBuffer.enqueue(1);
		assertEquals(new Integer(1), testBuffer.first());
	}

	@Test(expected = AssertionError.class)
	public void testFirstEmpty() {
		testBuffer.first();
	}

	@Test
	public void testFirst() {
		testBuffer.enqueue(1);
		testBuffer.enqueue(2);
		Integer expected = 1;
		assertEquals(expected, testBuffer.first());
	}

}
