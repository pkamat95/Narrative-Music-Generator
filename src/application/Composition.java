package application;

import jm.music.data.Part;
import jm.music.data.Phrase;
import jm.music.data.Score;
import jm.music.tools.Mod;

/**
 * Created by praneilkamat on 09/02/2018.
 */
public class Composition {
    private Section[] sections;
    private Score score;
    private double timeAdjustment = 0;
    private int DURATIONOFPHRASE = 4;
    private int NUMBEROFPARTS = 3;
    private boolean isStart = true;

    public Composition(Section[] sections) {
        this.sections = sections;
        score = new Score();
    }

    public void generateComposition() {
        score.empty();
        isStart = true;

        int i, startPoint;
        for (i = 0; i < sections.length; i++) {

            sections[i].generateSection();
            Mod.append(score, sections[i].getScore());

            // adjust the startTimes of all phrases in the current section based on the tempo from the previous section (to account for tempo)
            if (!isStart) {
                startPoint = score.getPartArray()[1].getSize() - sections[i].getSectionLength();
                adjustStartTimes(score.getPartArray(), startPoint, sections[i].getSectionLength());
            }

            // calculate startTimeAdjustment for next section
            timeAdjustment = DURATIONOFPHRASE - (DURATIONOFPHRASE * (60/sections[i].getTempo()));

            // Add transition section to score here

            if (isStart) {
                isStart = false;
            }
        }
    }

    private void adjustStartTimes(Part[] parts, int startPoint, int sectionLength) {
        Phrase phrase;

        int j, k;
        for (j = startPoint ; j < (startPoint + sectionLength); j++) {

            // adjust phrase startTimes for chords part
            for (k = 0; k < NUMBEROFPARTS; k++) {
                phrase = parts[0].getPhrase((j * NUMBEROFPARTS) + k);
                phrase.setStartTime(phrase.getStartTime() - timeAdjustment);
            }

            // adjust phrase startTimes for lead part
            phrase = parts[1].getPhrase(j);
            phrase.setStartTime(phrase.getStartTime() - timeAdjustment);

            // adjust phrase startTimes for bass part
            phrase = parts[2].getPhrase(j);
            phrase.setStartTime(phrase.getStartTime() - timeAdjustment);

        }
    }

    public Score getScore() {
        return score.copy();
    }
}
