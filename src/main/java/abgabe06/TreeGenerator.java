package abgabe06;

import java.util.Set;

/**
 * 
 * @author Mieke Narjes, Luka Hartwig, David Hoeck
 *
 *         A class to generate binary trees, using the generated values from
 *         generator as nodes.
 */
public class TreeGenerator {

	public static void main(String[] args) {
		final int ITERATIONS = 10_000;

		for (int i = 0; i <= 100; i++) {
			BinaryTree tree = new BinaryTree();
			Generator gen = new Generator(0.7, 0.4, 10_000);
			Set<Integer> keys = gen.generate(ITERATIONS).keySet();
			if (keys.isEmpty()) {
				tree.insert(0);
			}
			for (int key : keys) {
				tree.insert(key);
			}

			System.out.println("");
			System.out.println("Tree" + i);
			System.out.println("Generate Iterations: " + ITERATIONS);
			System.out.println("Node quantity: " + keys.size());
			System.out.println("Root: " + tree.getRoot());
			System.out.println("Max: " + tree.getMax());
			System.out.println("Min: " + tree.getMin());
			System.out.println("Average: " + tree.getAverage() + "\n");
		}

	}

}
