package abgabe11;

import org.junit.Before;
import org.junit.Test;

import abgabe08.Grammar;
import abgabe08.Rule;

import static org.junit.Assert.assertTrue;

public class CYKTest {

  private Grammar grammar;
  private CYK cyk;

  @Before
  public void setUp() throws Exception {
    grammar = new Grammar("S", "e");
    grammar.addRule(new Rule("S", "AB"));
    grammar.addRule(new Rule("S", "CD"));
    grammar.addRule(new Rule("S", "AT"));
    grammar.addRule(new Rule("S", "CU"));
    grammar.addRule(new Rule("S", "SS"));
    grammar.addRule(new Rule("T", "SB"));
    grammar.addRule(new Rule("U", "SD"));
    grammar.addRule(new Rule("A", "a"));
    grammar.addRule(new Rule("B", "b"));
    grammar.addRule(new Rule("C", "c"));
    grammar.addRule(new Rule("D", "d"));

    cyk = new CYK(grammar, "abcabd");
  }

  @Test
  public void compute() throws Exception {
    assertTrue(cyk.compute());
  }

}