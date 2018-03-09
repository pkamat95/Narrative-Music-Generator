package application.lowbackend;

import org.junit.Before;
import org.junit.Test;

import static application.Consts.NUMBER_OF_CHORDS;
import static jm.constants.Pitches.C4;
import static org.junit.Assert.*;

public class MusicGeneratorTest {
    private MusicGenerator musicGenerator;
    private double[] row;

    @Before
    public void setUp() {
        musicGenerator = new MusicGenerator(C4, 0.5, 90, 0.5);
        TransitionMatrixGenerator transitionMatrixGenerator = new TransitionMatrixGenerator(
                1, 1, 1, 1, 0.5, 0.5, 0.5);
        row = transitionMatrixGenerator.getTransitionMatrix().getRow(0).clone();
        musicGenerator.addToParts(row);
    }

    @Test
    public void chordsPartContainsCorrectNumberOfPhrases() {
        assertEquals(4, musicGenerator.getChordsPart().size());
    }

    @Test
    public void leadPartContainsCorrectNumberOfPhrases() {
        assertEquals(1, musicGenerator.getLeadPart().size());
    }

    @Test
    public void bassPartContainsCorrectNumberOfPhrases() {
        assertEquals(1, musicGenerator.getBassPart().size());
    }

    @Test
    public void returnsWithinBoundsChordIndex() {
        int chordIndex = musicGenerator.addToParts(row);

        assertTrue("chord index is out of range: " + chordIndex,chordIndex >= 0 && chordIndex < NUMBER_OF_CHORDS);
    }
}