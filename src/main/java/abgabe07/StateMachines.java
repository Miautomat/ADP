package abgabe07;

import java.util.HashSet;
import java.util.Set;

public class StateMachines {
    
    private StateMachines() {}
    
    @SuppressWarnings("unused")
    public static Set<String> generateWordSet(StateMachine stateMachine) {
        
        Set<String> wordSet = new HashSet<>();
        String word = "";
        State current = stateMachine.start();
        
        return wordSet;
    }
    
}
