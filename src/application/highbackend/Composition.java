package application.highbackend;

import jm.music.data.Part;
import jm.music.data.Phrase;
import jm.music.data.Score;
import jm.music.tools.Mod;

import static application.Consts.*;

public class Composition {
    private Section[] sections;
    private Score score;

    public Composition(Section[] sections) {
        this.sections = sections;
        score = new Score();
    }

    public void generateComposition() {
        score.empty();
        boolean isStart = true;

        double timeAdjustment = 0;
        int currentChord = I; // could change this starting chord based on mode
        TransitionHelper transitionHelper = new TransitionHelper(sections[0].getKey());
        int transitionLength;

        int i;
        int j;
        int startPoint;
        for (i = 0; i < sections.length; i++) {

            currentChord = sections[i].generateSection(currentChord);
            Mod.append(score, sections[i].getScore());

            // adjust the startTimes of all phrases in the current section based on the tempo from the previous section (to account for tempo)
            if (!isStart) {
                startPoint = score.getPartArray()[1].getSize() - sections[i].getSectionLength();
                adjustStartTimes(score.getPartArray(), startPoint, sections[i].getSectionLength(), timeAdjustment);
            }

            // calculate startTimeAdjustment for transition
            timeAdjustment = PHRASE_DURATION - (PHRASE_DURATION * (60/sections[i].getTempo()));

            // Add transition section to score
            if (i != sections.length - 1) { // generate transition unless we're on the last section
                transitionLength = sections[i].getEndTransitionLength() + sections[i+1].getStartTransitionLength();
                for (j = 0; j < transitionLength; j++) {
                    transitionHelper.interpolateParameters(sections[i].getParameters(), sections[i+1].getParameters(), j, transitionLength);

                    startPoint = score.getPartArray()[1].getSize();
                    currentChord = transitionHelper.generateTransitionPart(currentChord);
                    Mod.append(score, transitionHelper.getTransitionPart());

                    // need to adjust start times after each chord
                    adjustStartTimes(score.getPartArray(), startPoint, 1, timeAdjustment);

                    timeAdjustment = PHRASE_DURATION - (PHRASE_DURATION * (60/transitionHelper.getTempo()));
                }
            }

            if (isStart) {
                isStart = false;
            }
        }
    }

    private void adjustStartTimes(Part[] parts, int startPoint, int sectionLength, double timeAdjustment) {
        Phrase phrase;

        int j;
        int k;
        for (j = startPoint ; j < (startPoint + sectionLength); j++) {

            // adjust phrase startTimes for chords part
            for (k = 0; k < NUMBER_OF_NOTES_IN_CHORD; k++) {
                phrase = parts[0].getPhrase((j * NUMBER_OF_NOTES_IN_CHORD) + k);
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
