package abgabe10;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.PriorityQueue;

import abgabe05.Algorithm;
import abgabe05.princeton.BinaryIn;
import abgabe05.princeton.BinaryOut;

public class HuffmanOnSteroids implements Algorithm {

  private double[][] probablilities = new double[4][4];

  private Node trees[];
  private String[][] dict;
  private int state;
  private final int WINDOW  = 256;
  private BinaryIn in;
  private BinaryOut out;

  public HuffmanOnSteroids() {
    this.state = 0;
  }

  private Node buildTree(double[] frequencies) {

    PriorityQueue<Node> pq = new PriorityQueue<>();

    for (char i = 0; i < frequencies.length; i++) {
      if (frequencies[i] > 0) {
        pq.offer(new Node(i, frequencies[i], null, null));
      }
    }

    if (pq.size() == 1) {
      if (frequencies['\0'] == 0) {
        pq.offer(new Node('\0', 0, null, null));
      } else {
        pq.offer(new Node('\1', 0, null, null));
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
      System.out.println(code.length());
    } else {
      buildDict(dict, node.left, code + '0');
      buildDict(dict, node.right, code + '1');
    }
  }

  private void setProbablilities(char[] window) {
    for (int i = 0; i < window.length - 1; i++) {
      probablilities[window[i]][window[i + 1]]++;
    }
  }

  @Override
  public void compress(InputStream is, OutputStream os, long length) {
    this.in = new BinaryIn(is);
    this.out = new BinaryOut(os);

    String buf = this.in.readString(WINDOW);
    char[] window = buf.toCharArray();

    setProbablilities(window);

    for (int i = 0; i < 4; i++) {
      this.trees[i] = buildTree(this.probablilities[i]);
      buildDict(dict[i], trees[i], "");
      serializeTree(trees[i]);
    }

    out.write(length);

    for (char c : window) {
      encode((int) c);
    }

    int next;
    while (!in.isEmpty()) {
      next =  in.readByte();
      encode(next);
    }

    this.out.close();

  }

  private void encode(int next) {
    int nextState = next - 48;
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

  @Override
  public void decompress(InputStream is, OutputStream os) {
    BinaryIn in = new BinaryIn(is);
    BinaryOut out = new BinaryOut(os);
    this.state = 0;

    for (int i = 0; i < 4; i++) {
      this.trees[i] = deserializeTree();
    }

    long length = in.readLong();

    long written = 0L;
    while (written < length) {
      Node node = this.trees[state];
      while (!node.isLeaf()) {
        boolean bit = in.readBoolean();
        if (bit) {
          node = node.right;
        } else {
          node = node.left;
        }
      }
      out.write(node.word);
      state = node.word;
      written++;
    }
    assert written == length;
    out.close();
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

  private String getCode(int nextState) {
    String[] dict = this.dict[state];
    return dict[nextState];
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

  private Node deserializeTree() {

    boolean isLeaf = in.readBoolean();

    if (isLeaf) {
      return new Node(in.readChar(), -1, null, null);
    } else {
      return new Node('\0', -1, deserializeTree(), deserializeTree());
    }

  }
}
