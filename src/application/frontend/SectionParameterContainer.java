package application.frontend;

import application.lowbackend.ValenceArousalModel;

// holds all parameters needed to generate a section
class SectionParameterContainer {
    private ValenceArousalModel valenceArousalModel = new ValenceArousalModel();
    private Integer sectionLengthMinutes = null;
    private Integer sectionLengthSeconds = null;
    private Integer sectionLengthInBars = null;
    private Integer startTransitionLength = null;
    private Integer endTransitionLength = null;
    private Double tempo;
    private boolean isComplete = false;

    void setValence(double valence) {
        valenceArousalModel.setValence(valence);
        calculateSectionLengthInBarsAndUpdateTempo();
    }

    double getValence() {
        return valenceArousalModel.getValence();
    }

    void setArousal(double arousal) {
        valenceArousalModel.setArousal(arousal);
        calculateSectionLengthInBarsAndUpdateTempo();
    }

    double getArousal() {
        return valenceArousalModel.getArousal();
    }

    void setSectionLengthMinutes(Integer sectionLengthMinutes) {
        this.sectionLengthMinutes = sectionLengthMinutes;
        calculateSectionLengthInBarsAndUpdateTempo();
        checkIfComplete();
    }

    Integer getSectionLengthMinutes() {
        return sectionLengthMinutes;
    }

    void setSectionLengthSeconds(Integer sectionLengthSeconds) {
        this.sectionLengthSeconds = sectionLengthSeconds;
        calculateSectionLengthInBarsAndUpdateTempo();
        checkIfComplete();
    }

    Integer getSectionLengthSeconds() {
        return sectionLengthSeconds;
    }

    Integer getSectionLengthInBars() {
        return sectionLengthInBars;
    }

    void setStartTransitionLength(Integer startTransitionLength) {
        this.startTransitionLength = startTransitionLength;
        checkIfComplete();
    }

    Integer getStartTransitionLength() {
        return startTransitionLength;
    }

    void setEndTransitionLength(Integer endTransitionLength) {
        this.endTransitionLength = endTransitionLength;
        checkIfComplete();
    }

    Integer getEndTransitionLength() {
        return endTransitionLength;
    }

    double getTempo() {
        // if new tempo has been calculated, return this
        if (tempo != null) {
            return tempo;
        }
        // otherwise, return tempo from valence arousal model
        else {
            return valenceArousalModel.generateTempo();
        }
    }

    double[] generateParameters() {
        return valenceArousalModel.generateParameters();
    }

    boolean isComplete() {
        return isComplete;
    }

    private void checkIfComplete() {
        isComplete = (sectionLengthMinutes != null && sectionLengthSeconds != null);
    }

    private void calculateSectionLengthInBarsAndUpdateTempo() {
        if (sectionLengthMinutes != null && sectionLengthSeconds != null) {
            double rawTempo = valenceArousalModel.generateTempo(); // tempo before accounting for duration of the section
            double totalMinutes = calculateTotalMinutes();

            sectionLengthInBars = (int) Math.round((totalMinutes * rawTempo) / 4);

            // calculate new tempo such that it ensures that the time of the section will be equal to what the user set
            tempo = (sectionLengthInBars / totalMinutes) * 4;
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
}
