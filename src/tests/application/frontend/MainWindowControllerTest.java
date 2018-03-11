package application.frontend;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.junit.Test;
import javax.sound.midi.*;
import java.io.File;
import java.io.IOException;
import static application.Consts.TEMP_MIDI_FILE;

import org.testfx.framework.junit.ApplicationTest;
import static org.junit.Assert.*;
import static org.testfx.assertions.api.Assertions.assertThat;

public class MainWindowControllerTest extends ApplicationTest {

    @Override
    public void start (Stage stage) throws Exception {
        Parent root = FXMLLoader.load(Main.class.getResource("main_window_controller.fxml"));
        stage.setScene(new Scene(root));
        stage.show();
        stage.toFront();
    }

    private <T extends Node> T find(final String query) {
        return lookup(query).query();
    }

    @Test
    public void sectionComboBoxHasCorrectTextOnInitialisation() {
        ComboBox sectionComboBox = find("#sectionComboBox");
        assertEquals("Section 1", sectionComboBox.getSelectionModel().getSelectedItem().toString());
    }

    @Test
    public void addSectionButtonAddsNewSection() {
        Button addSectionButton = find("#addSectionButton");
        ComboBox sectionComboBox = find("#sectionComboBox");
        clickOn(addSectionButton);

        assertEquals(2, sectionComboBox.getItems().size());
        assertEquals("Section 2", sectionComboBox.getSelectionModel().getSelectedItem().toString());
    }

    @Test
    public void maxSectionsIsNine() {
        Button addSectionButton = find("#addSectionButton");
        ComboBox sectionComboBox = find("#sectionComboBox");
        Text helpText = find("#helpText");

        // click on add button more than 8 times
        int i;
        for (i = 0; i < 10; i++) {
            clickOn(addSectionButton);
        }

        assertEquals("Section 9", sectionComboBox.getSelectionModel().getSelectedItem().toString());
        assertEquals("The maximum number of sections has been reached.", helpText.getText());
    }

    @Test
    public void deleteSectionButtonDeletesSelectedSection() {
        Button addSectionButton = find("#addSectionButton");
        Button deleteSectionButton = find("#deleteSectionButton");
        ComboBox sectionComboBox = find("#sectionComboBox");

        clickOn(addSectionButton);
        clickOn(deleteSectionButton);

        assertEquals(1, sectionComboBox.getItems().size());
        assertEquals("Section 1", sectionComboBox.getSelectionModel().getSelectedItem().toString());
    }

    @Test
    public void minSectionsIsOne() {
        Button deleteSectionButton = find("#deleteSectionButton");
        ComboBox sectionComboBox = find("#sectionComboBox");
        Text helpText = find("#helpText");

        clickOn(deleteSectionButton);

        assertEquals("Section 1", sectionComboBox.getSelectionModel().getSelectedItem().toString());
        assertEquals("The composition must contain at least one section.", helpText.getText());
    }

    @Test
    public void maxSectionDurationMinutesIsFour() {
        TextField sectionDurationMinutesText = find("#sectionDurationMinutesText");

        clickOn(sectionDurationMinutesText).type(KeyCode.DIGIT4);

        assertEquals("4", sectionDurationMinutesText.getText());

        sectionDurationMinutesText.clear();
        clickOn(sectionDurationMinutesText).type(KeyCode.DIGIT5);

        assertEquals("", sectionDurationMinutesText.getText());
    }

    @Test
    public void maxSectionDurationSecondsIsFiftyNine() {
        TextField sectionDurationSecondsText = find("#sectionDurationSecondsText");

        clickOn(sectionDurationSecondsText).type(KeyCode.DIGIT5).type(KeyCode.DIGIT9);

        assertEquals("59", sectionDurationSecondsText.getText());

        sectionDurationSecondsText.clear();
        clickOn(sectionDurationSecondsText).type(KeyCode.DIGIT6).type(KeyCode.DIGIT0);

        assertEquals("6", sectionDurationSecondsText.getText());
    }

