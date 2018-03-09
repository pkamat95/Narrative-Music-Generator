package application.highbackend;

import application.lowbackend.ValenceArousalModel;
import jm.music.data.Part;
import org.junit.Before;
import org.junit.Test;

import static jm.constants.Pitches.C4;
import static org.junit.Assert.*;

public class CompositionTest {
    private Composition composition;
    private int sectionLength;
    private int numberOfSections;

    @Before
    public void setUp() {
        sectionLength = 2;
        numberOfSections = 3;
        Section[] sections = new Section[numberOfSections];
        ValenceArousalModel model = new ValenceArousalModel(0.5, 0.5);
        double[] parameters = model.generateParameters();

        int i;
        for (i = 0; i < numberOfSections; i++) {
            sections[i] = new Section(parameters, C4, sectionLength, true, true, 1, 1);
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