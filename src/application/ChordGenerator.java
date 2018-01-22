package application;

import jm.JMC;
import jm.music.data.Note;
import jm.util.Play;

/**
 * Created by praneilkamat on 22/01/2018.
 */
public class ChordGenerator {

    public void generateChord() {
        Note n = new Note();
        n.setPitch(JMC.GS4);
        Play.midi(n);
    }
}
