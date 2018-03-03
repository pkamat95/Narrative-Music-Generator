package application;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import static application.Consts.CROSSHAIRSIZE;
import static application.Consts.VALENCEAROUSALMODELSIZE;

/**
 * Created by praneilkamat on 26/02/2018.
 */
public class TargetEmotionSelectorController {
    private MainWindowController mainWindowController;
    private int index;

    @FXML Text valenceText;
    @FXML Text arousalText;
    @FXML Button setTargetEmotionButton;
    @FXML AnchorPane valenceArousalModel;
    @FXML Line crosshairX;
    @FXML Line crosshairY;

    public void init() {
        valenceArousalModel.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                double x = event.getX();
                double y = event.getY();

                if (x < 0) {
                    x = 0;
                }
                if (x > VALENCEAROUSALMODELSIZE) {
                    x = VALENCEAROUSALMODELSIZE;
                }
                if (y < 0) {
                    y = 0;
                }
                if (y > VALENCEAROUSALMODELSIZE) {
                    y = VALENCEAROUSALMODELSIZE;
                }

                setValence(x / VALENCEAROUSALMODELSIZE);
                setArousal((VALENCEAROUSALMODELSIZE - y) / VALENCEAROUSALMODELSIZE);
            }
        });
    }

    public void setMainWindowController(MainWindowController controller) {
        mainWindowController = controller;
    }

    public void setValence(double valence) {
        valenceText.setText(String.format("%.2f", valence));
        setCrosshairValence(valence);

    }

    public void setArousal(double arousal) {
        arousalText.setText(String.format("%.2f", arousal));
        setCrosshairArousal(arousal);
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

    public void setCrosshairValence(double valence) {
        double x = valence * VALENCEAROUSALMODELSIZE;

        valenceArousalModel.setLeftAnchor(crosshairX, x - CROSSHAIRSIZE - 1);
        valenceArousalModel.setLeftAnchor(crosshairY, x - CROSSHAIRSIZE - 1);
    }

    public void setCrosshairArousal(double arousal) {
        double y = VALENCEAROUSALMODELSIZE - (arousal * VALENCEAROUSALMODELSIZE);

        // stops bug where the anchorpane changes size when the bottom is selected
        if (y > 228) {
            y = 228;
        }

        valenceArousalModel.setTopAnchor(crosshairX, y - 1);
        valenceArousalModel.setTopAnchor(crosshairY, y - 1);
    }
}
