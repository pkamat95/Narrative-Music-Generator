package application;

import java.util.Arrays;

/**
 * Created by praneilkamat on 27/01/2018.
 */
public class TransitionMatrixGenerator {
    private ChordTransitionMatrix transitionMatix;
    private boolean[] rowsComplete;
    private ChordsFilter chordsFilter;
    private TonalityFilter tonalityFilter;
    private DiatonicFilter diatonicFilter;

    public TransitionMatrixGenerator(double majorChords, double minorChords, double diminishedChords,
                                     double dominantChords, double tonality, double mode, double diatonic) {
        transitionMatix = new ChordTransitionMatrix();

        rowsComplete = new boolean[transitionMatix.getRow(0).length];
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
            double[] row = transitionMatix.getRow(rowIndex);
            row = chordsFilter.getMajorChordsFilter().scaleMajorChords(row);
            row = chordsFilter.getMinorChordsFilter().scaleMinorChords(row);
            row = chordsFilter.getDiminishedChordsFilter().scaleDiminishedChords(row);
            row = chordsFilter.getDominantChordsFilter().scaleDominantChords(row);

            row = tonalityFilter.filter(row, rowIndex);

            row = diatonicFilter.filter(row);

            transitionMatix.setRow(row, rowIndex);
            rowsComplete[rowIndex] = true;
        }
        return transitionMatix.getRow(rowIndex);
    }

    public ChordTransitionMatrix getTransitionMatrix() {
        return transitionMatix;
    }
}
