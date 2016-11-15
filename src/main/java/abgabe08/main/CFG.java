package abgabe08.main;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CFG {
    
    private Pattern nExpr = Pattern.compile("[A-Z]");
    private Matcher nMatcher;
    private Pattern tExpr = Pattern.compile("[a-z]");
    private Matcher tMatcher;
    private Set<String> nonterminals = new HashSet<>();
    private Set<String> terminals = new HashSet<>();
    private Set<Rule> rules = new HashSet<>();
    private String startsymbol;
    private String emptysymbol;
    
    public CFG(String startsymbol, String emptysymbol) {
        nMatcher = nExpr.matcher(startsymbol);
        tMatcher = tExpr.matcher(emptysymbol);
        
        if (nMatcher.matches() && tMatcher.matches()) {
            this.startsymbol = startsymbol;
            nonterminals.add(startsymbol);
            this.emptysymbol = emptysymbol;
            terminals.add(emptysymbol);
            
        } else {
            throw new IllegalArgumentException();
        }
    }
    
    public void addNonterminal(String nonterminal) {
        nMatcher = nExpr.matcher(nonterminal);
        if (nMatcher.matches()) {
            nonterminals.add(nonterminal);
        } else {
            throw new IllegalArgumentException();
        }
    }
    
    public void addTerminal(String terminal) {
        nMatcher = tExpr.matcher(terminal);
        if (tMatcher.matches()) {
            terminals.add(terminal);
        } else {
            throw new IllegalArgumentException();
        }
    }
    
    public void addRule(String left, String right) {
        if (right.contains(emptysymbol)) {
            addLambaRule(left);
        } else {
            rules.add(new Rule(left, right));
        }
        
    }
    
    public void addLambaRule(String left) {
        rules.add(new Rule(left, emptysymbol));
    }
    
    public void removeRule(String name) {
        try {
            rules.remove(getRule(name));
        } catch (NullPointerException e) {
            
            e.getStackTrace();
            e.getMessage();
        }
    }
    
    public void setLambaFree() {
        for (Rule rule : rules) {
            
        }
    }
    
    /**
     * return the given rule or null if rule does not exists
     */
    public Rule getRule(String name) {
        Rule currentRule = null;
        try {
            for (Rule rule : rules) {
                if (rule.getLeft().equals(name)) {
                    currentRule = rule;
                } else {
                    
                    throw new RuleNotExistException();
                    
                }
            }
        } catch (RuleNotExistException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            e.getMessage();
        }
        
        return currentRule;
    }
    
    public Set<String> getNonterminals() {
        Set<String> copy = new HashSet<>();
        copy.addAll(nonterminals);
        return copy;
    }
    
    public Set<String> getTerminals() {
        Set<String> copy = new HashSet<>();
        copy.addAll(terminals);
        return copy;
    }
    
    public Set<Rule> getRules() {
        Set<Rule> copy = new HashSet<>();
        copy.addAll(rules);
        return copy;
    }
    
    public String getStartsymbol() {
        return startsymbol;
    }
    
    public String getEmptysymbol() {
        return emptysymbol;
    }
    
    public static void main(String[] args) {
        CFG cfg = new CFG("A", "e");
        
    }
    
}
