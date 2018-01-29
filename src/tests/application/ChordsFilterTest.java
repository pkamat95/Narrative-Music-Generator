package application;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * Created by praneilkamat on 29/01/2018.
 */
public class ChordsFilterTest {
    ChordsFilter chordsFilter;
    double[] row;

    @Before
    public void setUp() {
        chordsFilter = new ChordsFilter();
        row = new double[48];
        Arrays.fill(row, 1);
    }

    @Test
    public void majorChordsFilterDoesNotScaleMajorChordsWhenFilterValueIs0() {
        ChordsFilter.MajorChordsFilter majorChordsFilter = chordsFilter.getMajorChordsFilter();
        double[] oldRow = row.clone();
        majorChordsFilter.scaleMajorChords(row);

        assertArrayEquals(oldRow, row, 0);
    }

    @Test
    public void majorChordsFilterScalesMajorChords() {
        ChordsFilter.MajorChordsFilter majorChordsFilter = chordsFilter.getMajorChordsFilter();
        majorChordsFilter.setFilterValue(1);
        majorChordsFilter.scaleMajorChords(row);

        int i;
        for(i = 0; i < 12; i++) {
            assertEquals(3, row[i], 0);
        }
        for(i = 12; i < 48; i++) {
            assertEquals(1, row[i], 0);
        }
    }

    @Test
    public void minorChordsFilterDoesNotScaleMinorChordsWhenFilterValueIs0() {
        ChordsFilter.MinorChordsFilter minorChordsFilter = chordsFilter.getMinorChordsFilter();
        double[] oldRow = row.clone();
        minorChordsFilter.scaleMinorChords(row);

        assertArrayEquals(oldRow, row, 0);
    }

    @Test
    public void minorChordsFilterScalesMinorChords() {
        ChordsFilter.MinorChordsFilter minorChordsFilter = chordsFilter.getMinorChordsFilter();
        minorChordsFilter.setFilterValue(1);
        minorChordsFilter.scaleMinorChords(row);

        int i;
        for(i = 0; i < 12; i++) {
            assertEquals(1, row[i], 0);
        }
        for(i = 12; i < 24; i++) {
            assertEquals(3, row[i], 0);
        }
        for(i = 24; i < 48; i++) {
            assertEquals(1, row[i], 0);
        }
    }

    @Test
    public void diminishedChordsFilterDoesNotScaleDiminishedChordsWhenFilterValueIs0() {
        ChordsFilter.DiminishedChordsFilter diminishedChordsFilter = chordsFilter.getDiminishedChordsFilter();
        double[] oldRow = row.clone();
        diminishedChordsFilter.scaleDiminishedChords(row);

        assertArrayEquals(oldRow, row, 0);
    }

    @Test
    public void diminishedChordsFilterScalesDiminishedChords() {
        ChordsFilter.DiminishedChordsFilter diminishedChordsFilter = chordsFilter.getDiminishedChordsFilter();
        diminishedChordsFilter.setFilterValue(1);
        diminishedChordsFilter.scaleDiminishedChords(row);

        int i;
        for(i = 0; i < 24; i++) {
            assertEquals(1, row[i], 0);
        }
        for(i = 24; i < 36; i++) {
            assertEquals(3, row[i], 0);
        }
        for(i = 36; i < 48; i++) {
            assertEquals(1, row[i], 0);
        }
    }

    @Test
    public void dominantChordsFilterDoesNotScaleDominantChordsWhenFilterValueIs0() {
        ChordsFilter.DominantChordsFilter dominantChordsFilter = chordsFilter.getDominantChordsFilter();
        double[] oldRow = row.clone();
        dominantChordsFilter.scaleDominantChords(row);

        assertArrayEquals(oldRow, row, 0);
    }

    @Test
    public void dominantChordsFilterScalesDominantChords() {
        ChordsFilter.DominantChordsFilter dominantChordsFilter = chordsFilter.getDominantChordsFilter();
        dominantChordsFilter.setFilterValue(1);
        dominantChordsFilter.scaleDominantChords(row);

        int i;
        for(i = 0; i < 36; i++) {
            assertEquals(1, row[i], 0);
        }
        for(i = 36; i < 48; i++) {
            assertEquals(3, row[i], 0);
        }
    }
}