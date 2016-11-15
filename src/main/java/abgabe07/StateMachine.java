package abgabe07;

public interface StateMachine extends Iterable<State> {

    public State start();
    public String word();
    public StateMachine transition(Transition tran);



}
