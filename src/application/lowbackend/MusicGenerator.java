package application.lowbackend;

import jm.constants.Durations;
import jm.constants.ProgramChanges;
import jm.music.data.CPhrase;
import jm.music.data.Note;
import jm.music.data.Part;
import jm.music.data.Phrase;

import static application.Consts.MAX_VELOCITY;
import static application.Consts.MIN_VELOCITY;
import static application.Consts.PHRASE_DURATION;

// helper class to generate chords,lead and bass parts from a probability array
public class MusicGenerator {
    private int key;
    private double tempo;
    private int dynamic;
    private double startTime = 0;
    private Part chordsPart;
    private Part leadPart;
    private Part bassPart;
    private ChordSelector chordSelector;
    private ChordGenerator chordGenerator;

    public MusicGenerator(int key, double pitch, double tempo, double velocity) {
        // adjust key based on pitch
        if (pitch < 1) {
            key -= 12;
        }
        else if (pitch >= 2) {
            key += 12;
        }

        this.key = key;
        this.tempo = tempo;
        this.dynamic = calculateDynamic(velocity);
        chordsPart = new Part("Piano", ProgramChanges.PIANO, 0);
        leadPart = new Part("Guitar", ProgramChanges.GUITAR, 1);
        bassPart = new Part("Bass", ProgramChanges.BASS, 2);
        chordSelector = new ChordSelector();
        chordGenerator = new ChordGenerator();
    }

    // adds to all three parts from a given probability array
    public int addToParts(double[] row) {
        int chordIndex = chordSelector.selectChord(row);
        int chordType = chordIndex / 12; // 0 = major, 1 = minor, 2 = diminished, 3 = dominant
        int rootNote = key + (chordIndex % 12);

        // get the chord notes for the parts
        int[] chordNotes = generateChordNotes(chordType, rootNote);

        chordsPart.addCPhrase(generateChord(chordNotes));
        leadPart.addPhrase(generateLead(chordNotes));
        bassPart.addPhrase(generateBass(rootNote));

        // adjusts start time of phrases added to account for difference in current tempo and overall score tempo
        // this ensures no gaps between chords/ overlapping chords
        updateStartTime();

        return chordIndex;
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
        chord.addChord(chordNotes, Durations.WHOLE_NOTE, dynamic);
        chord.setTempo(tempo);
        return chord;
    }

    private Phrase generateLead(int[] chordNotes) {
        Note note1 = new Note();
        note1.setPitch(chordNotes[0]);
        note1.setDuration(Durations.QUARTER_NOTE);
        note1.setDynamic(dynamic);

        Note note2 = new Note();
        note2.setPitch(chordNotes[1]);
        note2.setDuration(Durations.QUARTER_NOTE);
        note2.setDynamic(dynamic);

        Note note3 = new Note();
        note3.setPitch(chordNotes[2]);
        note3.setDuration(Durations.QUARTER_NOTE);
        note3.setDynamic(dynamic);

        Note[] notes = {note1, note2, note3, note2};
        Phrase lead = new Phrase(notes);
        lead.setStartTime(startTime);
        lead.setTempo(tempo);
        return lead;
    }

    private Phrase generateBass(int rootNote) {
        Note bassNote = new Note();
        bassNote.setPitch(rootNote - 12); // lower by an octave
        bassNote.setDuration(Durations.QUARTER_NOTE);
        bassNote.setDynamic(dynamic);

        Note[] bassNotes = {bassNote, bassNote, bassNote, bassNote};
        Phrase bass = new Phrase(bassNotes);
        bass.setStartTime(startTime);
        bass.setTempo(tempo);
        return bass;
    }

    /* this handles all gaps/overlaps between each bar generated by a single MusicGenerator,
       however a new MusicGenerator is used for each section and transition bar
       gaps/overlaps between each section/transition bar are handled in the Composition class */
    private void updateStartTime() {
        startTime += PHRASE_DURATION * (60 / tempo);
    }

    // linearly calculate velocity between min and max (dynamic is midi velocity)
    private int calculateDynamic(double velocity) {
        return (int) (MIN_VELOCITY + ((MAX_VELOCITY - MIN_VELOCITY) * velocity));
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
