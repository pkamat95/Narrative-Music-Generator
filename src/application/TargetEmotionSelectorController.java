package application;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by praneilkamat on 26/02/2018.
 */
public class TargetEmotionSelectorController {
    private MainWindowController mainWindowController;
    private double valence;
    private double arousal;
    private int index;

    @FXML TextField valenceText;
    @FXML TextField arousalText;
    @FXML Button setTargetEmotionButton;

    public void init() {
        valenceText.setText(String.format("%.2f", valence));
        arousalText.setText(String.format("%.2f", arousal));
    }

    public void setMainWindowController(MainWindowController controller) {
        mainWindowController = controller;
    }

    public void setValence(double valence) {
        this.valence = valence;
    }

    public void setArousal(double arousal) {
        this.arousal = arousal;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void handleSetTargetEmotionButton() {
        mainWindowController.setValence(Double.parseDouble(valenceText.getText()), index);
        mainWindowController.setArousal(Double.parseDouble(arousalText.getText()), index);
        mainWindowController.updateParametersText(index);

        Stage stage = (Stage) setTargetEmotionButton.getScene().getWindow();
        stage.close();
    }
}
