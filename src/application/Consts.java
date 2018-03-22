package application;

// container for all constants
public final class Consts {
    private Consts() {
        throw new IllegalStateException("Consts Class");
    }

    // transition matrix constants
    // major chords
    public static final int I = 0;
    public static final int IS = 1;
    public static final int IIF = 1;
    public static final int II = 2;
    public static final int IIS = 3;
    public static final int IIIF = 3;
    public static final int III = 4;
    public static final int IV = 5;
    public static final int IVS = 6;
    public static final int VF = 6;
    public static final int V = 7;
    public static final int VS = 8;
    public static final int VIF = 8;
    public static final int VI = 9;
    public static final int VIS = 10;
    public static final int VIIF = 10;
    public static final int VII = 11;

    // minor chords
    public static final int IM = 12;
    public static final int IMS = 13;
    public static final int IIMF = 13;
    public static final int IIM = 14;
    public static final int IIMS = 15;
    public static final int IIIMF = 15;
    public static final int IIIM = 16;
    public static final int IVM = 17;
    public static final int IVMS = 18;
    public static final int VMF = 18;
    public static final int VM = 19;
    public static final int VMS = 20;
    public static final int VIMF = 20;
    public static final int VIM = 21;
    public static final int VIMS = 22;
    public static final int VIIMF = 22;
    public static final int VIIM = 23;

    // diminished chords
    public static final int IO = 24;
    public static final int IOS = 25;
    public static final int IIOF = 25;
    public static final int IIO = 26;
    public static final int IIOS = 27;
    public static final int IIIOF = 27;
    public static final int IIIO = 28;
    public static final int IVO = 29;
    public static final int IVOS = 30;
    public static final int VOF = 30;
    public static final int VO = 31;
    public static final int VOS = 32;
    public static final int VIOF = 32;
    public static final int VIO = 33;
    public static final int VIOS = 34;
    public static final int VIIOF = 34;
    public static final int VIIO = 35;

    // dominant chords
    public static final int I7 = 36;
    public static final int I7S = 37;
    public static final int II7F = 37;
    public static final int II7 = 38;
    public static final int II7S = 39;
    public static final int III7F = 39;
    public static final int III7 = 40;
    public static final int IV7 = 41;
    public static final int IV7S = 42;
    public static final int V7F = 42;
    public static final int V7 = 43;
    public static final int V7S = 44;
    public static final int VI7F = 44;
    public static final int VI7 = 45;
    public static final int VI7S = 46;
    public static final int VII7F = 46;
    public static final int VII7 = 47;

    // transition matrix filter parameter indexes
    public static final int MAJOR_CHORDS = 0;
    public static final int MINOR_CHORDS = 1;
    public static final int DIMINISHED_CHORDS = 2;
    public static final int DOMINANT_CHORDS = 3;
    public static final int TONALITY = 4;
    public static final int MODE = 5;
    public static final int DIATONIC = 6;
    public static final int PITCH = 7;
    public static final int TEMPO = 8;
    public static final int VOLUME = 9;
    public static final int VELOCITY = 10;

    // GUI related constants
    public static final int SECTION_MINIMUM_SECONDS = 5;
    public static final int MAX_SECTIONS = 9;
    public static final int MAX_TRANSITION_LENGTH = 24;
    public static final int VALENCE_AROUSAL_MODEL_SIZE = 230;
    public static final double CROSSHAIR_SIZE = 5;
    public static final String TEMP_MIDI_FILE = "composition.mid";
    public static final String TIME_FORMAT_SPECIFIER = "%02d:%02d";

    // other constants
    public static final int NUMBER_OF_PARAMETERS = 11;
    public static final int PHRASE_DURATION = 4;
    public static final int NUMBER_OF_NOTES_IN_CHORD = 4;
    public static final int NUMBER_OF_CHORDS = 48;
    public static final int MIN_VELOCITY = 40;
    public static final int MAX_VELOCITY = 70;
}
