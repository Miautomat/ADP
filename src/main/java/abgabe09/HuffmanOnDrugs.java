package abgabe09;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.PriorityQueue;

import abgabe05.Algorithm;
import abgabe05.princeton.BinaryIn;
import abgabe05.princeton.BinaryOut;

public class HuffmanOnDrugs implements Algorithm {

  private static double[][] probabilities =
      new double[][] {
              {0.05, 0.85, 0.05, 0.05},
              {0.05, 0.20, 0.70, 0.05},
              {0.10, 0.10, 0.10, 0.70},
              {0.65, 0.15, 0.15, 0.05}
      };

  private Node trees[];
  private String[][] dict;
  private int state;

  public HuffmanOnDrugs() {
    this.state = 0;
    this.trees = new Node[4];
    this.dict = new String[4][4];
    for (int i = 0; i < 4; i++) {
      this.trees[i] = buildTree(probabilities[i]);
      buildDict(dict[i], trees[i], "");
    }
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

  @Override
  public void compress(InputStream in, OutputStream os, long length) {

    BinaryOut out = new BinaryOut(os);

    try {
      int next;
      while ((next = in.read()) > -1) {
        // ASCII offset by 48 to get 0 to 3 as indices
        int nextState = next - 48;
        String code = getCode(nextState);
        // TODO extract
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
      e.printStackTrace();
    }
    out.close();
  }

  @Override
  public void decompress(InputStream is, OutputStream os) {

    BinaryIn in = new BinaryIn(is);
    BinaryOut out = new BinaryOut(os);
    this.state = 0;

    reading:
      while (!in.isEmpty()) {
        Node node = this.trees[state];
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
        state = node.word;
      }
    out.close();
  }

  private String getCode(int nextState) {
    String[] dict = this.dict[state];
    return dict[nextState];
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

    @Override
    public String toString() {
      return "Node{" +
              "word=" + word +
              ", frequency=" + frequency +
              ", left=" + left +
              ", right=" + right +
              '}';
    }
  }

  public static void main(String[] args) throws IOException {

    final String PATH = "src/test/resources";
    HuffmanOnDrugs huffman = new HuffmanOnDrugs();

    Path path = Paths.get(PATH + "/d100000.txt");
    long length = Files.size(path);
    InputStream filestream = Files.newInputStream(path);
    OutputStream compressedFile = Files.newOutputStream(Paths.get(PATH +
            "/compressed/d100000.txt"));

    huffman.compress(filestream, compressedFile, length);

    InputStream file = Files.newInputStream(Paths.get(PATH + "/compressed/d100000.txt"));
    OutputStream decompressedFile =
        Files.newOutputStream(Paths.get(PATH + "/decompressed/d100000.txt"));
    huffman.decompress(file, decompressedFile);
  }
}
