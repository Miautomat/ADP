package abgabe08;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Mieke Narjes, Luka Hartwig, David Hoeck
 */
public class Grammar {
    
    private Pattern nExpr = Pattern.compile("[A-Z]");
    private Matcher nMatcher;
    private Pattern tExpr = Pattern.compile("[a-z]");
    private Matcher tMatcher;
    private Set<String> nonterminals = new HashSet<>();
    private Set<String> terminals = new HashSet<>();
    private Set<Rule> rules = new HashSet<>();
    private String startsymbol;
    private String emptysymbol;
    
    public Grammar(String startsymbol, String emptysymbol) {
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
    
    public Grammar(String startsymbol, String emptysymbol, Set<Rule> rules) {
        this(startsymbol, emptysymbol);
        this.rules.addAll(rules);
    }
    
    /**
     * adds the rule to the rule-Set and adds terminals/nonterminals to suitable
     * set
     * 
     * @param rule
     */
    public void addRule(Rule rule) {
        if (ruleExists(rule)) {
            Rule r = getRule(rule.getLeft());
            r.addRight(rule.getRight());
        } else {
            rules.add(rule);
        }
        nonterminals.add(rule.getLeft());
        for (String word : rule.getRight()) {
            if (word.contains("<") && word.contains(">")) {
                StringBuilder builder = new StringBuilder();
                builder.append(word, word.indexOf("<"), word.indexOf(">") + 1);
                nonterminals.add(builder.toString());
                String restWord = word.substring(word.indexOf(">") + 1);
                if (restWord.matches("[a-z]")) {
                    terminals.add(restWord);
                } else {
                    nonterminals.add(restWord);
                }
            } else if (word.matches("'[a-z]'")) {
                nonterminals.add(word);
            } else {
                String[] symbols = word.split("");
                for (String symbol : symbols) {
                    if (symbol.matches("[A-Z]")) {
                        nonterminals.add(symbol);
                    } else if (symbol.matches("[a-z]")) {
                        terminals.add(symbol);
                    }
                }
            }
        }
    }
    
    /**
     * removes the rule which has the given String on the list side
     * 
     * @param rule
     */
    public void removeRule(Rule rule) {
        try {
            rules.remove(rule);
        } catch (NullPointerException e) {
            e.getStackTrace();
            e.getMessage();
        }
    }
    
    /**
     * creates a new Lambda Rule
     * 
     * @param left
     */
    public void addLambdaRule(String left) {
        Set<String> empty = new HashSet<>();
        empty.add(emptysymbol);
        addRule(new Rule(left, empty));
    }
    
    /**
     * creates a copy of this grammar in Chomsky-Normalform If param = true we
     * want to allow to read the empty word right away If param = false the
     * emptyWord is forbidden
     * 
     * @return copy of (this) in chomskyNormalform
     */
    
    public Grammar toChomsky(boolean allowEmptyWord) {
        Grammar grammar = setLambdaFree();
        grammar.removeUnitRules();
        grammar.addTerminalRules();
        grammar.addNonterminalRules();
        if (allowEmptyWord) {
            grammar.addLambdaRule("Z");
        }
        return grammar;
    }
    
    /**
     * Eliminates all Lambdarules
     * 
     * @return a new grammar, without Lambdarules.
     */
    public Grammar setLambdaFree() {
        Grammar tempGrammar = new Grammar(startsymbol, emptysymbol);
        
        // remove all rules which lead only to lambda
        Queue<Rule> onlyLambdaRules = new LinkedList<>(getOnlyLambdaRules());
        if (onlyLambdaRules.isEmpty()) {
            tempGrammar.rules.addAll(this.rules);
        }
        while (!onlyLambdaRules.isEmpty()) {
            String leftside = onlyLambdaRules.poll().getLeft();
            for (Rule rule : getRules()) {
                Rule newRule = new Rule(rule.getLeft());
                for (String word : rule.getRight()) {
                    word.replaceAll(leftside, "");
                    if (word.isEmpty()) {
                        word = emptysymbol;
                    }
                    newRule.addRight(word);
                }
                if (newRule.getRight().size() == 1 && newRule.getRight().contains(emptysymbol)
                    && !newRule.getLeft().equals(leftside)) {
                    onlyLambdaRules.add(newRule);
                } else {
                    tempGrammar.rules.add(newRule);
                }
            }
        }
        
        Grammar grammar = new Grammar(startsymbol, emptysymbol);
        Queue<Rule> lambdaRules = new LinkedList<>(tempGrammar.getLambdaRules());
        Set<String> checkedLeftsides = new HashSet<>();
        
        // set the grammar Lambdafree
        if (lambdaRules.isEmpty()) {
            grammar.rules.addAll(tempGrammar.rules);
        }
        while (!lambdaRules.isEmpty()) {
            String leftside = lambdaRules.poll().getLeft();
            for (Rule rule : tempGrammar.rules) {
                Rule newRule = new Rule(rule.getLeft());
                boolean containsLambda = false;
                for (String word : rule.getRight()) {
                    int occurrences = word.length() - word.replace(leftside, "").length();
                    if (occurrences > 0) {
                        for (int i = 0; i <= occurrences * occurrences; i++) {
                            
                            StringBuilder newWord = new StringBuilder();
                            boolean shouldBeDeleted;
                            boolean charIsLeftSide;
                            // k is the bit index
                            int k = i;
                            for (Character ch : word.toCharArray()) {
                                shouldBeDeleted = (k & 1) == 1; // Deletes if
                                                                // the bit is
                                                                // set to 1
                                charIsLeftSide = ch.equals(leftside.charAt(0));
                                if (charIsLeftSide) {
                                    if (shouldBeDeleted) {
                                        // Do nothing
                                    } else {
                                        newWord.append(ch);
                                    }
                                    k >>>= 1; // i = i >>> 1 logical shift right
                                } else {
                                    newWord.append(ch);
                                }
                            }
                            String nw = newWord.toString();
                            if (nw.isEmpty()) {
                                containsLambda = true;
                            } else {
                                newRule.addRight(nw);
                            }
                            
                        }
                    } else {
                        if (!word.equals(tempGrammar.emptysymbol))
                            newRule.addRight(word);
                    }
                }
                if (containsLambda && !checkedLeftsides.contains(leftside)) {
                    lambdaRules.add(newRule);
                }
                checkedLeftsides.add(leftside);
                grammar.addRule(newRule);
            }
            
        }
        
        return grammar;
        
    }
    
    /**
     * TODO removes all unit Rules(Kettenregeln)
     */
    public void removeUnitRules() {
        removeCircles();
        removeUnitRules2();
    }
    
    /**
     * will create a new rule for each right side which is like A --> Bc or A
     * --> BBc etc. (new Rule is <c> --> c)
     */
    public void addTerminalRules() {
        Set<Rule> rulesToBeAdded = new HashSet<>();
        Set<String> stringsToBeRemoved = new HashSet<>();
        Set<String> stringsToBeAdded = new HashSet<>();
        
        for (Rule r : rules) {
            Set<String> right = r.getRight();
            for (String s : right) {
                if (!s.matches("[a-z]")) {
                    // terminal that stands not alone --> get it and create new
                    // rule for it!
                    String[] characters = s.split("");
                    for (String c : characters) {
                        // adds a rule with single c on right side and updates
                        // the string
                        if (c.matches("[a-z]")) {
                            String newNonTerminal = "'" + c + "'";
                            String newString = s.replaceAll(c, newNonTerminal);
                            nonterminals.add(newNonTerminal);
                            stringsToBeRemoved.add(s);
                            stringsToBeAdded.add(newString);
                            rulesToBeAdded.add(new Rule(newNonTerminal, c));
                        }
                    }
                }
            }
            for (String s : stringsToBeRemoved) {
                r.removeRight(s);
            }
            stringsToBeRemoved.clear();
            for (String s : stringsToBeAdded) {
                r.addRight(s);
            }
            stringsToBeAdded.clear();
        }
        for (Rule r : rulesToBeAdded) {
            addRule(r);
        }
    }
    
    /**
     * very similar to addterminalRules !!! :( call addTerminalRules before this
     * method! will create a new Rule for each right side which doesn't fit A
     * --> BC (like BDC)
     */
    public void addNonterminalRules() {
        Set<Rule> rulesToBeAdded = new HashSet<>();
        Set<String> stringsToBeRemoved = new HashSet<>();
        Set<String> stringsToBeAdded = new HashSet<>();
        
        for (Rule r : rules) {
            Set<String> right = r.getRight();
            for (String s : right) {
                String sCopy = s.replaceAll("[']*", "");
                if (sCopy.length() > 2) { //
                    // get String except from last Character and turn it to a
                    // rule
                    String[] characters = s.split("");
                    List<String> newString;
                    if (characters[characters.length - 1].matches("'")) {
                        // modified nonterminal = length 3
                        newString = createModifiedNonterminal(characters, 3);
                    } else {
                        // normal nonterminal = length 1
                        newString = createModifiedNonterminal(characters, 1);
                    }
                    stringsToBeAdded.add(newString.get(0));
                    stringsToBeRemoved.add(s);
                    rulesToBeAdded.add(new Rule(newString.get(1), newString.get(2)));
                }
            }
            for (String s : stringsToBeRemoved) {
                r.removeRight(s);
            }
            stringsToBeRemoved.clear();
            for (String s : stringsToBeAdded) {
                r.addRight(s);
            }
            stringsToBeAdded.clear();
        }
        for (Rule r : rulesToBeAdded) {
            addRule(r);
        }
    }
    
    /**
     * return the given rule or null if rule does not exists
     */
    public Rule getRule(String left) {
        Rule currentRule = null;
        
        for (Rule rule : rules) {
            if (rule.getLeft().equals(left)) {
                currentRule = rule;
            }
        }
        return currentRule;
    }
    
    /**
     * @return Set of Nonterminals used by this grammar
     */
    public Set<String> getNonterminals() {
        Set<String> copy = new HashSet<>();
        copy.addAll(nonterminals);
        return copy;
    }
    
    /**
     * @return Set of terminals used by this grammar
     */
    public Set<String> getTerminals() {
        Set<String> copy = new HashSet<>();
        copy.addAll(terminals);
        return copy;
    }
    
    /**
     * @return Set of all Rules used by this grammar
     */
    public Set<Rule> getRules() {
        Set<Rule> copy = new HashSet<>();
        copy.addAll(rules);
        return copy;
    }
    
    /**
     * @return startsymbol
     */
    public String getStartsymbol() {
        return startsymbol;
    }
    
    /**
     * @return emptysymbol
     */
    public String getEmptysymbol() {
        return emptysymbol;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((emptysymbol == null) ? 0 : emptysymbol.hashCode());
        result = prime * result + ((nonterminals == null) ? 0 : nonterminals.hashCode());
        result = prime * result + ((rules == null) ? 0 : rules.hashCode());
        result = prime * result + ((startsymbol == null) ? 0 : startsymbol.hashCode());
        result = prime * result + ((terminals == null) ? 0 : terminals.hashCode());
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
        Grammar other = (Grammar) obj;
        if (emptysymbol == null) {
            if (other.emptysymbol != null)
                return false;
        } else if (!emptysymbol.equals(other.emptysymbol))
            return false;
        if (nonterminals == null) {
            if (other.nonterminals != null)
                return false;
        } else if (!nonterminals.equals(other.nonterminals))
            return false;
        if (rules == null) {
            if (other.rules != null)
                return false;
        } else if (!rules.equals(other.rules))
            return false;
        if (startsymbol == null) {
            if (other.startsymbol != null)
                return false;
        } else if (!startsymbol.equals(other.startsymbol))
            return false;
        if (terminals == null) {
            if (other.terminals != null)
                return false;
        } else if (!terminals.equals(other.terminals))
            return false;
        return true;
    }
    
    /**
     * readable String with all information about this class
     */
    @Override
    public String toString() {
        return "nonterminals:"
            + nonterminals.toString() + "\n"
            + "terminals:"
            + terminals.toString() + "\n"
            + "startsymbol:"
            + startsymbol + "\n"
            + "emptysymbol: "
            + emptysymbol + "\n"
            + "rules:\n"
            + Arrays.toString(rules.toArray());
    }
    
    /**
     * removes all rules that point to themselves again (S-->S)
     */
    private void removeCircles() {
        for (Rule r : rules) {
            List<String> stringsToBeRemoved = new ArrayList<>();
            Set<String> right = r.getRight();
            for (String s : right) {
                if (s.matches(r.getLeft())) {
                    stringsToBeRemoved.add(s);
                }
            }
            for (String s : stringsToBeRemoved) {
                r.removeRight(s);
            }
        }
    }
    
    private void removeUnitRules2() {
        for (Rule lookFor : rules) {
            List<String> stringsToBeAdded = new ArrayList<>();
            List<String> stringsToBeRemoved = new ArrayList<>();
            String left = lookFor.getLeft();
            for (Rule check : rules) {
                Set<String> checkRight = check.getRight();
                for (String s : checkRight) {
                    if (s.matches(left)) {
                        stringsToBeAdded.addAll(lookFor.getRight());
                        stringsToBeRemoved.add(left);
                    }
                }
                for (String s : stringsToBeAdded) {
                    check.addRight(s);
                }
                for (String s : stringsToBeRemoved) {
                    check.removeRight(s);
                }
                stringsToBeAdded.clear();
                stringsToBeRemoved.clear();
            }
        }
    }
    
    /**
     * @return the fully modified String on index 0 and the Nonterminals without
     *         <> on index 1 and the new <...>-Nonterminal for the left aside on
     *         index 2
     */
    private List<String> createModifiedNonterminal(String[] s, int length) {
        List<String> result = new ArrayList<>();
        List<String> modified = new ArrayList<>();
        List<String> modified2 = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();
        StringBuilder sb3 = new StringBuilder();
        
        // for index 0 = complete modified String
        for (String string : s) {
            modified.add(string);
            modified2.add(string);
        }
        modified.add(0, "<");
        modified.add(modified.size() - length, ">");
        for (String string : modified) {
            sb.append(string);
        }
        result.add(sb.toString());
        
        // for index 1 = nonterminals in <>
        int i = 1;
        int index = modified.size() - 1;
        while (i <= length) {
            modified.remove(index);
            i++;
            index--;
        }
        for (String string : modified) {
            sb2.append(string);
        }
        result.add(sb2.toString());
        
        // for index 2 = pure Nonterminals without <>
        int j = 1;
        int index2 = modified2.size() - 1;
        while (j <= length) {
            modified2.remove(index2);
            j++;
            index2--;
        }
        for (String string : modified2) {
            sb3.append(string);
        }
        result.add(sb3.toString());
        return result;
    }
    
    /**
     * @return true if rule with param left exists
     */
    private boolean ruleExists(Rule r) {
        for (Rule rule : rules) {
            if (rule.getLeft().equals(r.getLeft())) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * @return all rules which lead to lambda
     */
    private Set<Rule> getLambdaRules() {
        Set<Rule> rules = new HashSet<>();
        for (Rule rule : getRules()) {
            if (rule.getRight().contains(emptysymbol)) {
                rules.add(rule);
            }
        }
        return rules;
    }
    
    /**
     * @return the rules which only lead to the emptysymbol
     */
    private Set<Rule> getOnlyLambdaRules() {
        Set<Rule> rules = new HashSet<>();
        for (Rule rule : getRules()) {
            if (rule.getRight().size() == 1 && rule.getRight().contains(emptysymbol)) {
                rules.add(rule);
            }
        }
        return rules;
    }
    
    /**
     * @See: http://stackoverflow.com/questions/3976616/how-to-find-nth-occurrence-of-character-in-a-string
     * @param str
     * @param substr
     * @param n
     * @return
     */
    @SuppressWarnings("unused")
    private int ordinalIndexOf(String str, String substr, int n) {
        int pos = str.indexOf(substr);
        while (--n > 0 && pos != -1)
            pos = str.indexOf(substr, pos + 1);
        return pos;
    }
    
    public static void main(String[] args) {
        Grammar chomsky3 = new Grammar("S", "e");
        Grammar chomsky4 = new Grammar("S", "e");
        
        System.out.println("empty: " + chomsky3.equals(chomsky4));
        chomsky3.addRule(new Rule("Z", "ASA"));
        chomsky4.addRule(new Rule("Z", "ASA"));
        
        System.out.println("Z --> [ASA]: " + chomsky3.equals(chomsky4));
        chomsky3.addRule(new Rule("Z", "aB"));
        chomsky4.addRule(new Rule("Z", "aB"));
        System.out.println("Z --> [ASA, ab]: " + chomsky3.equals(chomsky4));
        chomsky3.addRule(new Rule("Z", "a"));
        chomsky4.addRule(new Rule("Z", "a"));
        System.out.println("Z --> [ASA, ab, a]: " + chomsky3.equals(chomsky4));
        chomsky3.addRule(new Rule("Z", "SA"));
        chomsky4.addRule(new Rule("Z", "SA"));
        System.out.println("Z --> [ASA, ab, a, SA]: " + chomsky3.equals(chomsky4));
        // chomsky3.addRule(new Rule("Z", "AS"));
        // chomsky3.addRule(new Rule("S", "ASA"));
        // chomsky3.addRule(new Rule("S", "aB"));
        // chomsky3.addRule(new Rule("S", "a"));
        // chomsky3.addRule(new Rule("S", "SA"));
        // chomsky3.addRule(new Rule("S", "AS"));
        // chomsky3.addRule(new Rule("A", "b"));
        // chomsky3.addRule(new Rule("A", "ASA"));
        // chomsky3.addRule(new Rule("A", "aB"));
        // chomsky3.addRule(new Rule("A", "a"));
        // chomsky3.addRule(new Rule("A", "SA"));
        // chomsky3.addRule(new Rule("A", "AS"));
        // chomsky3.addRule(new Rule("B", "b"));
        
    }
    
}
