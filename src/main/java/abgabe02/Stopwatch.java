package abgabe02;

/**
 * 
 * @author Mieke Narjes, Luka Hartwig, David Hoeck
 *
 */
public class Stopwatch {

	private long startTime;
	private long deltaTime;
	private boolean running;

	public Stopwatch() {

	}

	public void start() {
		if (running) {
			throw new RuntimeException("stopwatch is already running");
		} else {
			deltaTime = 0;
			startTime = System.nanoTime();
			running = true;
		}
	}

	public void stop() {
		if (!running) {
			throw new RuntimeException("stopwatch has already been stopped");
		} else {
			deltaTime = System.nanoTime() - startTime;
			running = false;
		}
	}

	public String getTime() {
		return String.format("ExecutionTime %f ", deltaTime / 1_000_000_000.0);
	}

	public static void main(String[] args) {
		Stopwatch testwatch = new Stopwatch();

		testwatch.start();
		for (int i = 0; i < 100; i++) {
			System.out.println("test");
			i++;
		}
		testwatch.stop();
		testwatch.stop();
		System.out.println(testwatch.getTime());
	}

}
