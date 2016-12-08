package abgabe09;

/**
 * The {@code markovseq} class provides static methods for generating a random sequence bases on a Markov system.
 *  @author Michael Köhler-Bussmeier
 */

public class Markovseq {
    
    public static void main(String[] args) {
        
        int numberOfGeneratedBits = 100000;
        if (args.length == 1) {
            numberOfGeneratedBits = Integer.parseInt(args[0]);
        }
        
        double matrix[][] = {
            {0.0, 1.0, 0.00, 0.00},
            {0.0, 0.0, 1.0, 0.0},
            {0.0, 0.0, 0.0, 1.0},
            {1.0, 0.0, 0.0, 0.00}
        };
        
        /*
         * double matrix1[][] = { { 0.25, 0.25, 0.25, 0.25 }, { 0.25, 0.25,
         * 0.25, 0.25 }, { 0.25, 0.25, 0.25, 0.25 }, { 0.25, 0.25, 0.25, 0.25 }
         * };
         */
        /*
         * double matrix2[][] = { { 0.0, 1.0, 0.0, 0.0 }, { 0.0, 0.0, 1.0, 0.0
         * }, { 0.0, 0.0, 0.0, 1.0 }, { 1.0, 0.0, 0.0, 0.0 } };
         */
        
        // deterministischer Startzustand
        int state = 0;
        
        for (int i = 0; i < numberOfGeneratedBits; i++) {
            state = StdRandom.discrete(matrix[state]);
            
            // Moore-Ausgabe: State --> 0 oder 1
            System.out.print(state);
        }
        System.out.println("");
    }// end-of-main
    
}
