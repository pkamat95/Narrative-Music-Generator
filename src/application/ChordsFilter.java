package application;

/**
 * Created by praneilkamat on 26/01/2018.
 */
public class ChordsFilter {
    private MajorChordsFilter majorChordsFilter;
    private MinorChordsFilter minorChordsFilter;
    private DiminishedChordsFilter diminishedChordsFilter;
    private DominantChordsFilter dominantChordsFilter;

    public ChordsFilter() {
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

    public MajorChordsFilter getMajorChordsFilter() {
        return majorChordsFilter;
    }

    public MinorChordsFilter getMinorChordsFilter() {
        return minorChordsFilter;
    }

    public DiminishedChordsFilter getDiminishedChordsFilter() {
        return diminishedChordsFilter;
    }

    public DominantChordsFilter getDominantChordsFilter() {
        return dominantChordsFilter;
    }

    public class MajorChordsFilter extends TransitionMatrixFilter {
        public double[] scaleMajorChords(double[] row) {
            return filter(filterValue, row, 0, 12);
        }
    }

    public class MinorChordsFilter extends TransitionMatrixFilter {
        public double[] scaleMinorChords(double[] row) {
            return filter(filterValue, row, 12, 24);
        }
    }

    public class DiminishedChordsFilter extends TransitionMatrixFilter {
        public double[] scaleDiminishedChords(double[] row) {
            return filter(filterValue, row, 24, 36);
        }
    }

    public class DominantChordsFilter extends TransitionMatrixFilter {
        public double[] scaleDominantChords(double[] row) {
            return filter(filterValue, row, 36, 48);
        }
    }
}
