package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import jm.music.data.*;
import jm.util.Play;
import jm.util.View;

import static application.Consts.*;
import static jm.constants.Pitches.*;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("application.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();

        int key = C4;
        ValenceArousalModel model = new ValenceArousalModel(1, 0);
        double[] parameters = model.generateParameters();

        System.out.print(parameters[MAJOR_CHORDS] + " " + parameters[MINOR_CHORDS] + " " + parameters[DIMINISHED_CHORDS] + " " +
                parameters[DOMINANT_CHORDS] + " " + parameters[TONALITY] + " " + parameters[MODE] + " " + parameters[DIATONIC] +
                " " + parameters[PITCH] + " " + parameters[TEMPO] + " " + parameters[VOLUME] + " " + parameters[VELOCITY]
        );

        TransitionMatrixGenerator transitionMatrixGenerator = new TransitionMatrixGenerator(parameters[MAJOR_CHORDS], parameters[MINOR_CHORDS],
                parameters[DIMINISHED_CHORDS], parameters[DOMINANT_CHORDS], parameters[TONALITY], parameters[MODE], parameters[DIATONIC]);
        MusicGenerator musicGenerator = new MusicGenerator(key, parameters[PITCH]);
        int currentChord = 0;

        for (int i = 0; i < 4; i++) {
            double[] row = transitionMatrixGenerator.generateRow(currentChord);
            /*
            for (int j = 0; j < row.length; j++) {
                System.out.print(row[j] + " ");
            }
            System.out.print("\n");
            */

           currentChord = musicGenerator.addToParts(row);
        }

        Score score = new Score();
        score.addPart(musicGenerator.getChordsPart());
        score.addPart(musicGenerator.getLeadPart());
        score.addPart(musicGenerator.getBassPart());
        score.setTempo(parameters[TEMPO]);
        Play.midi(score);

    }

    public static void main(String[] args) {
        launch(args);
    }
}
