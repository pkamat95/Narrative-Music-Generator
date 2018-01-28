package application;

import static application.Consts.*;

/**
 * Created by praneilkamat on 27/01/2018.
 */
public class TonalityFilter extends TransitionMatrixFilter{
    double mode = 0.5; // 0 is minor, 1 is major

    public double[] filter(double[] row, int rowIndex) {

        int i;
        for(i = 0; i < row.length; i++) {
            // scale all major mode chords/transitions towards 0 as mode is more minor
            if (isPreferredChordInMajorMode(i) || isPreferredTransitionInMajorMode(i, rowIndex)) {
                row[i] *= mode + ((1 - mode) * (1 - filterValue)); // filterValue controls how much mode has an effect
            }

            // scale all minor mode chords/transitions towards 0 as mode is more major
            else if (isPreferredChordInMinorMode(i) || isPreferredTransitionInMinorMode(i, rowIndex)) {
                row[i] *= (1 - mode) + (mode * (1 - filterValue));
            }

            // scale all other diatonic chords such that they scale down the least whether mode is more major or more minor
            else if (isDiatonic(i)) {
                double sf = 0.5;
                if (mode > 0.5) sf = mode; // if mode is major
                if (mode < 0.5) sf = (1 - mode); // if mode is minor
                row[i] *= sf + ((1 - sf) * (1 - filterValue));
            }

            // scale all other chords to 0
            else {
                row[i] *= 1 - filterValue;
            }
        }

        // scale up preferred transitions for major mode
        if (mode > 0.5) {
            double sf = 0.5 + mode;
            for (i = 0; i < row.length; i++) {
                if (isPreferredTransitionInMajorMode(i, rowIndex)) {
                    row[i] *= sf + ((1 - sf) * (1 - filterValue)); // scales to a max of 1.5
                }
            }
        }
        // scale up preferred transitions for minor mode
        else if (mode < 0.5) {
            double sf = 0.5 + (1 - mode);
            for (i = 0; i < row.length; i++) {
                if (isPreferredTransitionInMinorMode(i, rowIndex)) {
                    row[i] *= sf + ((1 - sf) * (1 - filterValue)); // scales to a max of 1.5
                }
            }
        }

        return row;
    }

    public void setMode(double val) {
        if (val < 0) val = 0;
        if (val > 1) val = 1;
        mode = val;
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
