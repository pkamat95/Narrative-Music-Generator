package application;

import jm.music.data.CPhrase;

import java.util.Random;

/**
 * Created by praneilkamat on 25/01/2018.
 */
public class ChordSelector {

    public int selectChord(double[] row) {

        // get sum of row
        double sum = 0;
        int i;
        for (i = 0; i < row.length; i++) {
            sum += row[i];
        }

        // generate a random number between 0 and the sum
        Random random = new Random();
        double randomNumber = random.nextDouble() * sum;

        // find the index (chord) corresponding with the random number
        sum = 0;
        for (i = 0; i < row.length; i++) {
            sum += row[i];
            if (randomNumber < sum) {
                return i;
            }
        }

        return -1;
    }
}
