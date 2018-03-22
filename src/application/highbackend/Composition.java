package application.highbackend;

import jm.music.data.Part;
import jm.music.data.Phrase;
import jm.music.data.Score;
import jm.music.tools.Mod;

import static application.Consts.*;

// generates a score based on an array of sections
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
        int currentChord = I; // root major chord - could improve this by adjusting major/minor based on mode
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

            // calculate amount to adjust by for next bar - need to do this when there is a potential tempo change next bar
            timeAdjustment = PHRASE_DURATION - (PHRASE_DURATION * (60/sections[i].getTempo()));

            boolean isNotLast = (i != sections.length - 1);
            // Add transition section to score
            if (isNotLast) { // generate transition unless we're on the last section
                transitionLength = sections[i].getEndTransitionLength() + sections[i+1].getStartTransitionLength();
                for (j = 0; j < transitionLength; j++) {
                    transitionHelper.interpolateParameters(sections[i].getParameters(), sections[i+1].getParameters(), j, transitionLength);

                    startPoint = score.getPartArray()[1].getSize();
                    currentChord = transitionHelper.generateTransitionPart(currentChord);
                    Mod.append(score, transitionHelper.getTransitionPart());

                    // need to adjust start times after each chord
                    adjustStartTimes(score.getPartArray(), startPoint, 1, timeAdjustment);

                    // calculate amount to adjust by for next bar
                    timeAdjustment = PHRASE_DURATION - (PHRASE_DURATION * (60/transitionHelper.getTempo()));
                }
            }

            if (isStart) {
                isStart = false;
            }
        }
    }

    /* this is done as JMusic doesn't automatically adjust start times
       when there are tempo changes, so without doing this there would be gaps/overlaps in the composition
       between each section and each bar in the transition */
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
