package application;

import static application.Consts.*;

/**
 * Created by praneilkamat on 02/02/2018.
 */
public class ValenceArousalModel {
    private double valence = 0.5;
    private double arousal = 0.5;

    public ValenceArousalModel() {
        this(0.5, 0.5);
    }

    public ValenceArousalModel(double valence, double arousal) {
        setValence(valence);
        setArousal(arousal);
    }

    public double[] generateParameters() {
        double[] parameters = new double[NUMBER_OF_PARAMETERS];

        // parameters controlled by valence
        parameters[MAJOR_CHORDS] = valence;
        parameters[MINOR_CHORDS] = 1 - valence;
        parameters[DIMINISHED_CHORDS] = 0; // model has no effect on diminished chords
        parameters[DOMINANT_CHORDS] = (1 - valence) * 0.5; // max 0.5
        parameters[TONALITY] = 0.75 + (valence * 0.25); // min 0.75, max 1
        parameters[MODE] = valence;
        parameters[DIATONIC] = 0.75 + (valence * 0.25); // min 0.75, max 1
        parameters[PITCH] = valence * 3; // thresholds for pitch are: under 1, between 1 and 2, and above 2

        // parameters controlled by arousal
        parameters[TEMPO] = 60 + (arousal * 60); // min 60, max 120 bpm
        parameters[VOLUME] = 0.7 + (arousal * 0.3);
        parameters[VELOCITY] = arousal;

        return parameters;
    }

    public double getValence() {
        return valence;
    }

    public double getArousal() {
        return arousal;
    }

    public void setValence(double valence) {
        if (valence < 0) valence = 0;
        if (valence > 1) valence = 1;
        this.valence = valence;
    }

    public void setArousal(double arousal) {
        if (arousal < 0) arousal = 0;
        if (arousal > 1) arousal = 1;
        this.arousal = arousal;
    }
}
