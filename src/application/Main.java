package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import jm.music.data.CPhrase;
import jm.music.data.Note;
import jm.music.data.Phrase;

import java.util.Vector;

import static jm.constants.Pitches.*;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("application.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();

        TransitionMatrixGenerator transitionMatrixGenerator = new TransitionMatrixGenerator(0.5, 1, 0.25,
                0.25, 1, 1, 0.5);

        transitionMatrixGenerator.generateRow(14);
        double[] row = transitionMatrixGenerator.getTransitionMatrix().getRow(14);
        for(int i = 0; i < row.length; i++) {
            System.out.print(row[i] + " ");
        }
        System.out.print("\n");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
