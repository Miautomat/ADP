package abgabe02;

import javax.xml.datatype.DatatypeConfigurationException;

public class PerformanceTest {

	public static void main(String[] args) throws DatatypeConfigurationException {
		Stopwatch watch = new Stopwatch();

		
		for (int x = 10; x <= 10_000_000; x *= 10) {
			
			CircularBuffer<Integer> bufferLinked = new CircularBuffer<>(x, "L");
			CircularBuffer<Integer> bufferArray = new CircularBuffer<>(x, "A");

			watch.start();
			for (int i = 0; i < x; i++) {
				bufferArray.enqueue(i);
			}
			watch.stop();
			System.out.println(watch.getTime() + "to enqueue " + x + " elements (array-based)");

			/**
			 * get the executiontime of the enqueue method from the
			 * linkedList-based buffer with 10 elements.
			 */
			watch.start();
			for (int i = 0; i < x; i++) {
				bufferLinked.enqueue(i);
			}
			watch.stop();
			System.out.println(watch.getTime() + "to enqueue " + x + " elements (Linked-List-based)");

			/**
			 * get the executiontime of the dequeue method from the array-based
			 * buffer with 10 elements.
			 */
			watch.start();
			for (int i = 0; i < x; i++) {
				bufferArray.dequeue();
			}
			watch.stop();
			System.out.println(watch.getTime() + "to dequeue " + x + " elements (Array-based)");

			/**
			 * get the executiontime of the dequeue method from the array-based
			 * buffer with 10 elements.
			 */
			watch.start();
			for (int i = 0; i < x; i++) {
				bufferLinked.dequeue();
			}
			watch.stop();
			System.out.println(watch.getTime() + "to dequeue " + x + " elements (Linked-List-based)");

			System.out.println("__________________________________________________________________________\n");
		}

		
		// just to refill the queue before testing the clear and first method
		CircularBuffer<Integer> bufferLinked = new CircularBuffer<>(10, "L");
		CircularBuffer<Integer> bufferArray = new CircularBuffer<>(10, "A");
		for (int i = 0; i < 10; i++) {
			bufferArray.enqueue(i);
			bufferLinked.enqueue(i);
		}

		/**
		 * get the executiontime of the first method from the array-based buffer
		 * with 10 elements. There is no need of more test at this point,
		 * because the size of the queue doesnt affect the runtime of the method
		 */
		watch.start();
		bufferArray.first();
		watch.stop();
		System.out.println(watch.getTime() + "to get the first element of a queue with 10 elements (array-based)");
	
		watch.start();
		bufferLinked.first();
		watch.stop();
		System.out.println(watch.getTime() + "to get the first element of a queue with 10 elements (linked-list-based)");


		/**
		 * get the executiontime of the clear method from the array-based buffer
		 * with 10 elements.There is no need of more test at this point, because
		 * the size of the queue doesnt affect the runtime of the method.
		 */
		watch.start();
		bufferArray.clear();
		watch.stop();
		System.out.println(watch.getTime() + "to clear a queue with 10 elements (array-based)");
		
		watch.start();
		bufferLinked.clear();
		watch.stop();
		System.out.println(watch.getTime() + "to clear a queue with 10 elements (linked-list-based)");
	}

}
