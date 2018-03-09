package application.lowbackend;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class  TransitionMatrixGeneratorTest {
    @Test
    public void generateRowDoesNotAlterRowIfAlreadyGenerated() {
        TransitionMatrixGenerator transitionMatrixGenerator = new TransitionMatrixGenerator(
                1, 1, 1, 1, 0.5, 0.5, 0.5);
        double[] row = transitionMatrixGenerator.getTransitionMatrix().getRow(0).clone();
        double[] newRow = transitionMatrixGenerator.generateRow(0).clone();

        // row altered after initial generation
        assertFalse(Arrays.equals(row, newRow));

        transitionMatrixGenerator.generateRow(0);

        // row not altered if generated again
        assertArrayEquals(newRow, transitionMatrixGenerator.getTransitionMatrix().getRow(0), 0);
    }
}