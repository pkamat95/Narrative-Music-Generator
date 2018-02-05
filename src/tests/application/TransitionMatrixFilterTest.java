package application;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by praneilkamat on 05/02/2018.
 */
public class TransitionMatrixFilterTest {
    DiatonicFilter diatonicFilter;
    @Before
    public void setUp() {
        diatonicFilter = new DiatonicFilter();
    }

    @Test
    public void filterValueSetTo0WhenTooLow() {
        diatonicFilter.setFilterValue(-1);
        assertEquals(0, diatonicFilter.filterValue, 0);
    }

    @Test
    public void filterValueSetTo1WhenTooHigh() {
        diatonicFilter.setFilterValue(2);
        assertEquals(1, diatonicFilter.filterValue, 0);
    }

}