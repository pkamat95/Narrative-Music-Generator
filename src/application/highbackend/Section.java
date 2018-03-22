package application.highbackend;

import application.lowbackend.TransitionMatrixGenerator;
import application.lowbackend.MusicGenerator;
import jm.music.data.Score;

import static application.Consts.*;

// generates a score for a section
public class Section {
    private Score score;
    private TransitionMatrixGenerator transitionMatrixGenerator;
    private double[] parameters;
    private int key;
    private int sectionLength; // includes transitions
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

    int generateSection(int currentChord) {
        MusicGenerator musicGenerator = new MusicGenerator(key, parameters[PITCH], parameters[TEMPO], parameters[VELOCITY]);

        // generate each chord and add to parts
        for (int i = 0; i < sectionLength; i++) {
            double[] row = transitionMatrixGenerator.generateRow(currentChord);
            currentChord = musicGenerator.addToParts(row);
        }

        // create score from parts
        score.empty();
        score.addPart(musicGenerator.getChordsPart());
        score.addPart(musicGenerator.getLeadPart());
        score.addPart(musicGenerator.getBassPart());

        // return current chord so it can be used for the next transition/section
        return currentChord;
    }

    Score getScore() {
        return score.copy();
    }

    int getSectionLength() {
        return sectionLength;
    }

    double getTempo() {
        return parameters[TEMPO];
    }

    int getStartTransitionLength() {
        return startTransitionLength;
    }

    int getEndTransitionLength() {
        return endTransitionLength;
    }

    int getKey() {
        return key;
    }

    double[] getParameters() {
        return parameters.clone();
    }
}
