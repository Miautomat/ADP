package abgabe02;

import java.util.Arrays;

/**
 * 
 * @author Mieke Narjes, Luka Hartwig, David Hoeck
 *
 * generic circularBuffer based on array.
 */
public class ArrayCircular<E> implements ICircularBuffer<E> {

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
    public ArrayCircular(int length) {
        buffer = (E[]) new Object[length];
        tail = 0;
        head = 0;
        numberOfElements = 0;
    }

    
    @Override
    public void enqueue(E element) {
        assert numberOfElements != buffer.length;

        this.add(element);

        assert (element == buffer[(tail - 1) % buffer.length]);
    }

    @Override
    public E first() {
        assert (numberOfElements != 0);
        return this.peek();
    }

    @Override
    public void clear() {
        this.deleteAll();
        assert head == tail;
    }

    @Override
    public E dequeue() {
        assert numberOfElements > 0;
        E result;

        result = this.remove();

        assert result != null;
        assert numberOfElements >= 0;
        return result;
    }

    public boolean isEmpty() {
        return numberOfElements == 0;
    }

    private void add(E element) {
        buffer[tail % buffer.length] = element;
        tail++;
        numberOfElements++;
    }

    private E remove() {
        E removedElem = this.peek();
        head = (head + 1) % buffer.length;
        numberOfElements--;
        return removedElem;
    }

    private void deleteAll() {
        head = 0;
        tail = 0;
        numberOfElements = 0;
    }

    private E peek() {
        return buffer[head];
    }

    /**
     * returns an useful image of the buffer as string.
     */
    public String toString() {
        return String.format("Queue:  |   size=%-3s|   head=%-3s|   tail=%-5s-->    %s", buffer.length, head, tail,
                Arrays.toString(buffer));
    }
}
