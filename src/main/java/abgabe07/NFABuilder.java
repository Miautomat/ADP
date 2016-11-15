package abgabe07;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;

/**
 * @author Mieke Narjes, David Hoeck Nodes can be only single character
 */

public class NFABuilder {
    private Graph graph;
    private Node start;
    private List<Node> end = new ArrayList<>();
    private String[] alphabet = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m",
        "n", "o", "p", "q",
        "r", "s", "t", "u", "v", "w", "x", "y", "z"};
    private int i = 0; // counter for knowing the right index to choose next
                       // character naming a node
    
    public Graph build(String name, String start) {
        graph = new MultiGraph(name);
        // adds a start-Node instantly
        this.addStartNode(start);
        
        graph.setStrict(false);
        
        // these Renderers suit our graphs better than the standard.
        System.setProperty("org.graphstream.ui.renderer",
            "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
        graph.addAttribute("ui.stylesheet", "url('src/main/resources/styles.css')");
        
        System.out.println("build successful");
        return graph;
    }
    
    // throws statement missing / try-catch?
    /**
     * Node-Names only consist of a single character from the alphabet!
     * 
     * @param prev
     * @param character
     * @param succ
     */
    public void addState(String prev, String character, String succ) {
        Collection<Node> nodes = graph.getNodeSet();
        System.out.println(nodes.toString());
        
        System.out.println("checking for prev");
        Node p = checkForNode(nodes, prev);
        if (p != null) {
            // prev found! --> check edge
            System.out.println("prev found --> continue");
            Collection<Edge> leavingEdges = p.getLeavingEdgeSet();
            Edge e = checkForEdge(leavingEdges, character);
            if ((e != null)) {
                System.out.println("get TargetNode and edit");
                Node successorNode = e.getTargetNode();
                addOrSetNodesAttr(succ, successorNode);
            } else {
                // edge not found --> check for succ
                System.out.println("edge not found --> search succ");
                Node s = checkForNode(nodes, succ);
                if (s != null) {
                    // succ found --> add edge
                    System.out.println("successor found, create edge");
                    Edge newEdge = graph.addEdge(character, p, s, true);
                    String label = newEdge.getId();
                    newEdge.addAttribute("ui.label", label);
                    System.out.println(newEdge);
                    
                    return;
                } else {
                    // succ does not exist --> create edge and succ-Node
                    System.out.println("Succ not found --> creating edge and succ-Node");
                    Node newSucc = graph.addNode(getNextCharacter());
                    
                    addOrSetNodesAttr(succ, newSucc);
                    System.out.println("previous: " + p);
                    System.out.println("newState: " + newSucc);
                    System.out.println("character: " + character);
                    System.out.println(graph.addEdge("z", p, newSucc, true).toString());
                    // newEdge.addAttribute("ui.label", newEdge.getId());
                    System.out.println("irgendwas");
                }
            }
        } else {
            System.out.println("Fehler!!!");
        }
    }
    
    public void displayNFA() {
        graph.display();
    }
    
    public void setStartOrEnd(String node, String startOrEnd) {
        Collection<Node> nodes = graph.getNodeSet();
        Node newNode = null;
        for (Node n : nodes) {
            String attr = n.getAttribute("nodes");
            if (attr.matches("\\w*" + node + "\\w*")) {
                n.changeAttribute(startOrEnd, true);
                newNode = n;
            }
        }
        
        switch (startOrEnd) {
        case "start":
            start.removeAttribute("start");
            this.start = newNode;
            break;
        case "end":
            this.end.add(newNode);
            break;
        }
    }
    
    // returns the node with attribute s if found - null if not found
    private Node checkForNode(Collection<Node> nodes, String s) {
        for (Node n : nodes) {
            String sAttr = n.getAttribute("nodes");
            System.out.println("check node: " + n.getId() + ",  sAttr = " + sAttr);
            if (sAttr.matches("\\w*" + s + "\\w*")) {
                System.out.println("found node");
                return n;
            }
        }
        return null; // iiih
    }
    
    // returns the edge if found - null else
    private Edge checkForEdge(Collection<Edge> edges, String character) {
        for (Edge e : edges) {
            if (e.getId() == character) {
                // edge exists --> get succ and add Attribute(doesn't
                // matter if node already exists)
                System.out.println("Edge found!");
                return e;
            }
        }
        return null; // iiih
    }
    
    /**
     * adds a Node to the nodes attribute of the wished Node (in our case = add
     * Node)
     * 
     * @param s
     * @param n
     */
    private void addOrSetNodesAttr(String s, Node n) {
        if (n.hasAttribute("nodes")) {
            String attr = n.getAttribute("nodes");
            if (attr.matches("\\w*" + s + "\\w*")) {
                System.out.println("attribute already exists");
            } else {
                n.changeAttribute("nodes", attr + s);
                System.out.println("attribute 'nodes' edited");
            }
        } else {
            n.addAttribute("nodes", s);
            String label = n.getAttribute("nodes");
            n.setAttribute("ui.label", label);
            
            System.out.println("attribute 'nodes' added");
        }
    }
    
    /**
     * the next node will be named like the returned character
     * 
     * @return
     */
    private String getNextCharacter() {
        String res = alphabet[i];
        i += 1;
        return res;
    }
    
    /**
     * should be called in constructor only once!
     * 
     * @param start
     */
    private void addStartNode(String start) {
        graph.addNode(getNextCharacter()).addAttribute("start", true);
        this.start = graph.getNode("a");
        this.start.addAttribute("nodes", "a");
        String label = this.start.getAttribute("nodes");
        this.start.setAttribute("ui.label", label);
    }
    
    public static void main(String[] args) {
        NFABuilder nfa = new NFABuilder();
        nfa.build("Test", "a");
        System.out.println("\nadding: a-1-b");
        nfa.addState("a", "1", "b");
        System.out.println("\nadding: a-1-b once again");
        nfa.addState("a", "1", "b");
        System.out.println("\nadding: b-0-a ");
        nfa.addState("b", "0", "a");
        System.out.println("\nadding: b-1-c");
        nfa.addState("b", "1", "c");
        System.out.println("\nadding: c-0-c");
        nfa.addState("c", "0", "c");
        System.out.println("\nadding: a-1-c");
        nfa.addState("a", "1", "c");
        System.out.println("\nsetting end");
        // nfa.addOrSetNodesAttr("c", "end");
        nfa.displayNFA();
    }
}