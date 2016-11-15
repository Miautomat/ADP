package abgabe08.main;

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
    
    private final Pattern Left = Pattern.compile("[A-Z]");
    private final Pattern Right = Pattern.compile("[A-Za-z]");
    private Matcher leftMatcher;
    private Matcher rightMatcher;
    
    /**
     * adds one element to the right-Set throws Exception if its an invalid
     * String
     *
     * @param left
     * @param right
     */
    public Rule(String left, String right) {
        setLeft(left);
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
        setLeft(left);
        for (String s : right) {
            this.addRight(s);
        }
    }
    
    public String getLeft() {
        return left;
    }
    
    public Set<String> getRight() {
        return right;
    }
    
    public void setLeft(String elem) {
        setMatcher(elem, "left");
        if (leftMatcher.matches()) {
            this.left = elem;
        } else {
            try {
                throw new IllegalArgumentException("This is not a single Nonterminal: " + elem);
            } catch (IllegalArgumentException e) {
                e.getMessage();
            }
        }
    }
    
    /**
     * will set the right Matcher and add the element if input is valid
     * 
     * @param elem
     */
    public void addRight(String elem) {
        setMatcher(elem, "right");
        if (rightMatcher.matches()) {
            right.add(elem);
        } else {
            try {
                throw new IllegalArgumentException("Illegal Argument: " + elem);
            } catch (IllegalArgumentException e) {
                e.getMessage();
            }
        }
    }
    
    public boolean removeRight(String elem) {
        return right.remove(elem);
    }
    
    @Override
    public int hashCode() {
        final int prime = 37;
        int res = 1;
        res = prime * res + (left.hashCode());
        res = prime * res + (right.hashCode());
        res = prime * res + (Left.hashCode());
        res = prime * res + (Right.hashCode());
        res = prime * res + (leftMatcher.hashCode());
        res = prime * res + (rightMatcher.hashCode());
        return res;
    }
    
    public boolean equals(Rule other) {
        if (this.left != other.left)
            return false;
        if (this.right != other.right)
            return false;
        if (this.Left != other.Left)
            return false;
        if (this.Right != other.Right)
            return false;
        if (this.leftMatcher != other.leftMatcher)
            return false;
        if (this.rightMatcher != other.rightMatcher)
            return false;
        return true;
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
