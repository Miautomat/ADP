package abgabe05;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Before;
import org.junit.Test;

import abgabe05.Compressor;
import abgabe05.Huffman;

public class HuffmanTest {
    
    Compressor huffman;
    
    @Before
    public void setUp() throws Exception {
        this.huffman = new Compressor(new Huffman(8));
    }
    
    @Test
    public void compress() throws Exception {
        Path path = Paths.get("src/abgabe05/resources/data16384.txt");
        long length = Files.size(path);
        InputStream filestream = Files.newInputStream(path);
        OutputStream compressedFile = Files
            .newOutputStream(
                Paths.get("src/abgabe05/resources/compressed/data16384.txt"));
        
        huffman.compress(filestream, compressedFile, length);
    }
    
    @Test
    
    public void decompress() throws Exception {
        InputStream file = Files
            .newInputStream(Paths.get("src/abgabe05/resources/compressed/data16384.txt"));
        OutputStream compressedFile = Files
            .newOutputStream(Paths.get("src/abgabe05/resources/decompressed/data16384.txt"));
        huffman.decompress(file, compressedFile);
    }
    
}