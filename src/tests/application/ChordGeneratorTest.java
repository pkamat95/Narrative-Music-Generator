package application;

import jm.music.data.Note;
import org.junit.Before;
import org.junit.Test;

import static jm.constants.Pitches.*;
import static org.junit.Assert.*;

/**
 * Created by praneilkamat on 28/01/2018.
 */
public class ChordGeneratorTest {
    ChordGenerator chordGenerator;
    @Before
    public void setUp() {
        chordGenerator = new ChordGenerator();
    }

    @Test
    public void majorChordShouldContainCorrectNotes() {
        int[] chordNotes = chordGenerator.majorChord(C3);

        Note c = new Note();
        c.setPitch(C3);
        Note e = new Note();
        c.setPitch(E3);
        Note g = new Note();
        c.setPitch(G3);

        assertEquals(C3, chordNotes[0]);
        assertEquals(E3, chordNotes[1]);
        assertEquals(G3, chordNotes[2]);
        assertEquals(0, chordNotes[3]);
        assertEquals(4, chordNotes.length);
    }

    @Test
    public void minorChordShouldContainCorrectNotes() {
        int[] chordNotes = chordGenerator.minorChord(C3);

        Note c = new Note();
        c.setPitch(C3);
        Note eF = new Note();
        c.setPitch(EF3);
        Note g = new Note();
        c.setPitch(G3);

        assertEquals(C3, chordNotes[0]);
        assertEquals(EF3, chordNotes[1]);
        assertEquals(G3, chordNotes[2]);
        assertEquals(0, chordNotes[3]);
        assertEquals(4, chordNotes.length);
    }

    @Test
    public void diminishedChordShouldContainCorrectNotes() {
        int[] chordNotes = chordGenerator.diminishedChord(C3);

        Note c = new Note();
        c.setPitch(C3);
        Note eF = new Note();
        c.setPitch(EF3);
        Note gF = new Note();
        c.setPitch(GF3);

        assertEquals(C3, chordNotes[0]);
        assertEquals(EF3, chordNotes[1]);
        assertEquals(GF3, chordNotes[2]);
        assertEquals(0, chordNotes[3]);
        assertEquals(4, chordNotes.length);
    }

    @Test
    public void dominantChordShouldContainCorrectNotes() {
        int[] chordNotes = chordGenerator.dominantChord(C3);

        Note c = new Note();
        c.setPitch(C3);
        Note e = new Note();
        c.setPitch(E3);
        Note g = new Note();
        c.setPitch(G3);
        Note bF = new Note();
        c.setPitch(BF4);

        assertEquals(C3, chordNotes[0]);
        assertEquals(E3, chordNotes[1]);
        assertEquals(G3, chordNotes[2]);
        assertEquals(BF3, chordNotes[3]);
        assertEquals(4, chordNotes.length);
    }
}