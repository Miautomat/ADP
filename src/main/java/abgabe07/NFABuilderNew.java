package abgabe07;

import java.util.ArrayList;
import java.util.List;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;

/**
 * @author Mieke Narjes, David Hoeck Nodes can be only single character
 */

public class NFABuilderNew {
    private Graph nfa;
    private Node start;
    private List<Node> end = new ArrayList<>();
    private Integer edgeId = 0;
    private Integer nodeId = 0;
    private String[] alphabet = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m",
        "n", "o", "p", "q",
        "r", "s", "t", "u", "v", "w", "x", "y", "z"};
    private int i = 0; // counter for knowing the right index to choose next
                       // character naming a node
    
    public String getNextEdgeID() {
        Integer nextID = edgeId++;
        return nextID.toString();
    }
    
    public String getNextNodeID() {
        Integer nextID = nodeId++;
        return nextID.toString();
    }
    
    public Graph buildNFA(String name, String start) {
        nfa = new MultiGraph(name);
        
        System.setProperty("org.graphstream.ui.renderer",
            "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
        nfa.addAttribute("ui.stylesheet", "url('src/main/resources/styles.css')");
        nfa.setAutoCreate(true);
        nfa.addNode(start);
        
        nfa.setStrict(false);
        
        return nfa;
    }
    
    public Graph toDFA(String name, String start) {
        Graph internNFA = buildNFA(name, start);
        Graph dfa = new MultiGraph(name);
        Node startNode = dfa.addNode(getNextNodeID());
        startNode.addAttribute("nodes", this.start.getId());
        for (Node node : internNFA.getNodeSet()) {
            
        }
        return null;
    }
    
    public void addState(String node1, String letter, String node2) {
        Edge newEdge = nfa.addEdge(getNextEdgeID(), node1, node2, true);
        newEdge.addAttribute("letter", letter);
    }
    
    public void displayNFA() {
        for (Node node : nfa.getNodeSet()) {
            node.addAttribute("ui.label", node.getId());
        }
        for (Edge edge : nfa.getEdgeSet()) {
            edge.addAttribute("ui.label", edge.getAttribute("letter").toString());
        }
        nfa.display();
    }
    
    public boolean readWord(String word) {
        Node currentState = start;
        char[] letters = word.toCharArray();
        for (char character : letters) {
            for (Edge edge : currentState.getEdgeSet()) {
                if (edge.getAttribute("edge").toString().charAt(0) == character) {
                    currentState = edge.getOpposite(currentState);
                } else {
                    return false;
                }
            }
        }
        return true;
    }
    
    public Node getStart() {
        return this.start;
    }
    
    public static void main(String[] args) {
        NFABuilderNew nfa = new NFABuilderNew();
        nfa.buildNFA("Test", "a");
        System.out.println("\nadding: a-1-b");
        nfa.addState("a", "1", "b");
        System.out.println("\nadding: a-1-b once again");
        nfa.addState("a", "1", "b");
        System.out.println("\nadding: b-1-c");
        nfa.addState("b", "1", "c");
        System.out.println("\nadding: b-0-a ");
        nfa.addState("b", "0", "a");
        System.out.println("\nadding: a-1-c");
        nfa.addState("a", "1", "c");
        System.out.println("\nadding: c-0-c");
        nfa.addState("c", "0", "c");
        // System.out.println("\nsetting end");
        // nfa.addOrSetNodesAttr("c", "end");
        nfa.displayNFA();
    }
}