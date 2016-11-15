package abgabe07;

import java.util.ArrayList;
import java.util.List;

import org.graphstream.graph.Edge;

public class EdgePathNode {
    Edge edge;
    EdgePathNode parent;

    EdgePathNode(Edge edge, EdgePathNode parent) {
        this.edge = edge;
        this.parent = parent;
    }

    boolean contains(Edge edge) {
        if (this.edge.equals(edge)) {
            return true;
        } else {
            return parent != null && parent.contains(edge);
        }
    }

    List<Edge> toList() {
        List<Edge> list = new ArrayList<>();
        for (EdgePathNode node = this; node != null; node = node.parent) {
            list.add(0, node.edge);
        }
        return list;
    }
}
