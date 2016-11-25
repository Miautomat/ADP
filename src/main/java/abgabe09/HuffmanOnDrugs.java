package abgabe09;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.PriorityQueue;

import abgabe05.Algorithm;
import abgabe05.princeton.BinaryIn;
import abgabe05.princeton.BinaryOut;

public class HuffmanOnDrugs implements Algorithm {

  private double[][] probabilities =
      new double[][] {
        {0.05, 0.85, 0.05, 0.05},
        {0.05, 0.20, 0.70, 0.05},
        {0.10, 0.10, 0.10, 0.70},
        {0.65, 0.15, 0.15, 0.05}
      };

  private enum State {
    STATE01,
    STATE02,
    STATE03,
    STATE04
  }

  private Node trees[];
  private String[][] dict;
  private State state;

  public HuffmanOnDrugs() {
    this.state = State.STATE01;
    this.trees = new Node[4];
    this.dict = new String[4][4];
    for (int i = 0; i < 4; i++) {
      this.trees[i] = buildTree(probabilities[i]);
      buildDict(dict[i], trees[i], "");
    }
  }

  @Override
  public void compress(InputStream in, OutputStream os, long length) {

    BinaryOut out = new BinaryOut(os);

    try {
      int next;
      while ((next = in.read()) > -1) {
        // ASCII offset by 48 to get 0 to 3 as indices
        State nextState = State.values()[next - 48];
        String code = getCode(nextState);
        for (int j = 0; j < code.length(); j++) {
          if (code.charAt(j) == '0') {
            out.write(false);
          } else if (code.charAt(j) == '1') {
            out.write(true);
          } else throw new IllegalStateException("Illegal state");
        }
        state = nextState;
      }
    } catch (IOException e) {

    }
    out.close();
  }

  @Override
  public void decompress(InputStream is, OutputStream os) {

    BinaryIn in = new BinaryIn(is);
    BinaryOut out = new BinaryOut(os);
    this.state = State.STATE01;

    reading:
    while (!in.isEmpty()) {
      Node node = this.trees[state.ordinal()];
      while (!node.isLeaf()) {
        if (in.isEmpty()) break reading;
        boolean bit = in.readBoolean();

        if (bit) {
          node = node.right;
        } else {
          node = node.left;
        }
      }
      out.write(Character.toChars(node.word + 48)[0]);
      state = State.values()[node.word];
    }
    out.close();
  }

  private String getCode(State nextState) {
    String[] dict = this.dict[state.ordinal()];
    return dict[nextState.ordinal()];
  }

  private Node buildTree(double[] frequencies) {

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

  private static class Node implements Comparable<Node> {

    private final char word;
    private final double frequency;
    private final Node left;
    private final Node right;

    public Node(char word, double frequency, Node left, Node right) {
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
      return Double.compare(this.frequency, other.frequency);
    }
  }

  public static void main(String[] args) throws IOException {

    final String PATH = "src/test/java/abgabe05/resources";
    HuffmanOnDrugs huffman = new HuffmanOnDrugs();

    Path path = Paths.get(PATH + "/drugs.txt");
    long length = Files.size(path);
    InputStream filestream = Files.newInputStream(path);
    OutputStream compressedFile =
            Files.newOutputStream(Paths.get(PATH + "/compressed/drugs.txt"));

    huffman.compress(filestream, compressedFile, length);

    InputStream file =
            Files.newInputStream(Paths.get(PATH + "/compressed/drugs.txt"));
    OutputStream decompressedFile =
            Files.newOutputStream(Paths.get(PATH + "/decompressed/drugs.txt"));
    huffman.decompress(file, decompressedFile);
  }
}
