package abgabe11;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import abgabe08.Grammar;
import abgabe08.Rule;

public class CYK {
    
    private Grammar grammar;
    private String word;
    private List<Set<String>> table;
    
    public CYK(Grammar grammer, String word) {
        // TODO falls toChomsky nicht funktioniert einfach weg lassen
        this.table = new ArrayList<Set<String>>();
        this.grammar = grammar.toChomsky(false);
        if (word.toLowerCase().matches("[a-z]")) {
            this.word = word.toLowerCase();
        } else {
            throw new IllegalArgumentException("Illegal Argument: " + word.toLowerCase());
        }
        
    }
    
    public boolean compute() {
        init();
        for (int i = 1; i < word.length(); i++) {
            for (int j = 0; j < word.length() - i; j++) {
                Set<String> result = new HashSet<>();
                for (int k = 0; k < i; k++) {
                    result.addAll(integrateAll(atIndex(j, k), atIndex(i, k)));
                    add(j, j + i, result);
                }
            }
        }
        return atIndex(0, word.length() - 1).contains(grammar.getStartsymbol());
    }
    
    private void init() {
        for (int i = 0; i < word.length(); i++) {
            String substring = Character.toString(word.charAt(i));
            add(i, i, integrate(substring));
        }
    }
    
    private Set<String> integrateAll(Set<String> s1, Set<String> s2) {
        Set<String> result = new HashSet<>();
        
        for (String elem1 : s1) {
            for (String elem2 : s2) {
                result.addAll(integrate(elem1 + elem2));
            }
        }
        return result;
    }
    
    private Set<String> integrate(String word) {
        Set<String> result = new HashSet<>();
        Set<Rule> rules = grammar.getRules();
        for (Rule rule : rules) {
            if (rule.getRight().contains(word)) {
                result.add(rule.getLeft());
            }
        }
        return result;
    }
    
    private Set<String> atIndex(int i, int j) {
        return table.get(j * word.length() + i);
    }
    
    private void add(int i, int j, Set<String> element) {
        table.add(j * word.length() + i, element);
    }
    
}
