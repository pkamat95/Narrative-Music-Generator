package application;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static application.Consts.*;
import static application.Consts.vi;
import static org.junit.Assert.*;

/**
 * Created by praneilkamat on 29/01/2018.
 */
public class TonalityFilterTest {
    TonalityFilter tonalityFilter;
    double[] row;

    @Before
    public void setUp() {
        tonalityFilter = new TonalityFilter();
        row = new double[48];
        Arrays.fill(row, 1);
    }

    @Test
    public void scalesAllNonDiatonicAndNonPreferredChords() {
        tonalityFilter.setFilterValue(1);
        tonalityFilter.filter(row, 0);

        int i;
        for(i = 0; i < 48; i++) {
            if (isDiatonic(i) || isPreferredChordInMajorMode(i) || isPreferredChordInMinorMode(i)
                    || isPreferredTransitionInMajorMode(i, 0) || isPreferredTransitionInMinorMode(i, 0)) {
                assertNotEquals(0, row[i], 0);
            }
            else {
                assertEquals(0, row[i], 0);
            }
        }
    }

    @Test
    public void scalesAllPreferredMajorChordsAndTransitionsWhenInMinorMode() {
        tonalityFilter.setFilterValue(1);
        tonalityFilter.setMode(0);
        tonalityFilter.filter(row, 0);

        int i;
        for(i = 0; i < 48; i++) {
            if (isPreferredChordInMajorMode(i) || isPreferredTransitionInMajorMode(i, 0)) {
                assertEquals(0, row[i], 0);
            }
            else if (isPreferredChordInMinorMode(i) || isPreferredTransitionInMinorMode(i, 0)) {
                assertEquals(1, row[i], 0);
            }
        }
    }

    @Test
    public void scalesAllPreferredMinorChordsAndTransitionsWhenInMajorMode() {
        tonalityFilter.setFilterValue(1);
        tonalityFilter.setMode(1);
        tonalityFilter.filter(row, 0);

        int i;
        for(i = 0; i < 48; i++) {
            if (isPreferredChordInMinorMode(i) || isPreferredTransitionInMinorMode(i, 0)) {
                assertEquals(0, row[i], 0);
            }
            else if (isPreferredChordInMajorMode(i) || isPreferredTransitionInMajorMode(i, 0)) {
                assertEquals(1, row[i], 0);
            }
        }
    }

    @Test
    public void scalesPreferredMajorTransitionsWhenInMajorMode() {
        tonalityFilter.setFilterValue(1);
        tonalityFilter.setMode(1);
        tonalityFilter.filter(row, ii);

        int i;
        for(i = 0; i < 48; i++) {
            if (isPreferredTransitionInMajorMode(i, ii)) {
                assertEquals(1.5, row[i], 0);
            }
        }
    }

    @Test
    public void scalesPreferredMinorTransitionsWhenInMinorMode() {
        tonalityFilter.setFilterValue(1);
        tonalityFilter.setMode(0);
        tonalityFilter.filter(row, III);

        int i;
        for (i = 0; i < 48; i++) {
            if (isPreferredTransitionInMinorMode(i, III)) {
                assertEquals(1.5, row[i], 0);
            }
        }
    }

    @Test
    public void doesNotScaleChordsWhenFilterValueIs0() {
        double[] oldRow = row.clone();
        tonalityFilter.filter(row, 0);

        assertArrayEquals(oldRow, row, 0);
    }

    @Test
    public void doesNotScalePreferredMajorTransitionsWhenFilterValueIs0() {
        double[] oldRow = row.clone();
        tonalityFilter.setMode(1);
        tonalityFilter.filter(row, 0);

        assertArrayEquals(oldRow, row, 0);
    }

    @Test
    public void doesNotScalePreferredMinorTransitionsWhenFilterValueIs0() {
        double[] oldRow = row.clone();
        tonalityFilter.setMode(0);
        tonalityFilter.filter(row, 0);

        assertArrayEquals(oldRow, row, 0);
    }

    private boolean isDiatonic(int c) {
        if ((c == I) || (c == ii) || (c == iii) || (c == IV) || (c == V) || (c == vi) || (c == viio)) {
            return true;
        }
        return false;
    }

    private boolean isPreferredChordInMajorMode(int c) {
        if ((c == V) || (c == V7) || (c == viio) || (c == IIF) || (c == I7S)) {
            return true;
        }
        return false;
    }

    private  boolean isPreferredTransitionInMajorMode(int c, int index) {
        switch (index) {
            case ii: if ((c == V) || (c == V7) || (c == viio)) return true; break;
            case IV: if ((c == I) || (c == V) || (c == V7)) return true; break;
            case V: if ((c == I) || (c == vi)) return true; break;
            case V7: if ((c == I) || (c == vi)) return true; break;
            case viio: if ((c == I) || (c == V) || (c == V7)) return true; break;
            case IIF: if ((c == V) || (c == V7) || (c == I)) return true; break;
            case I7S: if ((c == I)) return true; break;
        }
        return false;
    }

    private boolean isPreferredChordInMinorMode(int c) {
        if ((c == III) || (c == III7) || (c == voS) || (c == VIIF) || (c == VI7S)) {
            return true;
        }
        return false;
    }

    private boolean isPreferredTransitionInMinorMode(int c, int index) {
        switch (index) {
            case III: if (c == vi) return true; break;
            case III7: if (c == vi) return true; break;
            case VIIF: if ((c == III) || (c == III7)) return true; break;
            case VI7S: if (c == vi) return true; break;
        }
        return false;
    }
}