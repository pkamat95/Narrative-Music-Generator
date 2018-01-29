package application;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * Created by praneilkamat on 29/01/2018.
 */
public class ChordSelectorTest {
    ChordSelector chordSelector;
    double[] row;

    @Before
    public void setUp() {
        chordSelector = new ChordSelector();
        row = new double[48];
        Arrays.fill(row, 0);
    }

    @Test
    public void selectsCorrectChord() {
        row[24] = 1;

        assertEquals(24, chordSelector.selectChord(row));
    }

}