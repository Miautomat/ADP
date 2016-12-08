package abgabe07;

public interface StateMachine extends Iterable<State> {

    State start();
    String word();
    StateMachine transition(Transition tran);



}