    @Test
    public void minSectionDurationIsFiveSeconds() {
        TextField sectionDurationMinutesText = find("#sectionDurationMinutesText");
        TextField sectionDurationSecondsText = find("#sectionDurationSecondsText");

        clickOn(sectionDurationMinutesText).type(KeyCode.DIGIT0);
        clickOn(sectionDurationSecondsText).type(KeyCode.DIGIT0);
        clickOn(sectionDurationMinutesText); // take the focus away

        assertEquals("05", sectionDurationSecondsText.getText());

        sectionDurationMinutesText.clear();
        sectionDurationSecondsText.clear();

        clickOn(sectionDurationMinutesText).type(KeyCode.DIGIT1);
        clickOn(sectionDurationSecondsText).type(KeyCode.DIGIT0);
        clickOn(sectionDurationMinutesText); // take the focus away

        assertEquals("00", sectionDurationSecondsText.getText());

        clickOn(sectionDurationMinutesText).type(KeyCode.BACK_SPACE).type(KeyCode.DIGIT0);
        clickOn(sectionDurationSecondsText); // take the focus away

        assertEquals("05", sectionDurationSecondsText.getText());
    }

    @Test
    public void transitionLengthAreaIsDisabledForLastSection() {
        HBox transitionLengthArea = find("#transitionLengthArea");
        Button addSectionButton = find("#addSectionButton");
        ComboBox sectionComboBox = find("#sectionComboBox");

        clickOn(addSectionButton);
        assertThat(transitionLengthArea).isInvisible();

        clickOn(sectionComboBox).type(KeyCode.UP).type(KeyCode.ENTER);
        assertThat(transitionLengthArea).isVisible();
    }

    @Test
    public void maxTransitionLengthIs24() {
        Button addSectionButton = find("#addSectionButton");
        ComboBox sectionComboBox = find("#sectionComboBox");
        TextField transitionLengthText = find("#transitionLengthText");

        clickOn(addSectionButton);
        clickOn(sectionComboBox).type(KeyCode.UP).type(KeyCode.ENTER);

        clickOn(transitionLengthText).type(KeyCode.DIGIT2).type(KeyCode.DIGIT4);

        assertEquals("24", transitionLengthText.getText());

        transitionLengthText.clear();
        clickOn(transitionLengthText).type(KeyCode.DIGIT2).type(KeyCode.DIGIT5);
        clickOn(sectionComboBox); // take the focus away

        assertEquals("24", transitionLengthText.getText());
    }

    @Test
    public void transitionLengthMustBeEven() {
        Button addSectionButton = find("#addSectionButton");
        ComboBox sectionComboBox = find("#sectionComboBox");
        TextField transitionLengthText = find("#transitionLengthText");

        clickOn(addSectionButton);
        clickOn(sectionComboBox).type(KeyCode.UP).type(KeyCode.ENTER);

        clickOn(transitionLengthText).type(KeyCode.DIGIT1);
        clickOn(sectionComboBox); // take the focus away

        assertEquals("2", transitionLengthText.getText());
    }

    @Test
    public void totalCompositionLengthIsAutomaticallyUpdated() {
        TextField sectionDurationMinutesText = find("#sectionDurationMinutesText");
        TextField sectionDurationSecondsText = find("#sectionDurationSecondsText");
        Text totalCompositionDurationText = find("#totalCompositionDurationText");

        clickOn(sectionDurationMinutesText).type(KeyCode.DIGIT1);
        clickOn(sectionDurationSecondsText).type(KeyCode.DIGIT3).type(KeyCode.DIGIT0);
        clickOn(sectionDurationMinutesText); // take the focus away

        assertEquals("01:30", totalCompositionDurationText.getText());
    }

    @Test
    public void generateCompositionButtonDoesNotGenerateWhenCompositionIsIncomplete() {
        Button generateCompositionButton = find("#generateCompositionButton");
        Text helpText = find("#helpText");
        clickOn(generateCompositionButton);

        assertEquals( "Please complete section 1 to generate composition.", helpText.getText());
    }

