package application;

import jm.constants.Durations;
import jm.music.data.CPhrase;

/**
 * Created by praneilkamat on 22/01/2018.
 */
public class ChordGenerator {

    public int[] majorChord(int rootPitch) {
        int[] pitchArray = new int[4];

        pitchArray[0] = rootPitch;
        pitchArray[1] = rootPitch + 4;
        pitchArray[2] = rootPitch + 7;
        pitchArray[3] = 0; // note with no sound

        return pitchArray;
    }

    public int[] minorChord(int rootPitch) {
        int[] pitchArray = new int[4];

        pitchArray[0] = rootPitch;
        pitchArray[1] = rootPitch + 3;
        pitchArray[2] = rootPitch + 7;
        pitchArray[3] = 0; // note with no sound

        return pitchArray;
    }

    public int[] diminishedChord(int rootPitch) {
        int[] pitchArray = new int[4];

        pitchArray[0] = rootPitch;
        pitchArray[1] = rootPitch + 3;
        pitchArray[2] = rootPitch + 6;
        pitchArray[3] = 0; // note with no sound

        return pitchArray;
    }

    public int[] dominantChord(int rootPitch) {
        int[] pitchArray = new int[4];

        pitchArray[0] = rootPitch;
        pitchArray[1] = rootPitch + 4;
        pitchArray[2] = rootPitch + 7;
        pitchArray[3] = rootPitch + 10;

        return pitchArray;
    }
}
