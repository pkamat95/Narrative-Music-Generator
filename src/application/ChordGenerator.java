package application;

import jm.constants.Durations;
import jm.music.data.CPhrase;

/**
 * Created by praneilkamat on 22/01/2018.
 */
public class ChordGenerator {

    public CPhrase majorChord(int rootPitch) {
        int[] pitchArray = new int[3];

        pitchArray[0] = rootPitch;
        pitchArray[1] = rootPitch + 4;
        pitchArray[2] = rootPitch + 7;

        CPhrase chord = new CPhrase();
        chord.addChord(pitchArray, Durations.QUARTER_NOTE);

        return chord;
    }

    public CPhrase minorChord(int rootPitch) {
        int[] pitchArray = new int[3];

        pitchArray[0] = rootPitch;
        pitchArray[1] = rootPitch + 3;
        pitchArray[2] = rootPitch + 7;

        CPhrase chord = new CPhrase();
        chord.addChord(pitchArray, Durations.QUARTER_NOTE);

        return chord;
    }

    public CPhrase diminishedChord(int rootPitch) {
        int[] pitchArray = new int[3];

        pitchArray[0] = rootPitch;
        pitchArray[1] = rootPitch + 3;
        pitchArray[2] = rootPitch + 6;

        CPhrase chord = new CPhrase();
        chord.addChord(pitchArray, Durations.QUARTER_NOTE);

        return chord;
    }

    public CPhrase dominantChord(int rootPitch) {
        int[] pitchArray = new int[4];

        pitchArray[0] = rootPitch;
        pitchArray[1] = rootPitch + 4;
        pitchArray[2] = rootPitch + 7;
        pitchArray[3] = rootPitch + 10;

        CPhrase chord = new CPhrase();
        chord.addChord(pitchArray, Durations.QUARTER_NOTE);

        return chord;
    }
}
