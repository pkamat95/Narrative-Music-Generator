package application.lowbackend;

import org.junit.Test;

import static application.Consts.*;
import static org.junit.Assert.*;

public class ValenceArousalModelTest {
    private int delta = 0;

    @Test
    public void parametersCorrectWhenMaxValenceMaxArousal() {
        ValenceArousalModel model = new ValenceArousalModel(1, 1);
        double[] parameters = model.generateParameters();
        assertEquals(1, parameters[MAJOR_CHORDS], delta);
        assertEquals(0, parameters[MINOR_CHORDS], delta);
        assertEquals(0, parameters[DIMINISHED_CHORDS], delta);
        assertEquals(0, parameters[DOMINANT_CHORDS], delta);
        assertEquals(1, parameters[TONALITY], delta);
        assertEquals(1, parameters[MODE], delta);
        assertEquals(1, parameters[DIATONIC], delta);
        assertEquals(3, parameters[PITCH], delta);
        assertEquals(120, parameters[TEMPO], delta);
        assertEquals(1, parameters[VOLUME], delta);
        assertEquals(1, parameters[VELOCITY], delta);
    }

    @Test
    public void parametersCorrectWhenMinValenceMinArousal() {
        ValenceArousalModel model = new ValenceArousalModel(0, 0);
        double[] parameters = model.generateParameters();
        assertEquals(0, parameters[MAJOR_CHORDS], delta);
        assertEquals(1, parameters[MINOR_CHORDS], delta);
        assertEquals(0, parameters[DIMINISHED_CHORDS], delta);
        assertEquals(0.5, parameters[DOMINANT_CHORDS], delta);
        assertEquals(0.75, parameters[TONALITY], delta);
        assertEquals(0, parameters[MODE], delta);
        assertEquals(0.75, parameters[DIATONIC], delta);
        assertEquals(0, parameters[PITCH], delta);
        assertEquals(60, parameters[TEMPO], delta);
        assertEquals(0.7, parameters[VOLUME], delta);
        assertEquals(0, parameters[VELOCITY], delta);
    }

    @Test
    public void parametersCorrectWhenMaxValenceMinArousal() {
        ValenceArousalModel model = new ValenceArousalModel(1, 0);
        double[] parameters = model.generateParameters();
        assertEquals(1, parameters[MAJOR_CHORDS], delta);
        assertEquals(0, parameters[MINOR_CHORDS], delta);
        assertEquals(0, parameters[DIMINISHED_CHORDS], delta);
        assertEquals(0, parameters[DOMINANT_CHORDS], delta);
        assertEquals(1, parameters[TONALITY], delta);
        assertEquals(1, parameters[MODE], delta);
        assertEquals(1, parameters[DIATONIC], delta);
        assertEquals(3, parameters[PITCH], delta);
        assertEquals(60, parameters[TEMPO], delta);
        assertEquals(0.7, parameters[VOLUME], delta);
        assertEquals(0, parameters[VELOCITY], delta);
    }

    @Test
    public void parametersCorrectWhenMinValenceMaxArousal() {
        ValenceArousalModel model = new ValenceArousalModel(0, 1);
        double[] parameters = model.generateParameters();
        assertEquals(0, parameters[MAJOR_CHORDS], delta);
        assertEquals(1, parameters[MINOR_CHORDS], delta);
        assertEquals(0, parameters[DIMINISHED_CHORDS], delta);
        assertEquals(0.5, parameters[DOMINANT_CHORDS], delta);
        assertEquals(0.75, parameters[TONALITY], delta);
        assertEquals(0, parameters[MODE], delta);
        assertEquals(0.75, parameters[DIATONIC], delta);
        assertEquals(0, parameters[PITCH], delta);
        assertEquals(120, parameters[TEMPO], delta);
        assertEquals(1, parameters[VOLUME], delta);
        assertEquals(1, parameters[VELOCITY], delta);
    }

    @Test
    public void valenceSetTo0WhenTooLow() {
        ValenceArousalModel model = new ValenceArousalModel(-1, 0);
        assertEquals(0, model.getValence(), delta);
    }

    @Test
    public void valenceSetTo1WhenTooHigh() {
        ValenceArousalModel model = new ValenceArousalModel(2, 0);
        assertEquals(1, model.getValence(), delta);
    }

    @Test
    public void arousalSetTo0WhenTooLow() {
        ValenceArousalModel model = new ValenceArousalModel(0, -1);
        assertEquals(0, model.getArousal(), delta);
    }

    @Test
    public void arousalSetTo1WhenTooHigh() {
        ValenceArousalModel model = new ValenceArousalModel(0, 2);
        assertEquals(1, model.getArousal(), delta);
    }

}