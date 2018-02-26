package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import jm.music.data.Score;
import jm.util.Play;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static application.Consts.*;
import static jm.constants.Pitches.C4;

public class MainWindowController implements Initializable {
    @FXML private TextField sectionDurationMinutesText;
    @FXML private TextField sectionDurationSecondsText;
    @FXML private TextField transitionLengthText;
    @FXML private ComboBox sectionComboBox;
    @FXML private Button addSectionButton;
    @FXML private Button deleteSectionButton;
    @FXML private Text valenceText;
    @FXML private Text arousalText;
    @FXML private HBox transitionLengthArea;
    @FXML private Text helpText;
    @FXML private Text transitionDurationInTimeText;
    @FXML private Text totalCompositionDurationText;

    private ArrayList<SectionParameterContainer> sectionList;
    private ObservableList<String> sectionStringList;

    private Score composition = null;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        sectionList = new ArrayList<SectionParameterContainer>();
        sectionList.add(new SectionParameterContainer());

        sectionStringList = FXCollections.observableArrayList();
        int i;
        for (i = 0; i < sectionList.size(); i++) {
            sectionStringList.add(String.format("Section %d", i+1));
        }

        sectionComboBox.setItems(sectionStringList);
        sectionComboBox.getSelectionModel().selectFirst();

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

        updateTransitionLengthArea(0);
    }

    // only allow numbers 1-4
    private TextFormatter<String> minutesFormatter() {
        return new TextFormatter<String>( change -> {
            if (!change.getControlNewText().isEmpty()) {
                if (!change.getControlNewText().matches("[0-4]")) {
                    return null;
                }
            }
            return change;
        });
    }

    // only allow numbers and up to the number 59
    private TextFormatter<String> secondsFormatter() {
        return new TextFormatter<String>( change -> {
            if (!change.getControlNewText().isEmpty()) {
                if (!change.getControlNewText().matches("[0-5]?[0-9]")) {
                    return null;
                }
            }
            return change;
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

    // only allow numbers and up to 2 characters
    private TextFormatter<String> transitionDurationFormatter() {
        return new TextFormatter<String>( change -> {
            if (!change.getControlNewText().isEmpty()) {
                if (!change.getControlNewText().matches("[0-9]?[0-9]")) {
                    return null;
                }
            }
            return change;
        });
    }

    // only allow multiples of 2 and up to max based on section duration
    private void formatTransitionLength() {
        int size = Integer.parseInt(transitionLengthText.getText());

        if (size % 2 != 0) {
            size ++;
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
        }
    }

    public void handleDeleteSectionButton() {
        // only allow delete if there is more than one section
        if (sectionList.size() > 1) {
            int index = sectionComboBox.getSelectionModel().getSelectedIndex();

            // check if it is the start section or end section or neither
            boolean start = (index == 0) ? true : false;
            boolean end = (index == sectionList.size() - 1) ? true : false;

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
        }
    }

    public void handleSectionListAction() {
        int index = sectionComboBox.getSelectionModel().getSelectedIndex();

        if (index > -1) {
            updateParametersText(index);
            updateTransitionLengthArea(index);

            // load all parts of the section to spaces - done
            // hide transitionlength area if this is the last section - done
            // reset all paremeter textfields when adding a new section - done
            // set parameters when user sets them - done
            // clear transitions when a section is deleted - done
            // set parameters to null when textfields are left empty - done
            // calculate sectionLengthInBars - done
            // calculate transitionlength in time
            // calculate total length of composition
            // generate composition button
            // add help text for hovering over items as well as for when things don't work
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

    public void updateParametersText(int index) {
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

        boolean end = (index == sectionList.size() - 1) ? true : false;
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
        int i, incompleteSection = 0;
        for (i = 0; i < sectionList.size(); i++) {
            if (!sectionList.get(i).isComplete()) {
                isComplete = false;
                incompleteSection = i+1;
                break;
            }
        }

        if (isComplete) {
            // set key
            int key = C4;

            // create a list of sections
            Section[] sections = new Section[sectionList.size()];

            SectionParameterContainer section;
            double[] parameters;
            boolean isStart, isEnd;
            int sectionLength;
            Integer startTransitionLength, endTransitionLength;
            for (i = 0; i < sectionList.size(); i++) {
                section = sectionList.get(i);

                parameters = section.generateParameters();
                parameters[TEMPO] = section.getTempo();

                startTransitionLength = section.getStartTransitionLength();
                startTransitionLength = (startTransitionLength == null) ? 0 : startTransitionLength;

                endTransitionLength = section.getEndTransitionLength();
                endTransitionLength = (endTransitionLength == null) ? 0 : endTransitionLength;

                sectionLength = section.getSectionLengthInBars() + startTransitionLength + endTransitionLength;

                isStart = (i == 0) ? true : false;
                isEnd = (i == sectionList.size() - 1) ? true : false;

                sections[i] = new Section(parameters, key, sectionLength,isStart, isEnd, startTransitionLength, endTransitionLength);
            }

            Composition composition = new Composition(sections);
            composition.generateComposition();

            this.composition = composition.getScore();
        }
        else {
            System.out.println(String.format("Section %d is not complete", incompleteSection));
            // change help text, use incompleteSection to advise user
        }

        /*
        // create list of sections
        Section[] sections = new Section[3];
        // set key
        int key = C4;
        // create model with emotion inputs
        ValenceArousalModel model = new ValenceArousalModel();

        model.setValence(0.5);
        model.setArousal(0.5);
        sections[0] = new Section(model.generateParameters(), key, 2, true, false, 0, 1);

        model.setValence(0);
        model.setArousal(1);
        sections[1] = new Section(model.generateParameters(), key, 3,false, false, 1, 1);

        model.setValence(1);
        model.setArousal(0);
        sections[2] = new Section(model.generateParameters(), key, 2,false, true, 1, 0);

        Composition composition = new Composition(sections);
        composition.generateComposition();

        this.composition = composition.getScore();

        Play.midi(this.composition);
        */

    }

    public void handlePlayCompositionButton() {
        if (composition != null) {
            Play.midi(composition);
        }
        else {
            System.out.println("Composition has not been generated");
            // change help text to advise user
        }

        // STILL NEED TO:
        // change text to pause
        // need to figure out how to stop people from playing repeatedly
        // also need to figure out how to use seek bar, if note, replace with dropdown menu for key
    }

    public void openTargetEmotionSelector() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("target-emotion-selector.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 300, 400);
            Stage stage = new Stage();
            stage.setTitle("Target Emotion Selector");
            stage.setScene(scene);

            TargetEmotionSelectorController targetEmotionSelectorController =
                    fxmlLoader.<TargetEmotionSelectorController>getController();

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
            System.out.println(e);
        }
    }

    private void setHelpText(String s) {
        helpText.setText(s);
    }

    private void resetHelpText() {
        helpText.setText("Hover over editable areas in the application to display helpful text");
    }

    private Integer calculateTransitionLengthInSeconds(int index) {
        SectionParameterContainer section;
        SectionParameterContainer sectionAfter;

        boolean end = (index == sectionList.size() - 1) ? true : false;
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
            double transitionLengthInSeconds = 0, tempo;

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

            int minutes = 0, seconds = 0;
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

            totalCompositionDurationText.setText(String.format("%02d:%02d", minutes, seconds));
        }
        else {
            totalCompositionDurationText.setText("00:00");
        }
    }

    public void setValence(double valence, int index) {
        sectionList.get(index).setValence(valence);
    }

    public void setArousal(double arousal, int index) {
        sectionList.get(index).setArousal(arousal);
    }
}