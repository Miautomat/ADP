package abgabe07;

import java.util.Iterator;

public interface State extends Iterable<String> {

    public boolean isEnd();

    public Iterator<String> iterator();

}
