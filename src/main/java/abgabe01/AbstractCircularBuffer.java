package abgabe01;

public abstract class AbstractCircularBuffer<E> implements ICircularBuffer<E> {

	private E[] buffer;

	/**
	 * index of first element of the queue
	 */
	private int head;
	/**
	 * index of last element of the queue
	 */
	private int tail;
	/**
	 * number of containing elements
	 */
	private int numberOfElements;

	@SuppressWarnings("unchecked")
	public AbstractCircularBuffer(int length) {
		buffer = (E[]) new Object[length];
		tail = 0;
		head = 0;
		numberOfElements = 0;
	}

	@Override
	public void enqueue(E element) {
		assert (tail + 1) % buffer.length != head;
		this.add(element);
		assert (element == buffer[head]);
	}

	private void add(E element) {
	}

	@Override
	public E dequeue() {
		E result;
		assert (head != tail);
		result = this.remove();
		assert (result != null);
		return result;
	}

	private E remove() {
		return this.first();
	}

	@Override
	public void clear() {
		this.deleteAll();
		assert (head == tail);
	}

	private void deleteAll() {
	}

	@Override
	public E first() {
		assert (tail + 1) % buffer.length != head;
		this.peek();
		return null;
	}

	private void peek() {
	}

	@Override
	public E[] getAll() {
		return buffer.clone();
	}

}
