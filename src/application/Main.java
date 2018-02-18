package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import jm.music.data.*;
import jm.util.Play;

import static jm.constants.Pitches.*;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("application.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();

        // create list of sections
        Section[] sections = new Section[3];
        // set key
        int key = C4;
        // create model with emotion inputs
        ValenceArousalModel model = new ValenceArousalModel();

        model.setValence(0.5);
        model.setArousal(0.5);
        sections[0] = new Section(model.generateParameters(), key, 5, true, false, 2, 2);

        model.setValence(0);
        model.setArousal(1);
        sections[1] = new Section(model.generateParameters(), key, 5,false, false, 2, 2);

        model.setValence(1);
        model.setArousal(0);
        sections[2] = new Section(model.generateParameters(), key, 5,false, true, 2, 2);

        Composition composition = new Composition(sections);
        composition.generateComposition();

        Score score = composition.getScore();

        Play.midi(score);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
