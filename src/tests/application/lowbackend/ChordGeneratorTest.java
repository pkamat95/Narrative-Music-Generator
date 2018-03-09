package application.lowbackend;

import org.junit.Before;
import org.junit.Test;

import static jm.constants.Pitches.*;
import static org.junit.Assert.*;

public class ChordGeneratorTest {
    private ChordGenerator chordGenerator;

    @Before
    public void setUp() {
        chordGenerator = new ChordGenerator();
    }

    @Test
    public void majorChordShouldContainCorrectNotes() {
        int[] chordNotes = chordGenerator.majorChord(C3);

        assertEquals(C3, chordNotes[0]);
        assertEquals(E3, chordNotes[1]);
        assertEquals(G3, chordNotes[2]);
        assertEquals(0, chordNotes[3]);
        assertEquals(4, chordNotes.length);
    }

    @Test
    public void minorChordShouldContainCorrectNotes() {
        int[] chordNotes = chordGenerator.minorChord(C3);

        assertEquals(C3, chordNotes[0]);
        assertEquals(EF3, chordNotes[1]);
        assertEquals(G3, chordNotes[2]);
        assertEquals(0, chordNotes[3]);
        assertEquals(4, chordNotes.length);
    }

    @Test
    public void diminishedChordShouldContainCorrectNotes() {
        int[] chordNotes = chordGenerator.diminishedChord(C3);

        assertEquals(C3, chordNotes[0]);
        assertEquals(EF3, chordNotes[1]);
        assertEquals(GF3, chordNotes[2]);
        assertEquals(0, chordNotes[3]);
        assertEquals(4, chordNotes.length);
    }

    @Test
    public void dominantChordShouldContainCorrectNotes() {
        int[] chordNotes = chordGenerator.dominantChord(C3);

        assertEquals(C3, chordNotes[0]);
        assertEquals(E3, chordNotes[1]);
        assertEquals(G3, chordNotes[2]);
        assertEquals(BF3, chordNotes[3]);
        assertEquals(4, chordNotes.length);
    }
}