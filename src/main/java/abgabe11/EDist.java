package abgabe11;

/**
 * class to calculate the shortest editing distance from string1 to string2
 * 
 * @see http://www.geeksforgeeks.org/dynamic-programming-set-5-edit-distance/
 */
class EDist {
    static int editDistDP(String str1, String str2, int m, int n) {
        int distanceMatrix[][] = new int[m + 1][n + 1];
        
        // bottom up
        for (int i = 0; i <= m; i++) {
            for (int j = 0; j <= n; j++) {
                // If first string is empty, only option is to
                // insert all characters of second string
                if (i == 0) {
                    distanceMatrix[i][j] = j;// Min. operations = j
                }
                
                // If second string is empty, only option is to
                // remove all characters of second string
                else if (j == 0) {
                    distanceMatrix[i][j] = i;// Min. operations = i
                }
                
                // If last characters are same, ignore last char
                // and recur for remaining string
                else if (str1.charAt(i - 1) == str2.charAt(j - 1)) {
                    distanceMatrix[i][j] = distanceMatrix[i - 1][j - 1];
                }
                // If last character are different, consider all
                // possibilities and find minimum
                else {
                    distanceMatrix[i][j] = 1 + minimalDistance(distanceMatrix[i][j - 1], // Insert
                        distanceMatrix[i - 1][j], // Remove
                        distanceMatrix[i - 1][j - 1]); // Replace
                }
            }
        }
        return distanceMatrix[m][n];
    }
    
    static int minimalDistance(int x, int y, int z) {
        if (x < y && x < z) // x is minimum
            return x;
        if (y < x && y < z) // y is mimimum
            return y;
        else
            return z; // z is minimum
    }
    
    public static void main(String args[]) {
        String str1 = "sunday";
        String str2 = "saturday";
        System.out.println(editDistDP(str1, str2, str1.length(), str2.length()));
    }
}
