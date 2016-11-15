package abgabe05;

import java.io.InputStream;
import java.io.OutputStream;

public interface Algorithm {

    void compress(InputStream in, OutputStream out);
    void decompress(InputStream in, OutputStream out);

}