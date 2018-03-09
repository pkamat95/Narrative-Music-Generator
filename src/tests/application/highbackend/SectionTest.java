package application.highbackend;

import application.lowbackend.ValenceArousalModel;
import jm.music.data.Part;
import org.junit.Before;
import org.junit.Test;

import static application.Consts.I;
import static jm.constants.Pitches.C4;
import static org.junit.Assert.*;

public class SectionTest {
    private ValenceArousalModel model;

    @Before
    public void setUp() {
        model = new ValenceArousalModel(0.5, 0.5);
    }

    @Test
    public void scoreContainsThreeParts() {
        Section section = new Section(model.generateParameters(), C4, 3, true, true, 1, 1);
        section.generateSection(I);

        assertEquals(3, section.getScore().getPartArray().length);
    }

    @Test
    public void partsContainCorrectNumberOfPhrases() {
        double[] parameters = model.generateParameters();
        Section section = new Section(parameters, C4, 3, true, true, 1, 1);
        section.generateSection(I);
        int sectionLength = section.getSectionLength();
        Part[] parts = section.getScore().getPartArray();

        assertEquals(sectionLength * 4, parts[0].size());
        assertEquals(sectionLength, parts[1].size());
        assertEquals(sectionLength, parts[2].size());
    }

    @Test
    public void sectionLengthAdjustsIfNotStart() {
        int sectionLength = 5, startTransitionLength = 2;
        Section section = new Section(model.generateParameters(), C4, sectionLength, false, true, startTransitionLength, 2);

        assertEquals(sectionLength - startTransitionLength, section.getSectionLength());
    }

    @Test
    public void sectionLengthAdjustsIfNotEnd() {
        int sectionLength = 6, endTransitionLength = 1;
        Section section = new Section(model.generateParameters(), C4, sectionLength, true, false, 2, endTransitionLength);

        assertEquals(sectionLength - endTransitionLength, section.getSectionLength());
    }

    @Test
    public void sectionLengthMinimumIsOne() {
        int sectionLength = 3;
        Section section = new Section(model.generateParameters(), C4, sectionLength, false, false, 3, 3);

        assertEquals(1, section.getSectionLength());
    }
}