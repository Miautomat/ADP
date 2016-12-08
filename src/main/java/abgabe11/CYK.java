package abgabe11;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import abgabe08.Grammar;
import abgabe08.Rule;

/**
 * @author Mieke Narjes, David Hoeck, Luka Hartwig
 *     <p>Implementation of the CYK-Algorithm
 */
public class CYK {

  private Grammar grammar;
  private String word;
  private List<Set<String>> table;

  /**
   * Precondition: grammar has to be in CNF
   *
   * @param grammar grammar in CNF
   * @param word word
   */
  public CYK(Grammar grammar, String word) {
    this.table = new ArrayList<>();
    this.grammar = grammar;
    if (word.toLowerCase().matches("[a-z]*")) {
      this.word = word.toLowerCase();
    } else {
      throw new IllegalArgumentException("Illegal Argument: " + word.toLowerCase());
    }

    for (int m = 0; m < word.length() * word.length(); m++) {
      table.add(new HashSet<String>());
    }
  }

  /**
   * Checks if the grammar can produce the word
   *
   * @return true if the word is accepted, flase otherwise
   */
  public boolean compute() {
    init();
    for (int row = 1; row < word.length() + 1; row++) {
      for (int col = 0; col < word.length() - row; col++) {
        Set<String> result = new HashSet<>();
        for (int k = 0; k < row; k++) {

          int[] cell1 = new int[] {col, k + col};
          int[] cell2 =
              new int[] {
                col + k + 1, row + col,
              };

          result.addAll(integrateAll(atIndex(cell1[0], cell1[1]), atIndex(cell2[0], cell2[1])));
          add(col, col + row, result);

        }
      }
    }
    return atIndex(0, word.length() - 1).contains(grammar.getStartsymbol());
  }

  /** Initializes the table with the word as diagonal */
  private void init() {
    for (int i = 0; i < word.length(); i++) {
      String substring = Character.toString(word.charAt(i));
      add(i, i, integrate(substring));
    }
  }

  /**
   * Takes two sets of nonterminals and returns a set of nonterminals that can produce atleast one
   * combination of elements of set one with set two
   *
   * @param s1 set of first nonterminals
   * @param s2 set of second nonterminals
   * @return set of nonterminals
   */
  private Set<String> integrateAll(Set<String> s1, Set<String> s2) {
    Set<String> result = new HashSet<>();

    for (String elem1 : s1) {
      for (String elem2 : s2) {
        result.addAll(integrate(elem1 + elem2));
      }
    }
    return result;
  }

  /**
   * Returns a set of nonterminals that can produce the given symbol
   *
   * @param symbol symbol that might be produced
   * @return set of producers
   */
  private Set<String> integrate(String symbol) {
    Set<String> result = new HashSet<>();
    Set<Rule> rules = grammar.getRules();
    for (Rule rule : rules) {
      if (rule.getRight().contains(symbol)) {
        result.add(rule.getLeft());
      }
    }
    return result;
  }

  /**
   * Gets the element at a given position in the table
   *
   * @param i column
   * @param j row
   * @return element
   */
  private Set<String> atIndex(int i, int j) {
    return table.get(j * word.length() + i);
  }

  /**
   * Adds an element to the given position in the table
   *
   * @param i column
   * @param j row
   * @param element element
   */
  private void add(int i, int j, Set<String> element) {
    table.set(j * word.length() + i, element);
  }

  @Override
  public String toString() {
    StringBuilder string = new StringBuilder();
    for (int i = 0; i < table.size(); i++) {
      if (i != 0 && (i % word.length()) == 0) {
        string.append("\n");
      }
      string.append(String.format("%-10s", table.get(i).toString()));
    }
    string.append("\n");
    return string.toString();
  }
}
