package abgabe08.main;

public class RuleNotExistException extends Exception {
    
    public RuleNotExistException() {
        System.err.println("The rule does not exist");
    }
    
}
