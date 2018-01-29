package application;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static application.Consts.*;
import static org.junit.Assert.*;

/**
 * Created by praneilkamat on 29/01/2018.
 */
public class DiatonicFilterTest {
    DiatonicFilter diatonicFilter;
    double[] row;

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

        assertArrayEquals(oldRow, row, 0);
    }

    @Test
    public void diatonicFilterScalesNonDiatonicChords() {
        diatonicFilter.setFilterValue(1);
        diatonicFilter.filter(row);

        int i;
        for (i = 0; i < 48; i++) {
            if ((i == I) || (i == ii) || (i == iii) || (i == IV) || (i == V) || (i == vi) || (i == viio)) {
                assertEquals(1, row[i], 0);
            }
            else {
                assertEquals(0, row[i], 0);
            }
        }
    }
}