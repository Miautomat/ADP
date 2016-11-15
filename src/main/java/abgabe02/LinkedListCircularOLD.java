package abgabe02;

import java.util.NoSuchElementException;

/**
 * @author Mieke Narjes, Luka Hartwig, David Hoeck
 * 
 *         the variables current and temp are used in nearly every method. As
 *         they shouldn't be remembered outside of each method we decided to
 *         declare them within the method-body
 */
public class LinkedListCircularOLD<E> implements ICircularBuffer<E> {

	private Node<E> root;
	private int numberOfNodes = 0;

	@Override
	public void enqueue(E element) {
		add(element);
	}

	@Override
	public E dequeue() {
		return delete(0);
	}

	/**
	 * deletes all elements of the list
	 */
	@Override
	public void clear() {
		numberOfNodes = 0;
		root = null;
	}

	@Override
	public E first() {
		return get(0);
	}

	/**
	 * Static generic inner class to define Node-elements of the linked List.
	 */
	private static class Node<E> {
		E data;
		Node<E> nextNode;

		Node(E data) {
			this.data = data;
		}
	}

	public void add(E data) { addNode(data); }

	public E delete(int index) { return deleteNode(index); }

    /**
     * uses the private Method getNode to get the data of the element on the given index
     * @param index
     * @return E
     */
    public E get(int index) {
        return getNode(index).data;
    }

	/**
	 * returns true if the list has no elements
	 * 
	 * @return boolean
	 */
	public boolean isEmpty() {
		return root == null;
	}

    public void insert(E data, int index) { insertNode(data, index); }

	/**
	 * adds a new Node to the end of the List with the data given by the
	 * parameter.
	 * 
	 * @param data
	 */
	private void addNode(E data) {
		Node<E> node = new Node<>(data);
		Node<E> current;

		if (isEmpty()) {
			root = node;
			node.nextNode = root;
		} else {
			current = getNode(-1);
			current.nextNode = node;
			node.nextNode = root;
		}
		numberOfNodes++;
	}

    /**
     * removes the element on the index and gives it back
     * NumberOfNodes decreases
     * @param index
     * @return Node<E>
     */
    private E deleteNode(int index) {
        Node<E> before = null;
        Node<E> deleted = null;

        if (numberOfNodes == 1) {
            deleted = root;
            clear();
        }else{
            before = getNode(index - 1);

            deleted = before.nextNode;
            before.nextNode = before.nextNode.nextNode;
            if (index == 0) {
                root = before.nextNode;
            }
        }
        numberOfNodes--;
        return deleted.data;
    }

    /**
     * searches for the Node on the index and gives it back
     * positive and negative index allowed
     *
     * @param index
     * @return Node<E>
     */
    private Node<E> getNode(int index) throws NoSuchElementException {
        Node<E> current = root;

        if (numberOfNodes == 0) {
            throw new NoSuchElementException();
        }

        
        /**
         * @see http://stackoverflow.com/a/5385053
         *  the operation of the double modulo, allows us to give accept negativ indizes
         */
        index = (index % numberOfNodes + numberOfNodes) % numberOfNodes;

        int i = 0;
        while (index > i) {
            current = current.nextNode;
            i++;
        }
        return current;
    }

	/**
	 * Inserts a new Node with the data given by the parameter to the position
	 * of the wanted index.
	 * 
	 * @param data
	 * @param index
	 */
	private void insertNode(E data, int index) {
		Node<E> temp = null;
		Node<E> current;

		if (root == null) {
			addNode(data);
		} else {
			Node<E> node = new Node<>(data);
			current = getNode(index);
			temp = current.nextNode; // pointer to node after current
			current.nextNode = node; // put node inbetween current and temp
			node.nextNode = temp;
		}
		numberOfNodes++;
	}

	/**
	 * iterates through the list and appends each Nodes data to a readable String
	 * @return String
	 */
	public String toString() {
		Node<E> current = root;
		StringBuilder sb = new StringBuilder();
		boolean notStart = false;
		do {
			if (notStart) {
				sb.append("--> |").append(current.data).append("|");
			} else {
				sb.append("|").append(current.data).append("|");
				notStart = true;
			}
			current = current.nextNode;
		} while (current != root);
		return sb.toString();
	}

	/**
	 * iterates through the List and appends each Node-data to a readable
	 * String. Stops after n Iterations.
	 * 
	 * @param n
	 * @return String
	 */
	public String toString(int n) {
		Node<E> current = root;
		StringBuilder sb = new StringBuilder();
		boolean notStart = false;
		for (int i = 0; i < n; i++) {
			if (notStart) {
				sb.append("--> |").append(current.data).append("|");
			} else {
				sb.append("|").append(current.data).append("|");
				notStart = true;
			}
			current = current.nextNode;
		}
		return sb.toString();
	}

}
