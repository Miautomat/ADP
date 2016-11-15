package abgabe05;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.PriorityQueue;

import abgabe05.princeton.BinaryIn;
import abgabe05.princeton.BinaryOut;

public class Huffman implements Algorithm {

	/*
	 * byte : signed 8 bit [-128, 127] char : unsigned 16 bit [0, 65535] int :
	 * signed 32 bit [-2147483648, 2147483647] short : signed 16 bit [-32768,
	 * 32767]
	 */

	/*
	 * TODO B is only useful for optimizing of encoding (e.g. reading ASCII vs
	 * UTF-16) right now TODO B should be the length of the alphabet in the
	 * huffman tree TODO have this dynamically changed depending on the
	 * frequencies of different word lengths
	 */
	private int B;

	// input buffer size
	private int WINDOW;

	// TODO having In- and OutputStreams as instance variables could cause
	// problems with multithreading???
	private BinaryIn in;
	private BinaryOut out;

	public Huffman() {
		B = 256;
		WINDOW = 256;
	}

	public Huffman(int b) {
		// TODO b should be the length of a code word, and B is therefore the
		// max size of the dictionary or alphabet
		// TODO as of right now this has no meaning since the implementation
		// works only with single chars
		B = b * b;
		WINDOW = 256;
	}

	@Override
	public void compress(InputStream is, OutputStream os) {
		this.in = new BinaryIn(is);
		this.out = new BinaryOut(os);

		// TODO This accounts only for single chars right now
		// TODO make this work for sequences of a given length b chars
		String buf = this.in.readString(WINDOW);
		char[] window = buf.toCharArray();

		int[] frequencies = initFreq();
		for (int i = 0; i < window.length; i++) {
			frequencies[window[i]]++;
		}

		Node tree = buildTree(frequencies);

		// TODO make this more clear, frequencies.length is actually the length
		// of the alphabet
		String[] dict = new String[frequencies.length];
		buildDict(dict, tree, "");

		serializeTree(tree);

		encode(window, dict);

		/*
		 * TODO this doesn't quite work yet because the length of the file is
		 * not accounted for. TODO Therefore when the buffer of the output
		 * stream at the end is not completly filled it gets padded with zeros
		 * when flushed. TODO To prevent this you would have to remember the
		 * length of the file.
		 */
		while (!in.isEmpty()) {
			buf = this.in.readString(WINDOW);
			window = buf.toCharArray();
			encode(window, dict);
		}

		this.out.close();

	}

	@Override
	public void decompress(InputStream is, OutputStream os) {
		this.in = new BinaryIn(is);
		this.out = new BinaryOut(os);

		Node root = deserializeTree();

		while (!in.isEmpty()) {
			Node node = root;
			while (!node.isLeaf()) {
				boolean bit = in.readBoolean();
				if (bit) {
					node = node.right;
				} else {
					node = node.left;
				}
			}
			out.write(node.word);
		}
		out.close();
	}

	/**
	 * Creates an array of integer so that all words of word length B can be
	 * counted. This is done by finding the smallest power of 2 that is bigger
	 * or equal to B, which is the length of the alphabet.
	 *
	 * @return an integer array that is initialized with 0 and contains enough
	 *         space for the whole alphabet
	 */
	private int[] initFreq() {

		int size = 1;

		while (size < B)
			size <<= 1;

		assert size > 0;
		return new int[size];
	}

	private static class Node implements Comparable<Node> {

		private final char word;
		private final int frequency;
		private final Node left;
		private final Node right;

		public Node(char word, int frequency, Node left, Node right) {
			this.word = word;
			this.frequency = frequency;
			this.left = left;
			this.right = right;
		}

		public boolean isLeaf() {
			return left == null && right == null;
		}

		@Override
		public int compareTo(Node other) {
			return this.frequency - other.frequency;
		}
	}

	private Node buildTree(int[] frequencies) {

		PriorityQueue<Node> pq = new PriorityQueue<>();

		for (char i = 0; i < frequencies.length; i++) {
			if (frequencies[i] > 0) {
				pq.offer(new Node(i, frequencies[i], null, null));
			}
		}

		while (pq.size() > 1) {
			Node left = pq.poll();
			Node right = pq.poll();
			pq.offer(new Node('\0', left.frequency + right.frequency, left, right));
		}

		return pq.poll();
	}

	private void buildDict(String[] dict, Node node, String code) {
		if (node.isLeaf()) {
			dict[node.word] = code;
		} else {
			buildDict(dict, node.left, code + '0');
			buildDict(dict, node.right, code + '1');
		}
	}

	private void serializeTree(Node node) {
		if (node.isLeaf()) {
			out.write(true);
			out.write(node.word);
			return;
		}
		out.write(false);
		serializeTree(node.left);
		serializeTree(node.right);
	}

	private void encode(char[] input, String[] dict) {
		for (int i = 0; i < input.length; i++) {
			String code = dict[input[i]];
			for (int j = 0; j < code.length(); j++) {
				if (code.charAt(j) == '0') {
					this.out.write(false);
				} else if (code.charAt(j) == '1') {
					this.out.write(true);
				} else
					throw new IllegalStateException("Illegal state");
			}
		}
	}

	private Node deserializeTree() {

		boolean isLeaf = in.readBoolean();

		if (isLeaf) {
			return new Node(in.readChar(), -1, null, null);
		} else {
			return new Node('\0', -1, deserializeTree(), deserializeTree());
		}

	}
}
