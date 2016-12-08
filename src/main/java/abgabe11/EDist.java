package abgabe11;

/**
 * class to calculate the shortest editing distance from string1 to string2
 * 
 * @see http://www.geeksforgeeks.org/dynamic-programming-set-5-edit-distance/
 */
class EDist {
    static int editDistDP(String str1, String str2, int m, int n) {
        int distMatrix[][] = new int[m + 1][n + 1];
        // jew +1, damit die Reihe mit index 0 auch dabei ist!
        // Matrix muss nur bis gewünschten Punkt erstellt werden
        
        /*
         * bottom up --- i = index for str1(column) --- j = index for str2
         * (line)
         */
        for (int i = 0; i <= m; i++) {
            for (int j = 0; j <= n; j++) {
                /*
                 * sobald i oder j 0 sind, gehen wir bei einer der beiden Seiten
                 * von einem leeren String aus. Somit ist die Editierdistanz
                 * immer i bzw. j bzw. entspricht genau der Anzahl der bisher
                 * betrachteten Buchstaben
                 */
                if (i == 0) {
                    distMatrix[i][j] = j;
                } else if (j == 0) {
                    distMatrix[i][j] = i;
                }
                
                /*
                 * Falls die betrachteten Buchstaben gleich sind, erhöht sich
                 * die editierdistanz von dem feld nicht und wird vom vorigen
                 * übertragen
                 */
                else if (str1.charAt(i - 1) == str2.charAt(j - 1)) {
                    distMatrix[i][j] = distMatrix[i - 1][j - 1];
                }
                /*
                 * Falls sie sich unterscheiden, wird die Editierdistanz um ein
                 * erhöht und die minimale Distanz der umliegenden Distanzen
                 * addiert
                 */
                else {
                    distMatrix[i][j] = 1 + minimalDistance(distMatrix[i][j - 1],
                        distMatrix[i - 1][j],
                        distMatrix[i - 1][j - 1]);
                }
            }
        }
        return distMatrix[m][n];
    }
    
    static int minimalDistance(int x, int y, int z) {
        // muss einmal <= abprüfen falls x = y
        if (x <= y && x < z)
            return x;// x or y is minimum
        if (y < x && y < z)
            return y; // y is mimimum
        else
            return z; // z is minimum
    }
    
    public static void main(String args[]) {
        String str1 = "sunday";
        String str2 = "saturday";
        System.out.println(editDistDP(str1, str2, str1.length(), str2.length()));
    }
}
