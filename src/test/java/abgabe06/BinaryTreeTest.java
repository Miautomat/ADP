package abgabe06;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class BinaryTreeTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void deleteTest() {
		HashSet<Integer> testSet1 = new HashSet<>();
		testSet1.add(1);
		testSet1.add(3);
		
		HashSet<Integer> testSet2 = new HashSet<>();
		testSet2.add(3);
		
		HashSet<Integer> testSet3 = new HashSet<>();
		
		
		BinaryTree t1 = new BinaryTree();
		t1.insert(2);
		t1.insert(3);
		t1.insert(1);
		t1.delete(2);
		
		assertEquals(testSet1, t1.getNodes());
		
		t1.delete(1);
		assertEquals(testSet2, t1.getNodes());

		t1.delete(3);
		assertEquals(testSet3, t1.getNodes());
	}
	
	
	@Test
	public void insertTest() {
		HashSet<Integer> testSet = new HashSet<>();
		testSet.add(1);
		testSet.add(2);
		testSet.add(3);
		
		BinaryTree t1 = new BinaryTree();
		t1.insert(2);
		t1.insert(3);
		t1.insert(1);
		
		assertEquals(t1.getNodes(), testSet);
	}
	
	@Test
	public void getRootTest() {
		BinaryTree t = new BinaryTree();
		t.insert(2);
		t.insert(4);
		assertEquals(t.getRoot(), 2);
	}

	@Test
	public void getNodesTest() {
		HashSet<Integer> testSet = new HashSet<>();
		testSet.add(2);
		testSet.add(3);
		
		BinaryTree t1 = new BinaryTree();
		t1.insert(2);
		t1.insert(3);
		t1.delete(1);
	
		assertEquals(t1.getNodes(), testSet);
	}
	
	@Test
	public void isEmptyTest() {
		BinaryTree t1 = new BinaryTree(); // empty
		BinaryTree t2 = new BinaryTree();
		t2.insert(2);
		assertTrue(t1.isEmpty());
		assertFalse(t2.isEmpty());
	}
}
