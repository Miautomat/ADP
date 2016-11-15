package abgabe02;

import javax.xml.datatype.DatatypeConfigurationException;

/**
 * 
 * @author Mieke Narjes, Luka Hartwig, David Hoeck
 *
 */
public class CircularBuffer<E> implements ICircularBuffer<E> {

	private ICircularBuffer<E> strategy;
	private int capacity;
	private int numberOfELements = 0;

	public CircularBuffer(int size,String label) throws DatatypeConfigurationException {
		this.capacity = size;
		if(label == "A"){
			this.strategy = new ArrayCircular<>(size);
		}else if(label == "L") {
			this.strategy = new LinkedListCircular<>();
		} else {
			throw new DatatypeConfigurationException("wrong buffertype. Please type 'L' for a linkedlistBuffer and 'A' for arrayBuffer");
		}
	}

	public void setStrategy(ICircularBuffer<E> strategy) {
		this.strategy = strategy;
	}

	@Override
	public void enqueue(E element) {

		assert (numberOfELements < capacity);
		strategy.enqueue(element);

		numberOfELements++;

	}

	@Override
	public E dequeue() {



		E elem = (E) strategy.dequeue();

		numberOfELements--;

		return elem;

	}

	@Override
	public void clear() {
		strategy.clear();
	}

	@Override
	public E first() {
		return (E) strategy.first();
	}
}
