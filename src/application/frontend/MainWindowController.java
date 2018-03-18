package application.frontend;

import application.highbackend.Composition;
import application.highbackend.Section;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import jm.music.data.Score;
import jm.util.Write;

import javax.sound.midi.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import static application.Consts.*;
import static java.lang.Thread.sleep;
import static jm.constants.Pitches.C4;

public class MainWindowController implements Initializable {
    @FXML private TextField sectionDurationMinutesText;
    @FXML private TextField sectionDurationSecondsText;
    @FXML private TextField transitionLengthText;
    @FXML private ComboBox sectionComboBox;
    @FXML private Text valenceText;
    @FXML private Text arousalText;
    @FXML private HBox transitionLengthArea;
    @FXML private Text helpText;
    @FXML private Text transitionDurationInTimeText;
    @FXML private Text totalCompositionDurationText;
    @FXML private Button playButton;
    @FXML private Slider seekBar;
    @FXML private Text playMaxCompositionDuration;
    @FXML private Text playCurrentCompositionDuration;

    private final Logger logger = Logger.getLogger("ErrorLogging");
    private Stage primaryStage;
    private ArrayList<SectionParameterContainer> sectionList;
    private ObservableList<String> sectionStringList;

    private Score composition = null;
    private Sequencer sequencer;
    private boolean seekBarIsListening = true;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        sectionList = new ArrayList<>();
        sectionList.add(new SectionParameterContainer());

        sectionStringList = FXCollections.observableArrayList();
        sectionStringList.add("Section 1");

        sectionComboBox.setItems(sectionStringList);
        sectionComboBox.getSelectionModel().selectFirst();

        updateTransitionLengthArea(0);

        sectionDurationMinutesText.setTextFormatter(minutesFormatter());
        sectionDurationSecondsText.setTextFormatter(secondsFormatter());
        transitionLengthText.setTextFormatter(transitionDurationFormatter());

        sectionDurationMinutesText.focusedProperty().addListener(e ->
                handleSectionDurationMinutesTextFocus()
        );

        sectionDurationSecondsText.focusedProperty().addListener(e ->
                handleSectionDurationSecondsTextFocus()
        );

        transitionLengthText.focusedProperty().addListener(e ->
                handleTransitionLengthTextFocus()
        );

        setupSequencer();

