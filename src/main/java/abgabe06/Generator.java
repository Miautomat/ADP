package abgabe06;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * 
 * @author Mieke Narjes, Luka Hartwig, David Hoeck
 *
 *         A class which generates a dictionary with random keys
 */
public class Generator {

	private Map<Integer, Object> dict;
	private final double P;
	private final double Q;
	private final Random RANDOM;
	private State state;
	private int maxKey;

	private enum State {
		EMPTY, ADDED, REMOVED
	}

	public Generator(double p, double q, int maxKey) {
		dict = new HashMap<>();
		P = p;
		Q = q;
		RANDOM = new Random();
		state = State.EMPTY;
		this.maxKey = maxKey;
	}

	public Generator(double p, double q, int seed, int maxKey) {
		dict = new HashMap<>();
		P = p;
		Q = q;
		RANDOM = new Random(seed);
		state = State.EMPTY;
		this.maxKey = maxKey;
	}

	public State getState() {
		return this.state;
	}

	/**
	 * 
	 * @param state
	 *            The method returns a specific state of the generator.
	 */
	public State getSpecificState(String state) {
		State res = null;
		switch (state) {
		case "empty":
			res = State.EMPTY;
			break;
		case "added":
			res = State.ADDED;
			break;
		case "removed":
			res = State.REMOVED;
			break;
		}
		return res;
	}

	public Map<Integer, Object> generate() {
		switch (state) {
		case EMPTY:
			onEmpty();
			break;
		case ADDED:
			onAdded();
			break;
		case REMOVED:
			onRemoved();
			break;
		}
		return dict;
	}

	public Map<Integer, Object> generate(int iterations) {
		for (int i = 0; i < iterations; i++) {
			switch (state) {
			case EMPTY:
				onEmpty();
				break;
			case ADDED:
				onAdded();
				break;
			case REMOVED:
				onRemoved();
				break;
			}
		}
		return dict;
	}

	/**
	 * The method calls 'update()' with a probability of 100%
	 */
	private void onEmpty() {
		update(1);
	}

	/**
	 * The method calls 'update()' with a probability of the given P
	 */
	private void onAdded() {
		update(P);
	}

	/**
	 * The method calls 'update()' with a probability of 1-Q
	 */
	private void onRemoved() {
		update(1 - Q);
	}

	/**
	 * 
	 * @param probability
	 *            The method gets a specific probability, depending on the state
	 *            of the generator. according to this probability a new element
	 *            will be added to the dictionary or an old one will be removed.
	 */
	private void update(double prob) {
		if (RANDOM.nextDouble() <= prob) {
			dict.put(RANDOM.nextInt(maxKey), new Object());
			state = State.ADDED;
		} else {
			if (!dict.isEmpty()) {
				dict.remove(getRandomKey());
				if (dict.isEmpty()) {
					state = State.EMPTY;
				} else {
					state = State.REMOVED;
				}

			} else {
				state = State.EMPTY;
			}
		}
	}

	/**
	 * 
	 * The method returns a random key from the dictionary.
	 */
	private int getRandomKey() {
		Integer[] keys = dict.keySet().toArray(new Integer[dict.size()]);
		return keys[RANDOM.nextInt(keys.length)];
	}

	public static void main(String[] args) {
		final int ITERATIONS = 100_000;

		for (double i = 0; i <= 1; i += 0.1) {
			for (double j = 0; j <= 1; j += 0.1) {
				if (i == 0 && j == 0) {
					System.out.print("p\\q\t");
					for (double col = 0; col <= 1; col += 0.1) {
						System.out.print(String.format("%.2f\t", col));
					}
					System.out.print("\n");
				}
				Generator gen = new Generator(i, j, 100000);
				if (j == 0) {
					System.out.print(String.format("%.2f\t", i));
				}
				System.out.print(gen.generate(ITERATIONS).size() + "\t");
			}
			System.out.print("\n");
		}
	}

}
