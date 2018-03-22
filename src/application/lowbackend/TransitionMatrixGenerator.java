package application.lowbackend;

import java.util.Arrays;

// builds up a chord transition matrix
public class TransitionMatrixGenerator {
    private ChordTransitionMatrix transitionMatrix;
    private boolean[] rowsComplete;
    private ChordsFilter chordsFilter;
    private TonalityFilter tonalityFilter;
    private DiatonicFilter diatonicFilter;

    public TransitionMatrixGenerator () {
        this(0, 0, 0, 0, 0.5, 0.5, 0.5);
    }

    public TransitionMatrixGenerator (double majorChords, double minorChords, double diminishedChords,
                                     double dominantChords, double tonality, double mode, double diatonic) {
        chordsFilter = new ChordsFilter();
        tonalityFilter = new TonalityFilter();
        diatonicFilter = new DiatonicFilter();

        setFilterValues(majorChords, minorChords, diminishedChords, dominantChords, tonality, mode, diatonic);
    }

    // generates a single row in the chord transition matrix using the filters and the filter values provided
    // for efficiency, a row will only be generated if it has not already been generated
    public double[] generateRow(int rowIndex) {
        // generate row if it hasn't already been generated (apply each filter to the row)
        if (!rowsComplete[rowIndex]) {
            double[] row = transitionMatrix.getRow(rowIndex);
            chordsFilter.getMajorChordsFilter().scaleMajorChords(row);
            chordsFilter.getMinorChordsFilter().scaleMinorChords(row);
            chordsFilter.getDiminishedChordsFilter().scaleDiminishedChords(row);
            chordsFilter.getDominantChordsFilter().scaleDominantChords(row);

            tonalityFilter.filter(row, rowIndex);

            diatonicFilter.filter(row);

            transitionMatrix.setRow(row, rowIndex);
            rowsComplete[rowIndex] = true;
        }
        return transitionMatrix.getRow(rowIndex);
    }

    ChordTransitionMatrix getTransitionMatrix() {
        return transitionMatrix;
    }

    // changing filter values will reset the chord transition matrix as it will be invalidated
    public void setFilterValues(double majorChords, double minorChords, double diminishedChords,
                               double dominantChords, double tonality, double mode, double diatonic) {
        transitionMatrix = new ChordTransitionMatrix();

        rowsComplete = new boolean[transitionMatrix.getRow(0).length];
        Arrays.fill(rowsComplete, false);

        chordsFilter.getMajorChordsFilter().setFilterValue(majorChords);
        chordsFilter.getMinorChordsFilter().setFilterValue(minorChords);
        chordsFilter.getDiminishedChordsFilter().setFilterValue(diminishedChords);
        chordsFilter.getDominantChordsFilter().setFilterValue(dominantChords);

        tonalityFilter.setFilterValue(tonality);
        tonalityFilter.setMode(mode);

        diatonicFilter.setFilterValue(diatonic);
    }
}
