package abgabe11;

import org.junit.Before;
import org.junit.Test;

import abgabe08.Grammar;
import abgabe08.Rule;

public class CYKTest {

  private Grammar grammar;
  private CYK cyk;

  @Before
  public void setUp() throws Exception {
    grammar = new Grammar("S", "e");
    grammar.addRule(new Rule("S", "A"));
    grammar.addRule(new Rule("S", "aB"));
    grammar.addRule(new Rule("S", "a"));
    grammar.addRule(new Rule("S", "SA"));
    grammar.addRule(new Rule("S", "B"));
    grammar.addRule(new Rule("B", "b"));

    cyk = new CYK(grammar, "aabbaaa");
  }

  @Test
  public void compute() throws Exception {
    cyk.compute();
  }

}