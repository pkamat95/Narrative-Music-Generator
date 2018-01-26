package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("application.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();

        ChordTransitionMatrix matrix = new ChordTransitionMatrix();
        ChordSelector chordSelector = new ChordSelector();
        int c = chordSelector.selectChord(matrix.getRow(0));
        System.out.println(c);
    }


    public static void main(String[] args) {
        launch(args);
    }
}
