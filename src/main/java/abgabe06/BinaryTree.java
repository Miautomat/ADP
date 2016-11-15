package abgabe06;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Mieke Narjes, David Hoeck, Luka Hartwig the basic functions of the
 *         tree are from:
 *         http://www.newthinktank.com/2013/03/binary-tree-in-java/ class to
 *         represent a Binary tree
 */
public class BinaryTree {
	private Node root;
	private Set<Integer> pathLength;
	private Set<Integer> nodes;

	/**
	 * static inner class for elements of the tree
	 */
	static class Node {
		int data;
		Node left;
		Node right;

		public Node(int data) {
			this.data = data;
			left = null;
			right = null;
		}
	}

	public BinaryTree() {
		this.root = null;
		pathLength = new HashSet<>();
		nodes = new HashSet<>();
	}

	/**
	 * traverses via through the tree and counts the depth when it reaches a
	 * leave.
	 * 
	 * @param root
	 * @param count
	 */
	public void traverse(Node root, int count) {
		if (root != this.root)
			count++;
		if (root.left != null) {
			traverse(root.left, count);
		}
		if (root.right != null) {
			traverse(root.right, count);
		}
		if (root.left == null && root.right == null) {
			pathLength.add(count);
		}
	}

	/**
	 * @return max Path length
	 */
	public int getMax() {
		traverse(root, 0);
		return pathLength.stream().max(Comparator.naturalOrder()).get();
	}

	/**
	 * @return min Path length
	 */
	public int getMin() {
		traverse(root, 0);
		return pathLength.stream().min(Comparator.naturalOrder()).get();
	}

	/**
	 * @return avg Path length
	 */
	public double getAverage() {
		traverse(root, 0);
		return pathLength.stream().mapToInt(x -> x).average().getAsDouble();
	}

	/**
	 * Iterates through Tree searching the Node with id. Turns left if id <
	 * current.data and right is id > current.data
	 * 
	 * Returns true if element id exists in tree
	 * 
	 * @param id
	 * @return boolean
	 */
	public boolean find(int id) {
		Node current = root;
		while (current != null) {
			if (current.data == id) {
				return true;
			} else if (current.data > id) {
				current = current.left;
			} else {
				current = current.right;
			}
		}
		return false;
	}

	/**
	 * iterates over tree until it found the element id Deletes element, updates
	 * patent.left/parent.right
	 * 
	 * @param id
	 * @return boolean
	 */
	public boolean delete(int id) {
		Node parent = root;
		Node current = root;
		boolean isLeftChild = false;

		nodes.remove(id);

		while (current.data != id) {
			parent = current;
			if (current.data > id) {
				isLeftChild = true;
				current = current.left;
			} else {
				isLeftChild = false;
				current = current.right;
			}
			if (current == null) {
				return false;
			}
		}
		// at this point we found the node to be deleted
		// Case 1: if node to be deleted has no children
		if (current.left == null && current.right == null) {
			if (current == root) {
				root = null;
			}
			if (isLeftChild == true) {
				parent.left = null;
			} else {
				parent.right = null;
			}
		}
		// Case 2 : if node to be deleted has only one child
		else if (current.right == null) {
			if (current == root) {
				root = current.left;
			} else if (isLeftChild) {
				parent.left = current.left;
			} else {
				parent.right = current.left;
			}
		} else if (current.left == null) {
			if (current == root) {
				root = current.right;
			} else if (isLeftChild) {
				parent.left = current.right;
			} else {
				parent.right = current.right;
			}
			// Case 3: node to be deleted has two children
		} else if (current.left != null && current.right != null) {
			Node successor = getSuccessor(current);
			if (current == root) {
				root = successor;
			} else if (isLeftChild) {
				parent.left = successor;
			} else {
				parent.right = successor;
			}
			// left subtree to the new node which replaced current
			successor.left = current.left;
		}

		return true;
	}

	/**
	 * @param deleteNode
	 * @return smallest element on right subtree from deleteNode
	 */
	public Node getSuccessor(Node deleteNode) {
		Node successor = null;
		Node successorParent = null;
		Node current = deleteNode.right;
		while (current != null) {
			successorParent = successor;
			successor = current;
			current = current.left;
		}
		/*
		 * successor.left is null now - successor.right might be null or !empty
		 * if it has a right child, it has to be attached to the
		 * successorParents left side
		 */
		if (successor != deleteNode.right) {
			successorParent.left = successor.right;
			successor.right = deleteNode.right;
		}
		return successor;
	}

	/**
	 * Inserts element id by creating new node. Iterates over tree to find
	 * correct spot to insert.
	 * 
	 * @param id
	 */
	public void insert(int id) {
		nodes.add(id);
		Node newNode = new Node(id);
		if (root == null) {
			root = newNode;
			return;
		}
		Node current = root;
		Node parent = null;
		while (true) {
			parent = current;
			if (id < current.data) {
				current = current.left;
				if (current == null) {
					parent.left = newNode;
					return;
				}
			} else {
				current = current.right;
				if (current == null) {
					parent.right = newNode;
					return;
				}
			}
		}
	}

	public void display() {
		if (root != null) {
			display(root.left);
			System.out.println(" " + root.data + " ");
			display(root.right);
		}
	}

	/**
	 * Recursion Displays branch by branch!
	 *
	 * @param root
	 */
	public void display(Node root) {
		if (root != null) {
			display(root.left);
			System.out.println(" " + root.data + " ");
			display(root.right);
		}
	}

	/**
	 * @return root.data
	 */
	public int getRoot() {
		return root.data;
	}

	/**
	 * @return root
	 */
	public Node getRootNode() {
		return root;
	}

	/**
	 * @return nodes
	 */
	public Set<Integer> getNodes() {
		return nodes;
	}

	/**
	 * @return boolean
	 */
	public boolean isEmpty() {
		return nodes.isEmpty();
	}

}