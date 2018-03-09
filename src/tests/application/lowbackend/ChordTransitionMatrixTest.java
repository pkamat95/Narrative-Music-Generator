package application.lowbackend;

import org.junit.Test;

import static org.junit.Assert.*;

public class ChordTransitionMatrixTest {

    @Test
    public void chordTransitionMatrixInitialisesCorrectly() {
        ChordTransitionMatrix chordTransitionMatrix = new ChordTransitionMatrix();
        int delta = 0;

        int i, j;
        for (i = 0; i < 48; i++) {
            for(j = 0; j < 48; j++) {
                assertEquals(1, chordTransitionMatrix.getRow(i)[j], delta);
            }
        }
    }

}