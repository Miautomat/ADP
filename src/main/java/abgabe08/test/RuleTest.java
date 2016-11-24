package abgabe08.test;

import static org.junit.Assert.assertEquals;

import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import abgabe08.main.Rule;

/**
 * 
 * @author Mieke Narjes, David Hoeck, Luka Hartwig
 *
 */
public class RuleTest {
    private Rule r1, r1_2, r1_2_1, r1_3, r1_4, r1_5, r1_6, r2;
    
    @Before
    public void setUp() throws Exception {
        r1 = new Rule("A", "ABCd");
        r1_2 = new Rule("A", "ABCd");
        r1_2_1 = new Rule("D", "ABCd");
        r1_3 = new Rule("<ABC>", "<AB>C");
        r1_4 = new Rule("<A'a'>", "<AB>'c'");
        r1_5 = new Rule("<'a'AB>", "<AB'c'>C");
        r1_6 = new Rule("'a'", "<'b'AB>C");
        r2 = new Rule("B", "a");
    }
    
    @After
    public void tearDown() throws Exception {}
    
    @Test
    public void ruleTest() {
        r2.addRight("b");
        assertEquals("A --> [ABCd]", r1.toString());
        assertEquals("D --> [ABCd]", r1_2_1.toString());
        assertEquals("<ABC> --> [<AB>C]", r1_3.toString());
        assertEquals("<A'a'> --> [<AB>'c']", r1_4.toString());
        assertEquals("<'a'AB> --> [<AB'c'>C]", r1_5.toString());
        assertEquals("'a' --> [<'b'AB>C]", r1_6.toString());
        assertEquals("B --> [a, b]", r2.toString());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void setLeftTest() {
        r1.setLeft("D");
        assertEquals(true, r1.equals(r1_2_1));
        
        // throws exception
        r1.setLeft("a");
        r1.setLeft("'A'");
        r1.setLeft("BA");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void addRightTest() {
        r2.addRight("c");
        Set<String> expected = new HashSet<>();
        expected.add("a");
        expected.add("c");
        assertEquals(expected, r2.getRight());
        
        Set<String> rSet = new HashSet<>();
        rSet.add("d");
        rSet.add("e");
        r2.addRight(rSet);
        expected.addAll(rSet);
        assertEquals(expected, r2.getRight());
        
        // throws Exception
        r2.addRight("#+v");
        r2.addRight("A<B>c");
        r2.addRight("<AB>CD");
    }
    
    @Test
    public void removeRightTest() {
        r2.addRight("AD");
        r2.addRight("s");
        r2.removeRight("s");
        
        Set<String> expected = new HashSet<>();
        expected.add("a");
        expected.add("AD");
        
        assertEquals(expected, r2.getRight());
    }
    
    @Test
    public void equalsTest() {
        assertEquals(true, r1.equals(r1_2));
        assertEquals(false, r1.equals(r2));
    }
}
