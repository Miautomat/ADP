package abgabe07;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.MultiGraph;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import static org.junit.Assert.*;

public class PathIteratorTest {

    Graph loop;

    @Test
    public void testSingleEdgePositive() {

        Graph singleEdge = new MultiGraph("Single Edge");
        singleEdge.addNode("1");
        singleEdge.addNode("2");
        singleEdge.addEdge("1 -> 2", "1", "2", true);

        List<Edge> expected = new ArrayList<>();
        expected.add(singleEdge.getEdge("1 -> 2"));

        PathIterator pathIterator = new PathIterator(
                singleEdge.getNode("1"),
                singleEdge.getNode("2")
        );
        EdgePathNode path = pathIterator.next();
        List<Edge> actual = path.toList();

        assertEquals(expected, actual);
        assertFalse(pathIterator.hasNext());

    }

    @Test
    public void testSingleEdgeNegative() {

        Graph singleEdge = new MultiGraph("Single Edge");
        singleEdge.addNode("1");
        singleEdge.addNode("2");
        singleEdge.addEdge("1 -> 2", "1", "2", true);

        PathIterator pathIterator = new PathIterator(
                singleEdge.getNode("2"),
                singleEdge.getNode("1")
        );

        assertFalse(pathIterator.hasNext());

    }

    @Test
    public void testSingleNode() {

        Graph singleNode = new MultiGraph("Single Node");
        singleNode.addNode("1");
        singleNode.addEdge("1 -> 1", "1", "1", true);

        List<Edge> expected = new ArrayList<>();
        expected.add(singleNode.getEdge("1 -> 1"));

        PathIterator pathIterator = new PathIterator(
                singleNode.getNode("1"),
                singleNode.getNode("1")
        );
        EdgePathNode path = pathIterator.next();
        List<Edge> actual = path.toList();

        assertEquals(expected, actual);
        assertFalse(pathIterator.hasNext());

    }

    @Test
    public void testCircle() {

        Graph circle = new MultiGraph("Circle");
        circle.addNode("1");
        circle.addNode("2");
        circle.addEdge("1 -> 2", "1", "2", true);
        circle.addEdge("2 -> 1", "2", "1", true);

        List<Edge> expected = new ArrayList<>();
        expected.add(circle.getEdge("1 -> 2"));

        PathIterator pathIterator = new PathIterator(
                circle.getNode("1"),
                circle.getNode("2")
        );
        EdgePathNode path = pathIterator.next();
        List<Edge> actual = path.toList();

        assertEquals(expected, actual);
        assertFalse(pathIterator.hasNext());

    }

    @Test
    public void testRectangle() {

        Graph rectangle = new MultiGraph("Rectangle");
        rectangle.addNode("1");
        rectangle.addNode("2");
        rectangle.addNode("3");
        rectangle.addNode("4");
        rectangle.addEdge("1 -> 2", "1", "2", true);
        rectangle.addEdge("2 -> 4", "2", "4", true);
        rectangle.addEdge("1 -> 3", "1", "3", true);
        rectangle.addEdge("3 -> 4", "3", "4", true);

        Set<List<Edge>> expected = new HashSet<>();

        List<Edge> first = new ArrayList<>();
        first.add(rectangle.getEdge("1 -> 2"));
        first.add(rectangle.getEdge("2 -> 4"));

        List<Edge> second = new ArrayList<>();
        second.add(rectangle.getEdge("1 -> 3"));
        second.add(rectangle.getEdge("3 -> 4"));

        expected.add(first);
        expected.add(second);

        PathIterator pathIterator = new PathIterator(
                rectangle.getNode("1"),
                rectangle.getNode("4")
        );

        Set<List<Edge>> actual = new HashSet<>();
        actual.add(pathIterator.next().toList());
        actual.add(pathIterator.next().toList());

        assertEquals(expected, actual);
        assertFalse(pathIterator.hasNext());

    }

    @Test
    public void testNotConnected() {

        Graph notConnected = new MultiGraph("Not Connected");
        notConnected.addNode("1");
        notConnected.addNode("2");

        PathIterator pathIterator = new PathIterator(
                notConnected.getNode("1"),
                notConnected.getNode("2")
        );

        assertFalse(pathIterator.hasNext());

    }

    @Test
    public void testLoop() {

        Graph loop = new MultiGraph("Loop");
        loop.addNode("1");
        loop.addNode("2");
        loop.addNode("3");
        loop.addNode("4");
        loop.addEdge("1 -> 2", "1", "2", true);
        loop.addEdge("2 -> 3", "2", "3", true);
        loop.addEdge("3 -> 2", "3", "2", true);
        loop.addEdge("3 -> 4", "3", "4", true);

        List<Edge> expected = new ArrayList<>();
        expected.add(loop.getEdge("1 -> 2"));
        expected.add(loop.getEdge("2 -> 3"));
        expected.add(loop.getEdge("3 -> 4"));

        PathIterator pathIterator = new PathIterator(
                loop.getNode("1"),
                loop.getNode("4")
        );
        EdgePathNode path = pathIterator.next();
        List<Edge> actual = path.toList();

        assertEquals(expected, actual);
        assertFalse(pathIterator.hasNext());

    }

    @Test
    public void testAlreadyVisitedNode() {

        Graph alreadyVisited = new MultiGraph("Already Visited");
        alreadyVisited.addNode("1");
        alreadyVisited.addNode("2");
        alreadyVisited.addNode("3");
        alreadyVisited.addNode("4");
        alreadyVisited.addNode("5");
        alreadyVisited.addEdge("1 -> 2", "1", "2", true);
        alreadyVisited.addEdge("2 -> 4", "2", "4", true);
        alreadyVisited.addEdge("1 -> 3", "1", "3", true);
        alreadyVisited.addEdge("3 -> 4", "3", "4", true);
        alreadyVisited.addEdge("4 -> 5", "4", "5", true);

        Set<List<Edge>> expected = new HashSet<>();

        List<Edge> first = new ArrayList<>();
        first.add(alreadyVisited.getEdge("1 -> 2"));
        first.add(alreadyVisited.getEdge("2 -> 4"));
        first.add(alreadyVisited.getEdge("4 -> 5"));

        List<Edge> second = new ArrayList<>();
        second.add(alreadyVisited.getEdge("1 -> 3"));
        second.add(alreadyVisited.getEdge("3 -> 4"));
        second.add(alreadyVisited.getEdge("4 -> 5"));

        expected.add(first);
        expected.add(second);

        PathIterator pathIterator = new PathIterator(
                alreadyVisited.getNode("1"),
                alreadyVisited.getNode("5")
        );

        Set<List<Edge>> actual = new HashSet<>();
        actual.add(pathIterator.next().toList());
        actual.add(pathIterator.next().toList());

        assertEquals(expected, actual);
        assertFalse(pathIterator.hasNext());

    }

    @Test(expected = NoSuchElementException.class)
    public void testNextIfHasNextIsFalse() {
        Graph graph = new MultiGraph("Single Graph");
        graph.addNode("1");
        PathIterator path = new PathIterator(
                graph.getNode("1"),
                graph.getNode("1")
        );
        assertFalse(path.hasNext());
        path.next();
    }

}