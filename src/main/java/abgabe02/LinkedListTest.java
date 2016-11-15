package abgabe02;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.NoSuchElementException;

import org.junit.Before;
import org.junit.Test;


/**
 * 
 * @author Mieke Narjes, Luka Hartwig, David Hoeck
 *
 */
public class LinkedListTest {

    private LinkedListCircularOLD<Integer> bufferFilledWithIntegers;
    private LinkedListCircularOLD<Object> emptyBuffer;

    @Before
    public void setUp() {
        bufferFilledWithIntegers = new LinkedListCircularOLD<>();
        for (Integer i = 0; i < 1000; i++) {
            bufferFilledWithIntegers.add(i);
        }
        emptyBuffer = new LinkedListCircularOLD<>();
    }

    @Test
    public void testAddNodeToEmptyList() {
        for (Integer i = 0; i < 1000; i++) {
            assertEquals(i, bufferFilledWithIntegers.get(i));
        }
        
    }

    @Test
    public void testDeleteNode() {
    	
        for (int i = 0; i < 1000; i++) {
        	bufferFilledWithIntegers.delete(0);
        }
    
        assertTrue(bufferFilledWithIntegers.isEmpty());
    }

    @Test(expected = NoSuchElementException.class)
    public void testGetOnEmptyBuffer() {
        emptyBuffer.get(100);
    }

    @Test(expected = NoSuchElementException.class)
    public void testDeleteOnEmptyList() {
        emptyBuffer.delete(0);
    }
    
}
