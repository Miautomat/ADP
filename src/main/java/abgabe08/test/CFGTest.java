package abgabe08.test;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import abgabe08.main.CFG;
import abgabe08.main.Rule;

/**
 * 
 * @author Mieke Narjes, David Hoeck, Luka Hartwig
 *
 */
public class CFGTest {
    CFG c1, chomsky1, chomsky2, chomsky3, chomsky4, chomsky5, chomsky6;
    Set<Rule> expected;
    Set<String> nonterminals, terminals;
    String[] non = {"<'a'AB>", "A", "B", "S", "C", "D", "<ABC>", "<AB'c'>", "<'b'AB>", "<AB>",
        "'a'", "<A'a'>", "'c'"};
    String[] term = {"e", "a", "d"};
    Rule r;
    
    @Before
    public void setUp() throws Exception {
        c1 = new CFG("S", "e");
        c1.addRule(new Rule("D", "a"));
        c1.addRule(new Rule("A", "ABCd"));
        c1.addRule(new Rule("<ABC>", "<AB>C"));
        c1.addRule(new Rule("<A'a'>", "<AB>'c'"));
        c1.addRule(new Rule("<'a'AB>", "<AB'c'>C"));
        r = new Rule("'a'", "<'b'AB>C");
        c1.addRule(r);
        
        expected = new HashSet<>();
        expected.add(new Rule("D", "a"));
        expected.add(new Rule("A", "ABCd"));
        expected.add(new Rule("<ABC>", "<AB>C"));
        expected.add(new Rule("<A'a'>", "<AB>'c'"));
        expected.add(new Rule("<'a'AB>", "<AB'c'>C"));
        
        nonterminals = new HashSet<>();
        nonterminals.addAll(Arrays.asList(non));
        terminals = new HashSet<>();
        terminals.addAll(Arrays.asList(term));
        
        // Test Setup for Chomsky
        chomsky1 = new CFG("S", "e"); // Start
        chomsky2 = new CFG("S", "e"); // Lambda-Free
        chomsky3 = new CFG("S", "e"); // Unit-Rule Free
        chomsky4 = new CFG("S", "e"); // terminal Tules added
        chomsky5 = new CFG("S", "e"); // nonterminal Rules added
        chomsky6 = new CFG("S", "e"); // with empty Word allowed
        // Pure Grammar
        chomsky1.addRule(new Rule("S", "ASA"));
        chomsky1.addRule(new Rule("S", "aB"));
        chomsky1.addRule(new Rule("A", "B"));
        chomsky1.addRule(new Rule("A", "S"));
        chomsky1.addRule(new Rule("B", "b"));
        chomsky1.addLambdaRule("B");
        
        // Lambda free
        // chomsky2.addRule(new Rule("Z", "S"));
        // >>>>>>> CFG-Mieke
        chomsky2.addRule(new Rule("S", "ASA"));
        chomsky2.addRule(new Rule("S", "aB"));
        chomsky2.addRule(new Rule("S", "a"));
        chomsky2.addRule(new Rule("S", "SA"));
        chomsky2.addRule(new Rule("S", "AS"));
        chomsky2.addRule(new Rule("S", "S"));
        chomsky2.addRule(new Rule("A", "B"));
        chomsky2.addRule(new Rule("A", "S"));
        chomsky2.addRule(new Rule("B", "b"));
        // Unit-Rule free
        chomsky3.addRule(new Rule("Z", "ASA"));
        chomsky3.addRule(new Rule("Z", "aB"));
        chomsky3.addRule(new Rule("Z", "a"));
        chomsky3.addRule(new Rule("Z", "SA"));
        chomsky3.addRule(new Rule("Z", "AS"));
        chomsky3.addRule(new Rule("S", "ASA"));
        chomsky3.addRule(new Rule("S", "aB"));
        chomsky3.addRule(new Rule("S", "a"));
        chomsky3.addRule(new Rule("S", "SA"));
        chomsky3.addRule(new Rule("S", "AS"));
        chomsky3.addRule(new Rule("A", "b"));
        chomsky3.addRule(new Rule("A", "ASA"));
        chomsky3.addRule(new Rule("A", "aB"));
        chomsky3.addRule(new Rule("A", "a"));
        chomsky3.addRule(new Rule("A", "SA"));
        chomsky3.addRule(new Rule("A", "AS"));
        chomsky3.addRule(new Rule("B", "b"));
        // no aB aa etc --> 'a' added
        chomsky4.addRule(new Rule("Z", "ASA"));
        chomsky4.addRule(new Rule("Z", "'a'B"));
        chomsky4.addRule(new Rule("Z", "a"));
        chomsky4.addRule(new Rule("Z", "SA"));
        chomsky4.addRule(new Rule("Z", "AS"));
        chomsky4.addRule(new Rule("S", "ASA"));
        chomsky4.addRule(new Rule("S", "'a'B"));
        chomsky4.addRule(new Rule("S", "a"));
        chomsky4.addRule(new Rule("S", "SA"));
        chomsky4.addRule(new Rule("S", "AS"));
        chomsky4.addRule(new Rule("A", "b"));
        chomsky4.addRule(new Rule("A", "ASA"));
        chomsky4.addRule(new Rule("A", "'a'B"));
        chomsky4.addRule(new Rule("A", "a"));
        chomsky4.addRule(new Rule("A", "SA"));
        chomsky4.addRule(new Rule("A", "AS"));
        chomsky4.addRule(new Rule("B", "b"));
        chomsky4.addRule(new Rule("'a'", "a"));
        // No Strings longer than 2 Nonterminals on right side --> <AB>
        chomsky5.addRule(new Rule("Z", "<AS>A"));
        chomsky5.addRule(new Rule("Z", "'a'B"));
        chomsky5.addRule(new Rule("Z", "a"));
        chomsky5.addRule(new Rule("Z", "SA"));
        chomsky5.addRule(new Rule("Z", "AS"));
        chomsky5.addRule(new Rule("S", "<AS>A"));
        chomsky5.addRule(new Rule("S", "'a'B"));
        chomsky5.addRule(new Rule("S", "a"));
        chomsky5.addRule(new Rule("S", "SA"));
        chomsky5.addRule(new Rule("S", "AS"));
        chomsky5.addRule(new Rule("A", "b"));
        chomsky5.addRule(new Rule("A", "<AS>A"));
        chomsky5.addRule(new Rule("A", "'a'B"));
        chomsky5.addRule(new Rule("A", "a"));
        chomsky5.addRule(new Rule("A", "SA"));
        chomsky5.addRule(new Rule("A", "AS"));
        chomsky5.addRule(new Rule("B", "b"));
        chomsky5.addRule(new Rule("'a'", "a"));
        chomsky5.addRule(new Rule("<AS>", "AS"));
        
        chomsky6.addRule(new Rule("Z", "<AS>A"));
        chomsky6.addRule(new Rule("Z", "'a'B"));
        chomsky6.addRule(new Rule("Z", "a"));
        chomsky6.addRule(new Rule("Z", "SA"));
        chomsky6.addRule(new Rule("Z", "AS"));
        chomsky6.addRule(new Rule("S", "<AS>A"));
        chomsky6.addRule(new Rule("S", "'a'B"));
        chomsky6.addRule(new Rule("S", "a"));
        chomsky6.addRule(new Rule("S", "SA"));
        chomsky6.addRule(new Rule("S", "AS"));
        chomsky6.addRule(new Rule("A", "b"));
        chomsky6.addRule(new Rule("A", "<AS>A"));
        chomsky6.addRule(new Rule("A", "'a'B"));
        chomsky6.addRule(new Rule("A", "a"));
        chomsky6.addRule(new Rule("A", "SA"));
        chomsky6.addRule(new Rule("A", "AS"));
        chomsky6.addRule(new Rule("B", "b"));
        chomsky6.addRule(new Rule("'a'", "a"));
        chomsky6.addRule(new Rule("<AS>", "AS"));
        chomsky6.addLambdaRule("Z");
        
    }
    
