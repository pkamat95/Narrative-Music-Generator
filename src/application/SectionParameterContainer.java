package application;

/**
 * Created by praneilkamat on 24/02/2018.
 */
public class SectionParameterContainer {
    private ValenceArousalModel valenceArousalModel = new ValenceArousalModel(0.5, 0.5);
    private Integer sectionLengthMinutes = null;
    private Integer sectionLengthSeconds = null;
    private Integer sectionLengthInBars = null;
    private Integer startTransitionLength = null;
    private Integer endTransitionLength = null;
    private Double tempo;
    private boolean isComplete = false;

    public void setValence(double valence) {
        valenceArousalModel.setValence(valence);
        calculateSectionLengthInBarsAndUpdateTempo();
    }

    public double getValence() {
        return valenceArousalModel.getValence();
    }

    public void setArousal(double arousal) {
        valenceArousalModel.setArousal(arousal);
        calculateSectionLengthInBarsAndUpdateTempo();
    }

    public double getArousal() {
        return valenceArousalModel.getArousal();
    }

    public void setSectionLengthMinutes(Integer sectionLengthMinutes) {
        this.sectionLengthMinutes = sectionLengthMinutes;
        calculateSectionLengthInBarsAndUpdateTempo();
        checkIfComplete();
    }

    public Integer getSectionLengthMinutes() {
        return sectionLengthMinutes;
    }

    public void setSectionLengthSeconds(Integer sectionLengthSeconds) {
        this.sectionLengthSeconds = sectionLengthSeconds;
        calculateSectionLengthInBarsAndUpdateTempo();
        checkIfComplete();
    }

    public Integer getSectionLengthSeconds() {
        return sectionLengthSeconds;
    }

    public Integer getSectionLengthInBars() {
        return sectionLengthInBars;
    }

    public void setStartTransitionLength(Integer startTransitionLength) {
        this.startTransitionLength = startTransitionLength;
        checkIfComplete();
    }

    public Integer getStartTransitionLength() {
        return startTransitionLength;
    }

    public void setEndTransitionLength(Integer endTransitionLength) {
        this.endTransitionLength = endTransitionLength;
        checkIfComplete();
    }

    public Integer getEndTransitionLength() {
        return endTransitionLength;
    }

    public boolean isComplete() {
        return isComplete;
    }

    private void checkIfComplete() {
        if (sectionLengthMinutes != null &&
                sectionLengthSeconds != null) {
            isComplete = true;
        }
        else {
            isComplete = false;
        }
    }

    private void calculateSectionLengthInBarsAndUpdateTempo() {
        if (sectionLengthMinutes != null && sectionLengthSeconds != null) {
            double tempo = valenceArousalModel.generateTempo();
            double totalMinutes = calculateTotalMinutes();

            sectionLengthInBars = (int) Math.round((totalMinutes * tempo) / 4);

            // calculate this so that the tempo ensures that the time of the section will be equal to what the user set
            this.tempo = (sectionLengthInBars / totalMinutes) * 4;
        }
        else {
            sectionLengthInBars = null;
            tempo = null;
        }
    }

    private double calculateTotalMinutes() {
        double minutes = (double) sectionLengthMinutes;
        double seconds = (double) sectionLengthSeconds;

        return minutes + (seconds / 60);
    }

    public double getTempo() {
        if (tempo != null) {
            return tempo;
        }
        else {
            return valenceArousalModel.generateTempo();
        }
    }

    public double[] generateParameters() {
        return valenceArousalModel.generateParameters();
    }
}
