package application.lowbackend;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static application.Consts.*;
import static org.junit.Assert.*;

public class DiatonicFilterTest {
    private DiatonicFilter diatonicFilter;
    private double[] row;
    private int delta = 0;

    @Before
    public void setUp() {
        diatonicFilter = new DiatonicFilter();
        row = new double[48];
        Arrays.fill(row, 1);
    }

    @Test
    public void diatonicFilterDoesNotScaleNonDiatonicChordsWhenFilterValueIs0() {
        double[] oldRow = row.clone();
        diatonicFilter.filter(row);

        assertArrayEquals(oldRow, row, delta);
    }

    @Test
    public void diatonicFilterScalesNonDiatonicChords() {
        diatonicFilter.setFilterValue(1);
        diatonicFilter.filter(row);

        int i;
        for (i = 0; i < 48; i++) {
            if ((i == I) || (i == IIM) || (i == IIIM) || (i == IV) || (i == V) || (i == VIM) || (i == VIIO)) {
                assertEquals(1, row[i], delta);
            }
            else {
                assertEquals(0, row[i], delta);
            }
        }
    }
}