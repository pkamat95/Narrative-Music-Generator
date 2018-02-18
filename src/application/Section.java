package application;

import jm.music.data.Score;

import static application.Consts.*;

/**
 * Created by praneilkamat on 09/02/2018.
 */
public class Section {
    private Score score;
    private TransitionMatrixGenerator transitionMatrixGenerator;
    private double[] parameters;
    private int key;
    private int sectionLength;
    private int startTransitionLength = 0;
    private int endTransitionLength = 0;

    public Section(double[] musicGenerationParameters, int key, int sectionLength, boolean isStart, boolean isEnd, int startTransitionLength, int endTransitionLength) {
        // unless this is the first and/or last section, we need to make room for transitions
        if (!isStart) {
            this.startTransitionLength = startTransitionLength;
            sectionLength -= startTransitionLength;
        }
        if (!isEnd) {
            this.endTransitionLength = endTransitionLength;
            sectionLength -= endTransitionLength;
        }

        // in case section length is too small - user shouldn't be allowed to enter this into the application anyway
        if (sectionLength < 1) {
            sectionLength = 1;
        }

        parameters = musicGenerationParameters;
        this.key = key;
        this.sectionLength = sectionLength;

        score = new Score();

        transitionMatrixGenerator = new TransitionMatrixGenerator(parameters[MAJOR_CHORDS], parameters[MINOR_CHORDS],
                parameters[DIMINISHED_CHORDS], parameters[DOMINANT_CHORDS], parameters[TONALITY], parameters[MODE], parameters[DIATONIC]);
    }

    public int generateSection(int currentChord) {
        MusicGenerator musicGenerator = new MusicGenerator(key, parameters[PITCH], parameters[TEMPO], parameters[VELOCITY]);

        for (int i = 0; i < sectionLength; i++) {
            double[] row = transitionMatrixGenerator.generateRow(currentChord);
            currentChord = musicGenerator.addToParts(row);
        }

        score.empty();
        score.addPart(musicGenerator.getChordsPart());
        score.addPart(musicGenerator.getLeadPart());
        score.addPart(musicGenerator.getBassPart());

        return currentChord;
    }

    public Score getScore() {
        return score.copy();
    }

    public int getSectionLength() {
        return sectionLength;
    }

    public double getTempo() {
        return parameters[TEMPO];
    }

    public int getStartTransitionLength() {
        return startTransitionLength;
    }

    public int getEndTransitionLength() {
        return endTransitionLength;
    }

    public int getKey() {
        return key;
    }

    public double[] getParameters() {
        return parameters.clone();
    }
}