        seekBar.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (seekBarIsListening) {
                    sequencer.setMicrosecondPosition((long) newValue.intValue()*1000000);
                }
            }
        });
    }

    // only allow numbers 1-4
    private TextFormatter<String> minutesFormatter() {
        return new TextFormatter<>( change -> {
            if (change.getControlNewText().isEmpty() || change.getControlNewText().matches("[0-4]")) {
                return change;
            }
            return null;
        });
    }

    // only allow numbers up to the number 59
    private TextFormatter<String> secondsFormatter() {
        return new TextFormatter<>( change -> {
            if (change.getControlNewText().isEmpty() || change.getControlNewText().matches("[0-5]?[0-9]")) {
                return change;
            }
            return null;
        });
    }

    // only allow numbers and up to 2 characters
    private TextFormatter<String> transitionDurationFormatter() {
        return new TextFormatter<>( change -> {
            if (change.getControlNewText().isEmpty() || change.getControlNewText().matches("[0-9]?[0-9]")) {
                return change;
            }
            return null;
        });
    }

    // format seconds to 00 and enforce minimum section duration
    private void formatSeconds(TextField secondsText, TextField minutesText) {
        if (!secondsText.getText().isEmpty()) {
            int seconds = Integer.parseInt(secondsText.getText());

            if (minutesText.getText().isEmpty()) {
                if (seconds < SECTION_MINIMUM_SECONDS) {
                    seconds = SECTION_MINIMUM_SECONDS;
                }
            }
            else if (Integer.parseInt(minutesText.getText()) == 0 && seconds < SECTION_MINIMUM_SECONDS) {
                seconds = SECTION_MINIMUM_SECONDS;
            }

            secondsText.setText(String.format("%02d", seconds));
        }
    }

    // only allow multiples of 2 and up to max based on section duration
    private void formatTransitionLength() {
        int size = Integer.parseInt(transitionLengthText.getText());

        if (size % 2 != 0) {
            size ++;
            helpText.setText("The transition length must be even.");
        }

        if (size > MAX_TRANSITION_LENGTH) {
            size = MAX_TRANSITION_LENGTH;
        }

        transitionLengthText.setText(String.format("%d", size));
    }

    public void handleAddSectionButton() {
        if (sectionList.size() < MAX_SECTIONS) {
            sectionList.add(new SectionParameterContainer());
            sectionStringList.add(String.format("Section %d", sectionList.size()));
            sectionComboBox.getSelectionModel().select(sectionList.size()-1);
            resetParametersText();
            helpText.setText("Section added.");
        }
        else {
            helpText.setText("The maximum number of sections has been reached.");
        }
    }

    public void handleDeleteSectionButton() {
        // only allow delete if there is more than one section
        if (sectionList.size() > 1) {
            int index = sectionComboBox.getSelectionModel().getSelectedIndex();

            // check if it is the start section or end section or neither
            boolean start = (index == 0);
            boolean end = (index == sectionList.size() - 1);

            // update transitions
            // if it is the start section, reset start transition for the section after it
            if (start) {
                sectionList.get(index + 1).setStartTransitionLength(null);
            }
            // if it is the end section, reset end transition for the section before it
            else if (end) {
                sectionList.get(index - 1).setEndTransitionLength(null);
            }
            // otherwise, reset end transition for section before it and reset start transition for section after it
            else {
                sectionList.get(index - 1).setEndTransitionLength(null);
                sectionList.get(index + 1).setStartTransitionLength(null);
            }

            // remove the selected section
            sectionList.remove(index);

            // update the section names
            sectionStringList.clear();
            int i;
            for (i = 0; i < sectionList.size(); i++) {
                sectionStringList.add(String.format("Section %d", i+1));
            }

            // select an appropriate section
            // if it is not the start section, select the one before it
            if (!start) {
                sectionComboBox.getSelectionModel().select(index-1);
            }
            // otherwise select the one now in it's place
            else {
                sectionComboBox.getSelectionModel().select(index);
            }
            helpText.setText("Section deleted.");
        }
        else {
            helpText.setText("The composition must contain at least one section.");
        }
    }

    public void handleSectionListAction() {
        int index = sectionComboBox.getSelectionModel().getSelectedIndex();

        if (index > -1) {
            updateParametersText(index);
            updateTransitionLengthArea(index);
        }
    }

    private void handleSectionDurationMinutesTextFocus() {
        int index = sectionComboBox.getSelectionModel().getSelectedIndex();
        if (!sectionDurationMinutesText.getText().isEmpty()) {
            formatSeconds(sectionDurationSecondsText, sectionDurationMinutesText);
            int sectionLengthMinutes = Integer.parseInt(sectionDurationMinutesText.getText());
            sectionList.get(index).setSectionLengthMinutes(sectionLengthMinutes);
        }
        else {
            sectionList.get(index).setSectionLengthMinutes(null);
        }
        updateTransitionDurationInTimeText(calculateTransitionLengthInSeconds(index));
        updateTotalCompositionDurationText();
    }

    private void handleSectionDurationSecondsTextFocus() {
        int index = sectionComboBox.getSelectionModel().getSelectedIndex();
        if (!sectionDurationSecondsText.getText().isEmpty()) {
            formatSeconds(sectionDurationSecondsText, sectionDurationMinutesText);
            int sectionLengthSeconds = Integer.parseInt(sectionDurationSecondsText.getText());
            sectionList.get(index).setSectionLengthSeconds(sectionLengthSeconds);
        }
        else {
            sectionList.get(index).setSectionLengthSeconds(null);
        }
        updateTransitionDurationInTimeText(calculateTransitionLengthInSeconds(index));
        updateTotalCompositionDurationText();
    }

    private void handleTransitionLengthTextFocus() {
        int index = sectionComboBox.getSelectionModel().getSelectedIndex();
        if (!transitionLengthText.getText().isEmpty()) {
            formatTransitionLength();
            int transitionLength = Integer.parseInt(transitionLengthText.getText());
            sectionList.get(index).setEndTransitionLength(transitionLength/2);
            sectionList.get(index+1).setStartTransitionLength(transitionLength/2);
        }
        else {
            sectionList.get(index).setEndTransitionLength(null);
            sectionList.get(index+1).setStartTransitionLength(null);
        }
        updateTransitionDurationInTimeText(calculateTransitionLengthInSeconds(index));
        updateTotalCompositionDurationText();
    }

    private void resetParametersText() {
        sectionDurationMinutesText.clear();
        sectionDurationSecondsText.clear();
        transitionLengthText.clear();
        valenceText.setText("0.50");
        arousalText.setText("0.50");
    }

    private void updateTransitionLengthArea(int index) {
        if (index < sectionList.size() - 1) {
            transitionLengthArea.setVisible(true);
        }
        else {
            transitionLengthArea.setVisible(false);
        }
    }

    void updateParametersText(int index) {
        SectionParameterContainer section = sectionList.get(index);

        if (section.getSectionLengthMinutes() != null) {
            sectionDurationMinutesText.setText(String.format("%d", section.getSectionLengthMinutes()));
        }
        else {
            sectionDurationMinutesText.clear();
        }

        if (section.getSectionLengthSeconds() != null) {
            sectionDurationSecondsText.setText(String.format("%02d", section.getSectionLengthSeconds()));
        }
        else {
            sectionDurationSecondsText.clear();
        }

        valenceText.setText(String.format("%.2f", section.getValence()));
        arousalText.setText(String.format("%.2f", section.getArousal()));

        boolean end = (index == sectionList.size() - 1);
        // update transition length and transition duration in time text
        if (!end) {
            // update transition length text
            SectionParameterContainer sectionAfter = sectionList.get(index+1);
            if (section.getEndTransitionLength() != null && sectionAfter.getStartTransitionLength() != null) {
                int transitionLength = section.getEndTransitionLength() + sectionAfter.getStartTransitionLength();
                transitionLengthText.setText(String.format("%d", transitionLength));
            }
            else {
                transitionLengthText.clear();
            }
        }
        else { // not necessary as this area should be disabled, but just in case
            transitionLengthText.clear();
        }

        updateTransitionDurationInTimeText(calculateTransitionLengthInSeconds(index));
        updateTotalCompositionDurationText();
    }


    public void handleGenerateCompositionButton() {
        // check if composition is complete
        boolean isComplete = true;
        int incompleteSection = 0;
        int i;
        for (i = 0; i < sectionList.size(); i++) {
            if (!sectionList.get(i).isComplete()) {
                isComplete = false;
                incompleteSection = i+1;
                break;
            }
        }

        if (isComplete) {
            // reset playback if sequencer is running
            if (sequencer.isRunning()) {
                sequencer.stop();
                stopComposition();
            }

            // generate composition
            // set key
            int key = C4;

            // create a list of sections
            Section[] sections = new Section[sectionList.size()];

            SectionParameterContainer section;
            double[] parameters;
            boolean isStart;
            boolean isEnd;
            int sectionLength;
            Integer startTransitionLength;
            Integer endTransitionLength;
            for (i = 0; i < sectionList.size(); i++) {
                section = sectionList.get(i);

                parameters = section.generateParameters();
                parameters[TEMPO] = section.getTempo();

                startTransitionLength = section.getStartTransitionLength();
                startTransitionLength = (startTransitionLength == null) ? 0 : startTransitionLength;

                endTransitionLength = section.getEndTransitionLength();
                endTransitionLength = (endTransitionLength == null) ? 0 : endTransitionLength;

                sectionLength = section.getSectionLengthInBars() + startTransitionLength + endTransitionLength;

                isStart = (i == 0);
                isEnd = (i == sectionList.size() - 1);

                sections[i] = new Section(parameters, key, sectionLength, isStart, isEnd, startTransitionLength, endTransitionLength);
            }

            Composition comp = new Composition(sections);
            comp.generateComposition();

            this.composition = comp.getScore();
            Write.midi(this.composition, TEMP_MIDI_FILE);

            helpText.setText("Composition has been generated and loaded for playback.");
            setupPlayback();
        }
        else {
            helpText.setText(String.format("Please complete section %d to generate composition.", incompleteSection));
        }
    }

    private void setupPlayback() {
        try{
            Sequence sequence = MidiSystem.getSequence(new File(TEMP_MIDI_FILE));
            long seconds = sequence.getMicrosecondLength()/1000000;
            seekBar.setMax(seconds);
            seekBar.setValue(0);
            playMaxCompositionDuration.setText(String.format(TIME_FORMAT_SPECIFIER, seconds/60, seconds%60));
        }
        catch (IOException | InvalidMidiDataException e){
            logger.log(Level.SEVERE, e.toString(), e);
            helpText.setText("MIDI file could not be loaded for playback.");
        }
    }

    private void setupSequencer() {
        try {
            sequencer = MidiSystem.getSequencer();
            sequencer.open();
        }
        catch (MidiUnavailableException e) {
            logger.log(Level.SEVERE, e.toString(), e);
        }

        sequencer.addMetaEventListener(new MetaEventListener() {
            public void meta(MetaMessage event) {
                // sequencer started
                if (event.getType() == 88) {
                    // run on Java FX thread
                    Platform.runLater(new Runnable(){
                        @Override
                        public void run() {
                            playButton.setText("Stop\nComposition");
                            seekBar.setDisable(false);

                            // poll for time updates
                            Thread thread = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    long seconds;
                                    while(sequencer.isRunning()) {
                                        try{
                                            seconds = sequencer.getMicrosecondPosition()/1000000;

                                            // remove listener when setting value
                                            setSeekBarValue(seconds);

                                            playCurrentCompositionDuration.setText(
                                                    String.format(TIME_FORMAT_SPECIFIER, seconds/60, seconds%60)
                                            );
                                            sleep(500);
                                        }
                                        catch(InterruptedException e) {
                                            logger.log(Level.SEVERE, e.toString(), e);
                                            Thread.currentThread().interrupt();
                                        }
                                    }
                                }
                            });
                            thread.start();
                        }
                    });
                }
                // sequencer finished
                else if (event.getType() == 47) {
                    Platform.runLater(new Runnable(){
                        @Override
                        public void run() {
                            stopComposition();
                        }
                    });
                }
            }
        });
    }

    private void stopComposition() {
        playButton.setText("Play\nComposition");
        seekBar.setDisable(true);
        seekBar.setValue(0);
        playCurrentCompositionDuration.setText("00:00");
    }

    public void handlePlayCompositionButton() {
        if (sequencer.isRunning()) {
            sequencer.stop();
            stopComposition();

        }
        else if (composition == null) {
            helpText.setText("Generate composition to use playback features.");
        }
        else {
            try {
                Sequence sequence = MidiSystem.getSequence(new File(TEMP_MIDI_FILE));
                sequencer.setSequence(sequence);
                sequencer.start();
            }
            catch (IOException | InvalidMidiDataException e){
                logger.log(Level.SEVERE, e.toString(), e);
                helpText.setText("Error loading MIDI file. Could not play composition.");
            }
        }
    }

    public void openTargetEmotionSelector() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("target-emotion-selector.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 300, 400);
            Stage stage = new Stage();
            stage.setTitle("Target Emotion Selector");
            stage.setScene(scene);

            TargetEmotionSelectorController targetEmotionSelectorController = fxmlLoader.getController();

            targetEmotionSelectorController.setMainWindowController(this);
            int index = sectionComboBox.getSelectionModel().getSelectedIndex();
            targetEmotionSelectorController.setIndex(index);
            targetEmotionSelectorController.setValence(sectionList.get(index).getValence());
            targetEmotionSelectorController.setArousal(sectionList.get(index).getArousal());
            targetEmotionSelectorController.init();

            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        }
        catch(IOException e) {
            logger.log(Level.SEVERE, e.toString(), e);
        }
    }

    private Integer calculateTransitionLengthInSeconds(int index) {
        SectionParameterContainer section;
        SectionParameterContainer sectionAfter;

        boolean end = (index == sectionList.size() - 1);
        if (!end) {
            section = sectionList.get(index);
            sectionAfter = sectionList.get(index + 1);
        }
        else {
            return null;
        }

        if (section.getEndTransitionLength() != null && sectionAfter.getStartTransitionLength() != null) {
            int transitionLength = section.getEndTransitionLength() + sectionAfter.getStartTransitionLength();

            double tempoBefore = section.getTempo();
            double tempoAfter = sectionAfter.getTempo();
            double difference = tempoAfter - tempoBefore;
            double transitionLengthInSeconds = 0;
            double tempo;

            int i;
            for (i = 0; i < transitionLength; i++) {
                tempo = tempoBefore + (difference / transitionLength) * i;
                transitionLengthInSeconds += (60 / tempo) * 4;
            }

            return (int) Math.round(transitionLengthInSeconds);
        }
        else {
            return null;
        }
    }

    private void updateTransitionDurationInTimeText(Integer transitionLengthInSeconds) {
        if (transitionLengthInSeconds != null) {
            int minutes = transitionLengthInSeconds / 60;
            int seconds = transitionLengthInSeconds % 60;
            transitionDurationInTimeText.setText(String.format("%01d:%02d", minutes, seconds));
        }
        else {
            transitionDurationInTimeText.setText("0:00");
        }
    }

    private void updateTotalCompositionDurationText() {
        int i;
        boolean isComplete = true;
        for (i = 0; i < sectionList.size(); i++) {
            if (!sectionList.get(i).isComplete()) {
                isComplete = false;
                break;
            }
        }

        if (isComplete) {

            int minutes = 0;
            int seconds = 0;
            Integer transitionLength;
            SectionParameterContainer section;
            for (i = 0; i < sectionList.size(); i++) {
                section = sectionList.get(i);

                minutes += section.getSectionLengthMinutes();
                seconds += section.getSectionLengthSeconds();

                transitionLength = calculateTransitionLengthInSeconds(i);
                seconds += (transitionLength == null) ? 0 : transitionLength;
            }

            minutes += seconds / 60;
            seconds = seconds % 60;

            totalCompositionDurationText.setText(String.format(TIME_FORMAT_SPECIFIER, minutes, seconds));
        }
        else {
            totalCompositionDurationText.setText("00:00");
        }
    }

    public void handleResetCompositionButton() {
        sectionList.clear();
        sectionList.add(new SectionParameterContainer());

        sectionStringList = FXCollections.observableArrayList();
        sectionStringList.add("Section 1");

        sectionComboBox.setItems(sectionStringList);
        sectionComboBox.getSelectionModel().selectFirst();

        helpText.setText("All changes have been reset.");
    }

    void setValenceAndArousal(double valence, double arousal, int index) {
        sectionList.get(index).setValence(valence);
        sectionList.get(index).setArousal(arousal);
        helpText.setText("Valence and Arousal have been updated.");
    }

    private void setSeekBarValue(long value) {
        // remove listener so that only changes from the user affect the listener
        seekBarIsListening = false;
        seekBar.setValue(value);
        seekBarIsListening = true;
    }

    public void handleExportToMidiButton() {
        if (composition != null) {
            FileChooser fileChooser = new FileChooser();

            FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("MIDI Files (*.mid)", "*.mid");
            fileChooser.getExtensionFilters().add(extensionFilter);

            File file = fileChooser.showSaveDialog(primaryStage);

            if(file != null){
                try{
                    Sequence sequence = MidiSystem.getSequence(new File(TEMP_MIDI_FILE));
                    int[] allowedTypes = MidiSystem.getMidiFileTypes(sequence);
                    MidiSystem.write(sequence, allowedTypes[0], file);
                }
                catch (IOException | InvalidMidiDataException e){
                    logger.log(Level.SEVERE, e.toString(), e);
                    helpText.setText("Could not save MIDI file.");
                }
            }
        }
        else {
            helpText.setText("Generate composition to be able to export.");
        }
    }

    void setStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void onAddSectionButtonHover() {
        helpText.setText("Click to add a section to the composition.");
    }

    public void onDeleteSectionButtonHover() {
        helpText.setText("Click to delete the currently selected section.");
    }

    public void onSectionDurationHover() {
        helpText.setText("Section duration does not include the transition to the next section.");
    }

    public void onValenceArousalTextHover() {
        helpText.setText("Valence and Arousal are dimensions for classifying emotions.");
    }

    public void onSetTargetEmotionButtonHover() {
        helpText.setText("Click to open a window to set the target emotion for the current section");
    }

    public void onTransitionLengthDurationTextHover() {
        helpText.setText("Duration of the transition length. Changing target emotion may affect this.");
    }

    public void onTransitionLengthHover() {
        helpText.setText("Set length of the transition to next section in bars (Each chord lasts for one bar)");
    }

    public void onTotalCompositionDurationTextHover() {
        helpText.setText("Estimated total composition duration. Updates as changes are made.");
    }

    public void onGenerateCompositionButtonHover() {
        helpText.setText("Click to generate the composition based on the current values.");
    }

    public void onResetCompositionButtonHover() {
        helpText.setText("Click to reset all changes made.");
    }

    public void onPlayCompositionButtonHover() {
        helpText.setText("Click to play/stop the generated composition.");
    }

    public void onExportToMidiButtonHover() {
        helpText.setText("Click to export the generated composition in MIDI format.");
    }

    public void resetHelpText() {
        helpText.setText("Hover over interactive areas in the application to display helpful text.");
    }
}
