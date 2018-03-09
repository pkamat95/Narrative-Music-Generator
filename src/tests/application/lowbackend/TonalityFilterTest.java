package application.lowbackend;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static application.Consts.*;
import static application.Consts.VIM;
import static org.junit.Assert.*;

public class TonalityFilterTest {
    private TonalityFilter tonalityFilter;
    private double[] row;
    private int delta = 0;

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
                assertEquals(0, row[i], delta);
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
                assertEquals(0, row[i], delta);
            }
            else if (isPreferredChordInMinorMode(i) || isPreferredTransitionInMinorMode(i, 0)) {
                assertEquals(1, row[i], delta);
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
                assertEquals(0, row[i], delta);
            }
            else if (isPreferredChordInMajorMode(i) || isPreferredTransitionInMajorMode(i, 0)) {
                assertEquals(1, row[i], delta);
            }
        }
    }

    @Test
    public void scalesPreferredMajorTransitionsWhenInMajorMode() {
        tonalityFilter.setFilterValue(1);
        tonalityFilter.setMode(1);
        tonalityFilter.filter(row, IIM);

        int i;
        for(i = 0; i < 48; i++) {
            if (isPreferredTransitionInMajorMode(i, IIM)) {
                assertEquals(1.5, row[i], delta);
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
                assertEquals(1.5, row[i], delta);
            }
        }
    }

    @Test
    public void doesNotScaleChordsWhenFilterValueIs0() {
        double[] oldRow = row.clone();
        tonalityFilter.filter(row, 0);

        assertArrayEquals(oldRow, row, delta);
    }

    @Test
    public void doesNotScalePreferredMajorTransitionsWhenFilterValueIs0() {
        double[] oldRow = row.clone();
        tonalityFilter.setMode(1);
        tonalityFilter.filter(row, 0);

        assertArrayEquals(oldRow, row, delta);
    }

    @Test
    public void doesNotScalePreferredMinorTransitionsWhenFilterValueIs0() {
        double[] oldRow = row.clone();
        tonalityFilter.setMode(0);
        tonalityFilter.filter(row, 0);

        assertArrayEquals(oldRow, row, delta);
    }

    private boolean isDiatonic(int c) {
        return ((c == I) || (c == IIM) || (c == IIIM) || (c == IV) || (c == V) || (c == VIM) || (c == VIIO));
    }

    private boolean isPreferredChordInMajorMode(int c) {
        return ((c == V) || (c == V7) || (c == VIIO) || (c == IIF) || (c == I7S));
    }

    private  boolean isPreferredTransitionInMajorMode(int c, int index) {
        switch (index) {
            case IIM: if ((c == V) || (c == V7) || (c == VIIO)) return true; break;
            case IV: if ((c == I) || (c == V) || (c == V7)) return true; break;
            case V: if ((c == I) || (c == VIM)) return true; break;
            case V7: if ((c == I) || (c == VIM)) return true; break;
            case VIIO: if ((c == I) || (c == V) || (c == V7)) return true; break;
            case IIF: if ((c == V) || (c == V7) || (c == I)) return true; break;
            case I7S: if ((c == I)) return true; break;
        }
        return false;
    }

    private boolean isPreferredChordInMinorMode(int c) {
        return ((c == III) || (c == III7) || (c == VOS) || (c == VIIF) || (c == VI7S));
    }

    private boolean isPreferredTransitionInMinorMode(int c, int index) {
        switch (index) {
            case III: if (c == VIM) return true; break;
            case III7: if (c == VIM) return true; break;
            case VIIF: if ((c == III) || (c == III7)) return true; break;
            case VI7S: if (c == VIM) return true; break;
        }
        return false;
    }
}