package application.lowbackend;

class ChordGenerator {

    int[] majorChord(int rootPitch) {
        int[] pitchArray = new int[4];

        pitchArray[0] = rootPitch;
        pitchArray[1] = rootPitch + 4;
        pitchArray[2] = rootPitch + 7;
        pitchArray[3] = 0; // note with no sound

        return pitchArray;
    }

    int[] minorChord(int rootPitch) {
        int[] pitchArray = new int[4];

        pitchArray[0] = rootPitch;
        pitchArray[1] = rootPitch + 3;
        pitchArray[2] = rootPitch + 7;
        pitchArray[3] = 0; // note with no sound

        return pitchArray;
    }

    int[] diminishedChord(int rootPitch) {
        int[] pitchArray = new int[4];

        pitchArray[0] = rootPitch;
        pitchArray[1] = rootPitch + 3;
        pitchArray[2] = rootPitch + 6;
        pitchArray[3] = 0; // note with no sound

        return pitchArray;
    }

    int[] dominantChord(int rootPitch) {
        int[] pitchArray = new int[4];

        pitchArray[0] = rootPitch;
        pitchArray[1] = rootPitch + 4;
        pitchArray[2] = rootPitch + 7;
        pitchArray[3] = rootPitch + 10;

        return pitchArray;
    }
}
