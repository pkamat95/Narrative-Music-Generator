package application;

/**
 * Created by praneilkamat on 26/01/2018.
 */
public abstract class TransitionMatrixFilter {
    protected double filterValue = 0;

    public void setFilterValue(double val) {
        if (val < 0) val = 0;
        if (val > 1) val = 1;
        filterValue = val;
    }
}
