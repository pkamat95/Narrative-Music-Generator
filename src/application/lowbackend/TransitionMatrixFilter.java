package application.lowbackend;

abstract class TransitionMatrixFilter {
    double filterValue = 0;

    void setFilterValue(double val) {
        if (val < 0) val = 0;
        if (val > 1) val = 1;
        filterValue = val;
    }
}
