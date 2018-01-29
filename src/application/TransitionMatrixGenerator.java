package application;

import java.util.Arrays;

/**
 * Created by praneilkamat on 27/01/2018.
 */
public class TransitionMatrixGenerator {
    private ChordTransitionMatrix transitionMatrix;
    private boolean[] rowsComplete;
    private ChordsFilter chordsFilter;
    private TonalityFilter tonalityFilter;
    private DiatonicFilter diatonicFilter;

    public TransitionMatrixGenerator (double majorChords, double minorChords, double diminishedChords,
                                     double dominantChords, double tonality, double mode, double diatonic) {
        transitionMatrix = new ChordTransitionMatrix();

        rowsComplete = new boolean[transitionMatrix.getRow(0).length];
        Arrays.fill(rowsComplete, false);

        chordsFilter = new ChordsFilter();
        chordsFilter.getMajorChordsFilter().setFilterValue(majorChords);
        chordsFilter.getMinorChordsFilter().setFilterValue(minorChords);
        chordsFilter.getDiminishedChordsFilter().setFilterValue(diminishedChords);
        chordsFilter.getDominantChordsFilter().setFilterValue(dominantChords);

        tonalityFilter = new TonalityFilter();
        tonalityFilter.setFilterValue(tonality);
        tonalityFilter.setMode(mode);

        diatonicFilter = new DiatonicFilter();
        diatonicFilter.setFilterValue(diatonic);
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

    public ChordTransitionMatrix getTransitionMatrix() {
        return transitionMatrix;
    }
}
