package abgabe12;

import java.util.Iterator;

public class MarkovGenerator implements Iterator<Integer> {

  private double[][] matrix;
  private int state;

  public MarkovGenerator(double[][] matrix) {

    for (double[] m : matrix) {
      assert m.length == matrix.length;
      for (double d : m) {
        assert 1 >= d && d >= 0;
      }
    }

    this.matrix = matrix;
  }

  public MarkovGenerator(double[][] matrix, int initalState) {
    this(matrix);
    this.state = initalState;
  }

  @Override
  public boolean hasNext() {
    return true;
  }

  @Override
  public Integer next() {
    state = StdRandom.discrete(matrix[state]);
    return state;
  }

  public static void main(String[] args) {

    final int TOURISTS = 1000000;

    double[][] beaches = new double[][]{
            {0.0, 0.5, 0.5},
            {0.3, 0.0, 0.7},
            {0.2, 0.3, 0.5}
    };

    MarkovGenerator gen = new MarkovGenerator(beaches);

    int[] count = new int[beaches.length];

    for (int i = 0; i < TOURISTS; i++) {
      count[gen.next()]++;
    }

    System.out.println(String.format("Tourists: %d\nClifton: %d (%.3f%%)\nWaikiki: %d (%.3f%%)" +
            "\nIpanema: %d (%.3f%%)\n", TOURISTS, count[0], 100*((double) count[0])/TOURISTS,
            count[1], 100*((double) count[1])/TOURISTS, count[2], 100*((double) count[2])
                    /TOURISTS));

  }
}
