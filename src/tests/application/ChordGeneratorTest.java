package application;

import jm.music.data.CPhrase;
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
        CPhrase chord = chordGenerator.majorChord(C3);

        Note c = new Note();
        c.setPitch(C3);
        Note e = new Note();
        c.setPitch(E3);
        Note g = new Note();
        c.setPitch(G3);

        assertEquals(true, chord.hasNote(c));
        assertEquals(true, chord.hasNote(e));
        assertEquals(true, chord.hasNote(g));
        assertEquals(3, chord.getPhraseList().size());
    }

    @Test
    public void minorChordShouldContainCorrectNotes() {
        CPhrase chord = chordGenerator.minorChord(C3);

        Note c = new Note();
        c.setPitch(C3);
        Note eF = new Note();
        c.setPitch(EF3);
        Note g = new Note();
        c.setPitch(G3);

        assertEquals(true, chord.hasNote(c));
        assertEquals(true, chord.hasNote(eF));
        assertEquals(true, chord.hasNote(g));
        assertEquals(3, chord.getPhraseList().size());
    }

    @Test
    public void diminishedChordShouldContainCorrectNotes() {
        CPhrase chord = chordGenerator.diminishedChord(C3);

        Note c = new Note();
        c.setPitch(C3);
        Note eF = new Note();
        c.setPitch(EF3);
        Note gF = new Note();
        c.setPitch(GF3);

        assertEquals(true, chord.hasNote(c));
        assertEquals(true, chord.hasNote(eF));
        assertEquals(true, chord.hasNote(gF));
        assertEquals(3, chord.getPhraseList().size());
    }

    @Test
    public void dominantChordShouldContainCorrectNotes() {
        CPhrase chord = chordGenerator.dominantChord(C3);

        Note c = new Note();
        c.setPitch(C3);
        Note e = new Note();
        c.setPitch(E3);
        Note g = new Note();
        c.setPitch(G3);
        Note bF = new Note();
        c.setPitch(BF4);

        assertEquals(true, chord.hasNote(c));
        assertEquals(true, chord.hasNote(e));
        assertEquals(true, chord.hasNote(g));
        assertEquals(true, chord.hasNote(bF));
        assertEquals(4, chord.getPhraseList().size());
    }
}