package abgabe11;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class EDistTest {
    String s1, s2, s3, s4;
    
    @Before
    public void setUp() throws Exception {
        s1 = "apfel";
        s2 = "pferd";
    }
    
    @Test
    public void testEditDistDP() {
        assertEquals(1, EDist.editDistDP(s1, s2, 1, 1));
        assertEquals(5, EDist.editDistDP(s1, s2, 2, 5));
        assertEquals(3, EDist.editDistDP(s1, s2, 5, 5));
        assertEquals(0, EDist.editDistDP(s1, s2, 0, 0));
    }
    
    @Test
    public void testMinimalDistance() {
        assertEquals(1, EDist.minimalDistance(1, 2, 3));
        assertEquals(1, EDist.minimalDistance(3, 1, 2));
        assertEquals(1, EDist.minimalDistance(3, 2, 1));
        assertEquals(1, EDist.minimalDistance(1, 1, 2)); // überprüfen!
        assertEquals(1, EDist.minimalDistance(1, 1, 1));
    }
    
}
