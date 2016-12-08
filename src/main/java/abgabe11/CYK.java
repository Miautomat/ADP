package abgabe11;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import abgabe08.Grammar;
import abgabe08.Rule;

public class CYK {
    
    private Grammar grammar;
    private String word;
    private List<Set<String>> table;
    
    public CYK(Grammar grammar, String word) {
        // TODO falls toChomsky nicht funktioniert einfach weg lassen
        this.table = new ArrayList<Set<String>>();
        this.grammar = grammar.toChomsky(false);
        if (word.toLowerCase().matches("[a-z]*")) {
            this.word = word.toLowerCase();
        } else {
            throw new IllegalArgumentException("Illegal Argument: " + word.toLowerCase());
        }

      for (int m = 0; m < word.length() * word.length(); m++) {
        table.add(new HashSet<String>());
      }
        
    }

  public boolean compute() {
    init();
    for (int row = 1; row < word.length() + 1; row++) {
      for (int col = 0; col < word.length() - row; col++) {
        Set<String> result = new HashSet<>();
        for (int k = 0; k < row; k++) {
          System.out.println("s1 - col: " + col + " k + col: " + (k + col));
          System.out
                  .println("s2 - row + col: " + (row + col) + " col + k:" + (col + k + 1));

          System.out.println(toString());

          result.addAll(
                  integrateAll(atIndex(col, k + col), atIndex(row + col, col + k + 1)));
          add(col, col + row, result);
        }
      }
    }
    return atIndex(0, word.length() - 1).contains(grammar.getStartsymbol());
  }
    
    private void init() {
        for (int i = 0; i < word.length(); i++) {
            String substring = Character.toString(word.charAt(i));
            add(i, i, integrate(substring));
        }
    }
    
    private Set<String> integrateAll(Set<String> s1, Set<String> s2) {
        Set<String> result = new HashSet<>();
        
        for (String elem1 : s1) {
            for (String elem2 : s2) {
                result.addAll(integrate(elem1 + elem2));
            }
        }
        return result;
    }
    
    private Set<String> integrate(String word) {
        Set<String> result = new HashSet<>();
        Set<Rule> rules = grammar.getRules();
        for (Rule rule : rules) {
            if (rule.getRight().contains(word)) {
                result.add(rule.getLeft());
            }
        }
        return result;
    }
    
    private Set<String> atIndex(int i, int j) {
        return table.get(j * word.length() + i);
    }
    
    private void add(int i, int j, Set<String> element) {
        table.add(j * word.length() + i, element);
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
    return string.toString();
  }
}
