package application.lowbackend;

import java.util.Arrays;

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

    // used to reset the transition matrix generator
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