    @Test
    public void generateCompositionButtonGeneratesAndLoadsComposition() {
        TextField sectionDurationMinutesText = find("#sectionDurationMinutesText");
        TextField sectionDurationSecondsText = find("#sectionDurationSecondsText");
        Button generateCompositionButton = find("#generateCompositionButton");
        Text helpText = find("#helpText");
        Text playMaxCompositionDuration = find("#playMaxCompositionDuration");
        Sequence sequence = null;

        try {
            sequence = MidiSystem.getSequence(new File(TEMP_MIDI_FILE));
        }
        catch(IOException | InvalidMidiDataException e) {}

        clickOn(sectionDurationMinutesText).type(KeyCode.DIGIT0);
        clickOn(sectionDurationSecondsText).type(KeyCode.DIGIT1).type(KeyCode.DIGIT0);

        clickOn(generateCompositionButton);

        assertEquals("Composition has been generated and loaded for playback.", helpText.getText());
        long seconds = sequence.getMicrosecondLength()/1000000;
        assertEquals(String.format("%02d:%02d", seconds/60, seconds%60), playMaxCompositionDuration.getText());
    }

    @Test
    public void resetCompositionButtonResetsAllChanges() {
        TextField sectionDurationMinutesText = find("#sectionDurationMinutesText");
        TextField sectionDurationSecondsText = find("#sectionDurationSecondsText");
        Button addSectionButton = find("#addSectionButton");
        Button resetCompositionButton = find("#resetCompositionButton");
        ComboBox sectionComboBox = find("#sectionComboBox");

        int i;
        for (i = 0; i < 5; i++) {
            clickOn(sectionDurationMinutesText).type(KeyCode.DIGIT1);
            clickOn(sectionDurationSecondsText).type(KeyCode.DIGIT3).type(KeyCode.DIGIT0);
            clickOn(addSectionButton);
        }
        clickOn(sectionDurationMinutesText).type(KeyCode.DIGIT1);
        clickOn(sectionDurationSecondsText).type(KeyCode.DIGIT3).type(KeyCode.DIGIT0);

        assertEquals(6, sectionComboBox.getItems().size());
        assertEquals("Section 6", sectionComboBox.getSelectionModel().getSelectedItem().toString());
        assertEquals("1", sectionDurationMinutesText.getText());
        assertEquals("30", sectionDurationSecondsText.getText());

        clickOn(resetCompositionButton);

        assertEquals(1, sectionComboBox.getItems().size());
        assertEquals("Section 1", sectionComboBox.getSelectionModel().getSelectedItem().toString());
        assertEquals("", sectionDurationMinutesText.getText());
        assertEquals("", sectionDurationSecondsText.getText());
    }

    @Test
    public void playCompositionButtonDoesNotPlayWhenNoCompositionHasBeenGenerated() {
        Button playButton = find("#playButton");
        Text helpText = find("#helpText");

        clickOn(playButton);

        assertEquals("Generate composition to use playback features.", helpText.getText());
    }

    @Test
    public void playButtonPlaysAndStopsPlayback() {
        TextField sectionDurationMinutesText = find("#sectionDurationMinutesText");
        TextField sectionDurationSecondsText = find("#sectionDurationSecondsText");
        Button generateCompositionButton = find("#generateCompositionButton");
        Button playButton = find("#playButton");

        clickOn(sectionDurationMinutesText).type(KeyCode.DIGIT0);
        clickOn(sectionDurationSecondsText).type(KeyCode.DIGIT1).type(KeyCode.DIGIT0);

        clickOn(generateCompositionButton);

        clickOn(playButton);
        assertEquals("Stop\nComposition", playButton.getText());

        clickOn(playButton);
        assertEquals("Play\nComposition", playButton.getText());
    }

    @Test
    public void exportCompositionButtonDoesNotExportWhenNoCompositionHasBeenGenerated() {
        Button exportCompositionButton = find("#exportCompositionButton");
        Text helpText = find("#helpText");

        clickOn(exportCompositionButton);

        assertEquals("Generate composition to be able to export.", helpText.getText());
    }
}