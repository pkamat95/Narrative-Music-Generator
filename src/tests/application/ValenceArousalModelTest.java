package application;

import org.junit.Test;

import static application.Consts.*;
import static org.junit.Assert.*;

/**
 * Created by praneilkamat on 05/02/2018.
 */
public class ValenceArousalModelTest {

    @Test
    public void parametersCorrectWhenMaxValenceMaxArousal() {
        ValenceArousalModel model = new ValenceArousalModel(1, 1);
        double[] parameters = model.generateParameters();
        assertEquals(1, parameters[MAJOR_CHORDS], 0);
        assertEquals(0, parameters[MINOR_CHORDS], 0);
        assertEquals(0, parameters[DIMINISHED_CHORDS], 0);
        assertEquals(0, parameters[DOMINANT_CHORDS], 0);
        assertEquals(1, parameters[TONALITY], 0);
        assertEquals(1, parameters[MODE], 0);
        assertEquals(1, parameters[DIATONIC], 0);
        assertEquals(3, parameters[PITCH], 0);
        assertEquals(120, parameters[TEMPO], 0);
        assertEquals(1, parameters[VOLUME], 0);
        assertEquals(1, parameters[VELOCITY], 0);
    }

    @Test
    public void parametersCorrectWhenMinValenceMinArousal() {
        ValenceArousalModel model = new ValenceArousalModel(0, 0);
        double[] parameters = model.generateParameters();
        assertEquals(0, parameters[MAJOR_CHORDS], 0);
        assertEquals(1, parameters[MINOR_CHORDS], 0);
        assertEquals(0, parameters[DIMINISHED_CHORDS], 0);
        assertEquals(0.5, parameters[DOMINANT_CHORDS], 0);
        assertEquals(0.75, parameters[TONALITY], 0);
        assertEquals(0, parameters[MODE], 0);
        assertEquals(0.75, parameters[DIATONIC], 0);
        assertEquals(0, parameters[PITCH], 0);
        assertEquals(60, parameters[TEMPO], 0);
        assertEquals(0.7, parameters[VOLUME], 0);
        assertEquals(0.4, parameters[VELOCITY], 0);
    }

    @Test
    public void parametersCorrectWhenMaxValenceMinArousal() {
        ValenceArousalModel model = new ValenceArousalModel(1, 0);
        double[] parameters = model.generateParameters();
        assertEquals(1, parameters[MAJOR_CHORDS], 0);
        assertEquals(0, parameters[MINOR_CHORDS], 0);
        assertEquals(0, parameters[DIMINISHED_CHORDS], 0);
        assertEquals(0, parameters[DOMINANT_CHORDS], 0);
        assertEquals(1, parameters[TONALITY], 0);
        assertEquals(1, parameters[MODE], 0);
        assertEquals(1, parameters[DIATONIC], 0);
        assertEquals(3, parameters[PITCH], 0);
        assertEquals(60, parameters[TEMPO], 0);
        assertEquals(0.7, parameters[VOLUME], 0);
        assertEquals(0.4, parameters[VELOCITY], 0);
    }

    @Test
    public void parametersCorrectWhenMinValenceMaxArousal() {
        ValenceArousalModel model = new ValenceArousalModel(0, 1);
        double[] parameters = model.generateParameters();
        assertEquals(0, parameters[MAJOR_CHORDS], 0);
        assertEquals(1, parameters[MINOR_CHORDS], 0);
        assertEquals(0, parameters[DIMINISHED_CHORDS], 0);
        assertEquals(0.5, parameters[DOMINANT_CHORDS], 0);
        assertEquals(0.75, parameters[TONALITY], 0);
        assertEquals(0, parameters[MODE], 0);
        assertEquals(0.75, parameters[DIATONIC], 0);
        assertEquals(0, parameters[PITCH], 0);
        assertEquals(120, parameters[TEMPO], 0);
        assertEquals(1, parameters[VOLUME], 0);
        assertEquals(1, parameters[VELOCITY], 0);
    }

    @Test
    public void valenceSetTo0WhenTooLow() {
        ValenceArousalModel model = new ValenceArousalModel(-1, 0);
        assertEquals(0, model.getValence(), 0);
    }

    @Test
    public void valenceSetTo1WhenTooHigh() {
        ValenceArousalModel model = new ValenceArousalModel(2, 0);
        assertEquals(1, model.getValence(), 0);
    }

    @Test
    public void arousalSetTo0WhenTooLow() {
        ValenceArousalModel model = new ValenceArousalModel(0, -1);
        assertEquals(0, model.getArousal(), 0);
    }

    @Test
    public void arousalSetTo1WhenTooHigh() {
        ValenceArousalModel model = new ValenceArousalModel(0, 2);
        assertEquals(1, model.getArousal(), 0);
    }

}