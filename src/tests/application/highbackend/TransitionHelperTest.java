package application.highbackend;

import jm.music.data.Part;
import org.junit.Before;
import org.junit.Test;

import static application.Consts.I;
import static jm.constants.Pitches.C4;
import static org.junit.Assert.*;

public class TransitionHelperTest {
    private TransitionHelper transitionHelper;

    @Before
    public void setUp() {
        double[] beforeParameters = new double[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        double[] afterParameters = new double[] {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};

        transitionHelper = new TransitionHelper(C4);

        transitionHelper.interpolateParameters(beforeParameters, afterParameters, 2, 4);
        transitionHelper.generateTransitionPart(I);
    }

    @Test
    public void scoreFromGenerateTransitionPartContainsThreeParts() {
        assertEquals(3, transitionHelper.getTransitionPart().getPartArray().length);
    }

    @Test
    public void scoreFromGeneratedTranstitionPartContainsCorrectNumberOfPhrases() {
        Part[] parts = transitionHelper.getTransitionPart().getPartArray();

        assertEquals(4, parts[0].size());
        assertEquals(1, parts[1].size()); // lead part
        assertEquals(1, parts[2].size()); // bass part
    }

}