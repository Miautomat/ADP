package ressources;

import java.io.InputStream;
import java.io.OutputStream;

public interface Algorithm {
    
    void compress(InputStream in, OutputStream out, long length);
    
    void decompress(InputStream in, OutputStream out);
    
}