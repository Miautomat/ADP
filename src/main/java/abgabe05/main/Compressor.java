package abgabe05.main;

import java.io.InputStream;
import java.io.OutputStream;

public class Compressor {
    
    private Algorithm compressor;
    
    public Compressor(Algorithm compressor) {
        this.compressor = compressor;
    }
    
    public void compress(InputStream in, OutputStream out, long length) {
        this.compressor.compress(in, out, length);
    }
    
    public void decompress(InputStream in, OutputStream out) {
        this.compressor.decompress(in, out);
    }
    
}