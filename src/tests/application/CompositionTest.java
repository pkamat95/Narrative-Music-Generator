package application;

import jm.music.data.Part;
import org.junit.Before;
import org.junit.Test;

import static application.Consts.DIATONIC;
import static jm.constants.Pitches.C4;
import static org.junit.Assert.*;

/**
 * Created by praneilkamat on 10/02/2018.
 */
public class CompositionTest {
    Composition composition;
    int sectionLength;
    int numberOfSections;

    @Before
    public void setUp() {
        int key = C4;
        sectionLength = 2;
        numberOfSections = 3;
        Section[] sections = new Section[numberOfSections];
        ValenceArousalModel model = new ValenceArousalModel(0.5, 0.5);
        double[] parameters = model.generateParameters();

        int i;
        for (i = 0; i < numberOfSections; i++) {
            sections[i] = new Section(parameters, key, sectionLength, true, true, 1, 1);
        }

        composition = new Composition(sections);
        composition.generateComposition();
    }

    @Test
    public void scoreContainsThreeParts() {
        assertEquals(3, composition.getScore().getPartArray().length);
    }

    @Test
    public void partsContainCorrectNumberOfPhrases() {
        Part[] parts = composition.getScore().getPartArray();

        assertEquals(numberOfSections * sectionLength * 4, parts[0].size());
        assertEquals(numberOfSections * sectionLength, parts[1].size()); // lead part
        assertEquals(numberOfSections * sectionLength, parts[2].size()); // bass part
    }
}