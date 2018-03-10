package application.frontend;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import static application.Consts.CROSSHAIR_SIZE;
import static application.Consts.VALENCE_AROUSAL_MODEL_SIZE;

public class TargetEmotionSelectorController {
    private MainWindowController mainWindowController;
    private int index;

    @FXML Text valenceText;
    @FXML Text arousalText;
    @FXML Button setTargetEmotionButton;
    @FXML AnchorPane valenceArousalModel;
    @FXML Line crosshairX;
    @FXML Line crosshairY;

    void init() {
        valenceArousalModel.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                double x = event.getX();
                double y = event.getY();

                if (x < 0) {
                    x = 0;
                }
                if (x > VALENCE_AROUSAL_MODEL_SIZE) {
                    x = VALENCE_AROUSAL_MODEL_SIZE;
                }
                if (y < 0) {
                    y = 0;
                }
                if (y > VALENCE_AROUSAL_MODEL_SIZE) {
                    y = VALENCE_AROUSAL_MODEL_SIZE;
                }

                setValence(x / VALENCE_AROUSAL_MODEL_SIZE);
                setArousal((VALENCE_AROUSAL_MODEL_SIZE - y) / VALENCE_AROUSAL_MODEL_SIZE);
            }
        });
    }

    void setMainWindowController(MainWindowController controller) {
        mainWindowController = controller;
    }

    void setValence(double valence) {
        valenceText.setText(String.format("%.2f", valence));
        setCrosshairValence(valence);

    }

    void setArousal(double arousal) {
        arousalText.setText(String.format("%.2f", arousal));
        setCrosshairArousal(arousal);
    }

    void setIndex(int index) {
        this.index = index;
    }

    public void handleSetTargetEmotionButton() {
        mainWindowController.setValenceAndArousal(Double.parseDouble(valenceText.getText()), Double.parseDouble(arousalText.getText()), index);
        mainWindowController.updateParametersText(index);

        Stage stage = (Stage) setTargetEmotionButton.getScene().getWindow();
        stage.close();
    }

    private void setCrosshairValence(double valence) {
        double x = valence * VALENCE_AROUSAL_MODEL_SIZE;

        AnchorPane.setLeftAnchor(crosshairX, x - CROSSHAIR_SIZE - 1);
        AnchorPane.setLeftAnchor(crosshairY, x - CROSSHAIR_SIZE - 1);
    }

    private void setCrosshairArousal(double arousal) {
        double y = VALENCE_AROUSAL_MODEL_SIZE - (arousal * VALENCE_AROUSAL_MODEL_SIZE);

        // stops bug where the anchorpane changes size when the bottom is selected
        if (y > 228) {
            y = 228;
        }

        AnchorPane.setTopAnchor(crosshairX, y - 1);
        AnchorPane.setTopAnchor(crosshairY, y - 1);
    }
}
