package abgabe07;

import java.util.Iterator;

public interface State extends Iterable<String> {

    boolean isEnd();

    Iterator<String> iterator();

}
