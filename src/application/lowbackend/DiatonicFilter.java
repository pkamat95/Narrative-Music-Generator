package application.lowbackend;

import static application.Consts.*;

class DiatonicFilter extends TransitionMatrixFilter {

    double[] filter(double[] row) {
        int i;
        for(i = 0; i < row.length; i++) {
            // don't scale down diatonic chords
            if (isDiatonic(i)) {
                continue;
            }
            // scale all non-diatonic weights towards 0
            row[i] *= 1 - filterValue; // filter value of 1 will make all non-diatonic chords 0
        }
        return row;
    }

    private boolean isDiatonic(int c) {
        return ((c == I) || (c == IIM) || (c == IIIM) || (c == IV) || (c == V) || (c == VIM) || (c == VIIO));
    }
}
