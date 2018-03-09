package application.lowbackend;

class ChordsFilter {
    private MajorChordsFilter majorChordsFilter;
    private MinorChordsFilter minorChordsFilter;
    private DiminishedChordsFilter diminishedChordsFilter;
    private DominantChordsFilter dominantChordsFilter;

    ChordsFilter() {
        majorChordsFilter = new MajorChordsFilter();
        minorChordsFilter = new MinorChordsFilter();
        diminishedChordsFilter = new DiminishedChordsFilter();
        dominantChordsFilter = new DominantChordsFilter();
    }

    private double[] filter(double filterValue, double[] row, int start, int end) {
        int i;
        for (i = start; i < end; i++) {
            // when set to 0, chords don't scale at all, when set to 1, chords scale by 3
            row[i] *= 1 + (filterValue * 2);
        }
        return row;
    }

    MajorChordsFilter getMajorChordsFilter() {
        return majorChordsFilter;
    }

    MinorChordsFilter getMinorChordsFilter() {
        return minorChordsFilter;
    }

    DiminishedChordsFilter getDiminishedChordsFilter() {
        return diminishedChordsFilter;
    }

    DominantChordsFilter getDominantChordsFilter() {
        return dominantChordsFilter;
    }

    class MajorChordsFilter extends TransitionMatrixFilter {
        double[] scaleMajorChords(double[] row) {
            return filter(filterValue, row, 0, 12);
        }
    }

    class MinorChordsFilter extends TransitionMatrixFilter {
        double[] scaleMinorChords(double[] row) {
            return filter(filterValue, row, 12, 24);
        }
    }

    class DiminishedChordsFilter extends TransitionMatrixFilter {
        double[] scaleDiminishedChords(double[] row) {
            return filter(filterValue, row, 24, 36);
        }
    }

    class DominantChordsFilter extends TransitionMatrixFilter {
        double[] scaleDominantChords(double[] row) {
            return filter(filterValue, row, 36, 48);
        }
    }
}
