package application.lowbackend;

import java.util.Arrays;

import static application.Consts.NUMBER_OF_CHORDS;

class ChordTransitionMatrix {
    private double[][] matrix;

    ChordTransitionMatrix() {
        matrix = new double[NUMBER_OF_CHORDS][NUMBER_OF_CHORDS];

        // initialise every value in matrix to 1
        initialise();
    }

    private void initialise() {
        int i;
        for (i = 0; i < matrix.length; i++) {
            Arrays.fill(matrix[i], 1);
        }
    }

    double[] getRow(int index) {
        return matrix[index].clone();
    }

    void setRow(double[] row, int index) {
        matrix[index] = row;
    }
}
