package application.highbackend;

import application.lowbackend.TransitionMatrixGenerator;
import application.lowbackend.MusicGenerator;
import jm.music.data.Score;

import static application.Consts.*;

// helper class for generating the parts that make up a transition between sections
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

    // intepolate a set of parameters anywhere between two sets of parameters
    void interpolateParameters(double[] beforeTransitionParameters, double[] afterTransitionParameters, int iteration, int transitionLength) {
        // ensures each interpolated set of parameters is different to the two sets of parameters being interpolated
        transitionLength += 1;
        iteration += 1;

        double difference;
        int i;
        for (i = 0; i < NUMBER_OF_PARAMETERS; i++) {
            difference = afterTransitionParameters[i] - beforeTransitionParameters[i];
            beforeTransitionParameters[i] += (difference / transitionLength) * iteration;
        }

        parameters = beforeTransitionParameters;
    }

    // generates one bar worth of transition
    int generateTransitionPart(int currentChord) {
        MusicGenerator musicGenerator = new MusicGenerator(key, parameters[PITCH], parameters[TEMPO], parameters[VELOCITY]);
        transitionMatrixGenerator.setFilterValues(parameters[MAJOR_CHORDS], parameters[MINOR_CHORDS], parameters[DIMINISHED_CHORDS],
                                                parameters[DOMINANT_CHORDS], parameters[TONALITY], parameters[MODE], parameters[DIATONIC]);

        double[] row = transitionMatrixGenerator.generateRow(currentChord);
        currentChord = musicGenerator.addToParts(row);

        // add parts to score
        transitionPart.empty();
        transitionPart.addPart(musicGenerator.getChordsPart());
        transitionPart.addPart(musicGenerator.getLeadPart());
        transitionPart.addPart(musicGenerator.getBassPart());

        // return current chord for next bar
        return currentChord;
    }

    Score getTransitionPart() {
        return transitionPart;
    }

    double getTempo() {
        return parameters[TEMPO];
    }
}
