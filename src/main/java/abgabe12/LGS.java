package abgabe12;

import java.util.Arrays;

import Jama.Matrix;

/**
 * @author Mimi
 * @see https://www2.htw-dresden.de/~iwe/grundlagen/java/galileo/java_050006.htm
 */
public class LGS {
    private double[][] matrixArray;
    private double[][] resultArray;
    private String resultString;
    
    public LGS() {
        double[][] beaches = new double[][] {
            {0.0, 0.5, 0.5},
            {0.3, 0.0, 0.7},
            {0.2, 0.3, 0.5}
        };
        
        // diagonale -1 (Ergebnisse sind 0)
        int accu = 0;
        for (double[] i : beaches) {
            i[accu] -= 1;
            accu++;
        }
        
        // Matrix um eine Zeile erweitern mit 1,1,1 = 1
        matrixArray = new double[4][3];
        // Zeilen und Spalten aus beaches vertauschen und eintragen
        int mI = 0;
        int mJ = 0;
        for (double[] i : beaches) {
            for (double j : i) {
                matrixArray[mJ][mI] = j;
                mJ++;
            }
            mJ = 0;
            mI++;
        }
        // letzte Zeile eintragen
        for (int i = 0; i < 3; i++) {
            matrixArray[3][i] = 1;
        }
        
        // Vektor = 0,0,0,1
        double[][] vektor = new double[][] {
            {0},
            {0},
            {0},
            {1}
        };
        
        // Matrixoperationen
        Matrix m = new Matrix(matrixArray);
        Matrix v = new Matrix(vektor);
        Matrix resM = m.solve(v);
        
        // Ergebnis zurück in einen Array schreiben
        resultArray = resM.getArray();
        resultString = Arrays.deepToString(resultArray);
    }
    
    public static void main(String[] args) {
        LGS l = new LGS();
        System.out.println(l.resultString);
    }
}
