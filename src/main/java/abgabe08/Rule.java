package abgabe08;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class to represent Rules for CFG
 * 
 * @author Mieke Narjes, David Hoeck, Luka Hartwig
 */
public class Rule {
    private String left;
    private Set<String> right = new HashSet<>();
    private StringBuilder sbLeft = new StringBuilder();
    private StringBuilder sbRight = new StringBuilder();
    private Pattern Left;
    private Pattern Right;
    private Matcher leftMatcher;
    private Matcher rightMatcher;
    
    private void patternMaker() {
        sbLeft.append("[A-Z]|") // A
            .append("'[a-z]'|") // 'a'
            .append("<('[a-z]')*[A-Z]+('[a-z]')*>|") // <'a'A>
            .append("<[A-Z]*('[a-z]')+[A-Z]*>"); // <A'a''a'>
        sbRight.append("[A-Za-z]+|") // aA
            .append("('[a-z])'*[A-Z]+('[a-z]')*|")
            .append("<('[a-z]')*[A-Z]+('[a-z]')*>[A-Z]|") // <'a'A>B
            .append("<('[a-z]')*[A-Z]+('[a-z]')*>'[a-z]'|") // <'a'A>'a'
            .append("<[A-Z]*('[a-z]')+[A-Z]*>'[a-z]'|")
            .append("<[A-Z]*('[a-z]')+[A-Z]*>[A-Z]");
        Left = Pattern.compile(sbLeft.toString());
        Right = Pattern.compile(sbRight.toString());
    }
    
    public Rule(String left) {
        patternMaker();
        setLeft(left);
        
    }
    
    /**
     * adds one element to the right-Set throws Exception if its an invalid
     * String
     *
     * @param left
     * @param right
     */
    public Rule(String left, String right) {
        this(left);
        addRight(right);
    }
    
    /**
     * adds elements for the right side one by one - throws exception if it is
     * invalid.
     * 
     * @param left
     * @param right
     */
    public Rule(String left, Set<String> right) {
        patternMaker();
        setLeft(left);
        addRight(right);
    }
    
    public String getLeft() {
        return left;
    }
    
    public Set<String> getRight() {
        return right;
    }
    
    public void setLeft(String elem) throws IllegalArgumentException {
        setMatcher(elem, "left");
        if (leftMatcher.matches()) {
            this.left = elem;
        } else {
            throw new IllegalArgumentException("This is not a single Nonterminal: " + elem);
        }
    }
    
    /**
     * will set the right Matcher and add the element if input is valid
     * 
     * @param elem
     */
    public void addRight(Set<String> elem) {
        for (String s : elem) {
            addRight(s);
        }
    }
    
    public void addRight(String elem) throws IllegalArgumentException {
        setMatcher(elem, "right");
        if (rightMatcher.matches()) {
            right.add(elem);
        } else {
            throw new IllegalArgumentException("Illegal Argument: " + elem);
        }
    }
    
    public boolean removeRight(String elem) {
        Boolean res = right.remove(elem);
        return res;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((left == null) ? 0 : left.hashCode());
        result = prime * result + ((right == null) ? 0 : right.hashCode());
        return result;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Rule other = (Rule) obj;
        if (left == null) {
            if (other.left != null)
                return false;
        } else if (!left.equals(other.left))
            return false;
        if (right == null) {
            if (other.right != null)
                return false;
        } else if (!right.equals(other.right))
            return false;
        return true;
    }
    
    @Override
    public String toString() {
        return left + " --> " + right.toString();
    }
    
    /**
     * sets the Matcher
     * 
     * @param elem
     * @param side
     */
    private void setMatcher(String elem, String side) {
        switch (side) {
        case "left":
            leftMatcher = Left.matcher(elem);
            break;
        case "right":
            rightMatcher = Right.matcher(elem);
            break;
        }
    }
}
