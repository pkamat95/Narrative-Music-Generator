package application;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by praneilkamat on 29/01/2018.
 */
public class ChordTransitionMatrixTest {

    @Test
    public void chordTransitionMatrixInitialisesCorrectly() {
        ChordTransitionMatrix chordTransitionMatrix = new ChordTransitionMatrix();

        int i, j;
        for (i = 0; i < 48; i++) {
            for(j = 0; j < 48; j++) {
                assertEquals(1, chordTransitionMatrix.getRow(i)[j], 0);
            }
        }
    }

}