    @Test
    public void addRuleTest() {
        expected.add(new Rule("'a'", "<'b'AB>C"));
        assertEquals(expected, c1.getRules());
        assertEquals(nonterminals, c1.getNonterminals());
        assertEquals(terminals, c1.getTerminals());
    }
    
    @Test
    public void removeRuleTest() {
        c1.removeRule(r);
        assertEquals(expected, c1.getRules());
    }
    
    // @Test
    // public void toChomskyTest() {
    // CFG chomskyFalse = chomsky1.toChomsky(false);
    // assertEquals(chomsky5, chomskyFalse);
    
    // CFG chomskyTrue = chomsky1.toChomsky(true);
    // assert Equals(chomsky5, chomskyTrue);
    // }
    
    @Test
    public void setLambdaFreeTest() {
        // blöder Aufruf, bitte Ändern!!!
        assertEquals(chomsky2, chomsky1.setLambdaFree());
    }
    
    // @Test
    // public void removeUnitRulesTest() {
    // chomsky2.removeUnitRules();
    // assertEquals(chomsky3, chomsky2);
    // }
    
    @Test
    public void removeUnitRulesTest() {
        chomsky2.removeUnitRules();
        assertEquals(chomsky3, chomsky2);
    }
    
    @Test
    public void addTerminalRuleTest() {
        chomsky3.addTerminalRules();
        assertEquals(chomsky4, chomsky3);
    }
    
    @Test
    public void addNonterminalRuleTest() {
        chomsky4.addNonterminalRules();
        assertEquals(chomsky5, chomsky4);
    }
    
    @Test
    public void getRuleTest() {
        Rule testRule = new Rule("D", "a");
        Rule testRule2 = new Rule("<A'a'>", "<AB>'c'");
        
        assertEquals(testRule, c1.getRule("D"));
        assertEquals(testRule2, c1.getRule("<A'a'>"));
    }
}
