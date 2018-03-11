package application.frontend;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import static org.junit.Assert.*;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

public class TargetEmotionSelectorControllerTest extends ApplicationTest {
    private double valence = 0.2;
    private double arousal = 0.4;

    @Override
    public void start (Stage stage) throws Exception {
        MainWindowController mainWindowController = new MainWindowController();

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("target-emotion-selector.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);

        TargetEmotionSelectorController targetEmotionSelectorController = fxmlLoader.getController();

        targetEmotionSelectorController.setMainWindowController(mainWindowController);
        targetEmotionSelectorController.setIndex(0);
        targetEmotionSelectorController.setValence(valence);
        targetEmotionSelectorController.setArousal(arousal);
        targetEmotionSelectorController.init();

        stage.show();
        stage.toFront();
    }

    private <T extends Node> T find(final String query) {
        return lookup(query).query();
    }

    @Test
    public void valenceIsSetCorrectlyOnInitialisation() {
        Text valenceText = find("#valenceText");
        assertEquals(String.format("%.2f", valence), valenceText.getText());
    }

    @Test
    public void arousalIsSetCorrectlyOnInitialisation() {
        Text arousalText = find("#arousalText");
        assertEquals(String.format("%.2f", arousal), arousalText.getText());
    }

    @Test
    public void crosshairPositionIsSetCorrectlyOnInitialisation() {
        Line crosshairX = find("#crosshairX");
        Line crosshairY = find("#crosshairY");

        assertEquals(40, AnchorPane.getLeftAnchor(crosshairX), 0);
        assertEquals(40, AnchorPane.getLeftAnchor(crosshairY), 0);
        assertEquals(137, AnchorPane.getTopAnchor(crosshairX), 0);
        assertEquals(137, AnchorPane.getTopAnchor(crosshairY), 0);
    }

    @Test
    public void clickingOnValenceArousalModelAltersValenceAndArousal() {
        AnchorPane valenceArousalModel = find("#valenceArousalModel");
        Text valenceText = find("#valenceText");
        Text arousalText = find("#arousalText");

        clickOn(valenceArousalModel);
        assertNotEquals(String.format("%.2f", valence), valenceText.getText());
        assertNotEquals(String.format("%.2f", arousal), arousalText.getText());
    }

    @Test
    public void clickingOnValenceArousalModelChangesCrosshairPosition() {
        AnchorPane valenceArousalModel = find("#valenceArousalModel");
        Line crosshairX = find("#crosshairX");
        Line crosshairY = find("#crosshairY");

        clickOn(valenceArousalModel);

        assertNotEquals(40, AnchorPane.getLeftAnchor(crosshairX), 0);
        assertNotEquals(40, AnchorPane.getLeftAnchor(crosshairY), 0);
        assertNotEquals(137, AnchorPane.getTopAnchor(crosshairX), 0);
        assertNotEquals(137, AnchorPane.getTopAnchor(crosshairY), 0);
    }
}