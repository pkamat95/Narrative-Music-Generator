package application.highbackend;

import application.lowbackend.TransitionMatrixGenerator;
import application.lowbackend.MusicGenerator;
import jm.music.data.Score;

import static application.Consts.*;

class TransitionHelper {
    private int key;
    private TransitionMatrixGenerator transitionMatrixGenerator;
    private double[] parameters;
    private Score transitionPart;

    TransitionHelper(int key) {
        this.key = key;
        transitionMatrixGenerator = new TransitionMatrixGenerator();
        transitionPart = new Score();
    }

    void interpolateParameters(double[] beforeTransitionParameters, double[] afterTransitionParameters, int iteration, int transitionLength) {
        double difference;
        int i;
        for (i = 0; i < NUMBER_OF_PARAMETERS; i++) {
            difference = afterTransitionParameters[i] - beforeTransitionParameters[i];
            beforeTransitionParameters[i] += (difference / transitionLength) * iteration;
        }

        parameters = beforeTransitionParameters;
    }

    int generateTransitionPart(int currentChord) {
        MusicGenerator musicGenerator = new MusicGenerator(key, parameters[PITCH], parameters[TEMPO], parameters[VELOCITY]);
        transitionMatrixGenerator.setFilterValues(parameters[MAJOR_CHORDS], parameters[MINOR_CHORDS], parameters[DIMINISHED_CHORDS],
                                                parameters[DOMINANT_CHORDS], parameters[TONALITY], parameters[MODE], parameters[DIATONIC]);

        double[] row = transitionMatrixGenerator.generateRow(currentChord);
        currentChord = musicGenerator.addToParts(row);

        transitionPart.empty();
        transitionPart.addPart(musicGenerator.getChordsPart());
        transitionPart.addPart(musicGenerator.getLeadPart());
        transitionPart.addPart(musicGenerator.getBassPart());

        return currentChord;
    }

    Score getTransitionPart() {
        return transitionPart;
    }

    double getTempo() {
        return parameters[TEMPO];
    }
}
