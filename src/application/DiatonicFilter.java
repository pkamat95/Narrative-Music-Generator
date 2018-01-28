package application;

import static application.Consts.*;

/**
 * Created by praneilkamat on 26/01/2018.
 */
public class DiatonicFilter extends TransitionMatrixFilter{

    public double[] filter(double[] row) {
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
        if ((c == I) || (c == ii) || (c == iii) || (c == IV) || (c == V) || (c == vi) || (c == viio)) {
            return true;
        }
        return false;
    }
}
