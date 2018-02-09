package application;

import jm.constants.Durations;
import jm.constants.ProgramChanges;
import jm.music.data.CPhrase;
import jm.music.data.Note;
import jm.music.data.Part;
import jm.music.data.Phrase;

/**
 * Created by praneilkamat on 30/01/2018.
 */
public class MusicGenerator {
    private int key;
    private double tempo;
    private double startTime = 0;
    private int DURATIONOFPHRASE = 4; // each will always be 4 beats long
    private Part chordsPart;
    private Part leadPart;
    private Part bassPart;
    private ChordSelector chordSelector;
    private ChordGenerator chordGenerator;

    public MusicGenerator(int key, double pitch, double tempo) {
        // adjust key based on pitch
        if (pitch < 1) {
            key -= 12;
        }
        else if (pitch >= 2) {
            key += 12;
        }

        this.key = key;
        this.tempo = tempo;
        chordsPart = new Part("Guitar", ProgramChanges.PIANO, 0);
        leadPart = new Part("Piano", ProgramChanges.FLUTE, 1);
        bassPart = new Part("Bass", ProgramChanges.BASS, 2);
        chordSelector = new ChordSelector();
        chordGenerator = new ChordGenerator();
    }

    public int addToParts(double[] row) {
        int chordIndex = chordSelector.selectChord(row);
        int chordType = chordIndex / 12;
        int rootNote = key + (chordIndex % 12);

        int[] chordNotes = generateChordNotes(chordType, rootNote);

        chordsPart.addCPhrase(generateChord(chordNotes));
        leadPart.addPhrase(generateLead(chordNotes));
        bassPart.addPhrase(generateBass(rootNote));

        updateStartTime(); // adjusts start time of phrases added to account for difference in current tempo and overall score tempo

        return chordIndex; // return current chord index for transition matrix
    }

    private int[] generateChordNotes(int chordType, int rootNote) {
        switch(chordType) {
            case 0: return chordGenerator.majorChord(rootNote);
            case 1: return chordGenerator.minorChord(rootNote);
            case 2: return chordGenerator.diminishedChord(rootNote);
            case 3: return chordGenerator.dominantChord(rootNote);
            default: return new int[] {};
        }
    }

    private CPhrase generateChord(int[] chordNotes) {
        CPhrase chord = new CPhrase(startTime);
        chord.addChord(chordNotes, Durations.WHOLE_NOTE);
        chord.setTempo(tempo);
        return chord;
    }

    private Phrase generateLead(int[] chordNotes) {
        Note note1 = new Note();
        note1.setPitch(chordNotes[0]);
        note1.setDuration(Durations.QUARTER_NOTE);

        Note note2 = new Note();
        note2.setPitch(chordNotes[1]);
        note2.setDuration(Durations.QUARTER_NOTE);

        Note note3 = new Note();
        note3.setPitch(chordNotes[2]);
        note3.setDuration(Durations.QUARTER_NOTE);

        Note[] notes = {note1, note2, note3, note2};
        Phrase lead = new Phrase(notes);
        lead.setStartTime(startTime);
        lead.setTempo(tempo);
        return lead;
    }

    private Phrase generateBass(int rootNote) {
        Note bassNote = new Note();
        bassNote.setPitch(rootNote - 12);
        bassNote.setDuration(Durations.QUARTER_NOTE);
        Note[] bassNotes = {bassNote, bassNote, bassNote, bassNote};
        Phrase bass = new Phrase(bassNotes);
        bass.setStartTime(startTime);
        bass.setTempo(tempo);
        return bass;
    }

    private void updateStartTime() {
        startTime += DURATIONOFPHRASE * (60 / tempo);
    }

    public Part getChordsPart() {
        return chordsPart;
    }

    public Part getLeadPart() {
        return leadPart;
    }

    public Part getBassPart() {
        return bassPart;
    }
}
