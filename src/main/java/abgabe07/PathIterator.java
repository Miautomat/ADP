package abgabe07;

import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;

public class PathIterator implements Iterator<EdgePathNode> {
    private Iterator<Edge> edges;
    private Node from;
    private Node to;
    private EdgePathNode node;
    private Iterator<EdgePathNode> next;

    public PathIterator(Node from, Node to) {
        this(from, to, null);
    }

    private PathIterator(Node from, Node to, EdgePathNode node) {
        this.from = from;
        this.to = to;
        this.edges = from.getLeavingEdgeIterator();
        this.node = node;
    }

    @Override
    public boolean hasNext() {
        while (next == null) {
            if (!edges.hasNext()) {
                return false;
            }
            Edge nextEdge = edges.next();
            if (node == null || !node.contains(nextEdge)) {
                Node opposite = nextEdge.getOpposite(from);

                if (opposite.equals(to)) {
                    next = Collections.singleton(new EdgePathNode(nextEdge, node)).iterator();
                } else {
                    next = new PathIterator(opposite, to, new EdgePathNode(nextEdge, node));
                }
                if (next.hasNext()) {
                    return true;
                } else {
                    next = null;
                }
            }
        }
        return true;
    }

    @Override
    public EdgePathNode next() {
        if (hasNext()) {
            EdgePathNode result = next.next();
            next = null;
            return result;
        } else {
            throw new NoSuchElementException();
        }
    }
}