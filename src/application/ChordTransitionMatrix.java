package application;

import java.util.Arrays;

/**
 * Created by praneilkamat on 25/01/2018.
 */
public class ChordTransitionMatrix {
    private double[][] matrix;
    private int numOfChords = 48;

    public ChordTransitionMatrix() {
        matrix = new double[numOfChords][numOfChords];

        // initialise every value in matrix to 1
        int i;
        for (i = 0; i < matrix.length; i++) {
            Arrays.fill(matrix[i], 1);
        }
    }

    public double[] getRow(int index) {
        return matrix[index];
        // need error checking here to see if index is out of bounds
    }
}
