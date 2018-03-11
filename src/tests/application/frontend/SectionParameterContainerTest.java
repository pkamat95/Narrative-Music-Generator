package application.frontend;

import org.junit.Test;

import static org.junit.Assert.*;

public class SectionParameterContainerTest {
    @Test
    public void isCompleteWhenSectionDurationIsFilledIn() {
        SectionParameterContainer sectionParameterContainer = new SectionParameterContainer();
        assertFalse(sectionParameterContainer.isComplete());

        sectionParameterContainer.setSectionLengthMinutes(1);
        assertFalse(sectionParameterContainer.isComplete());

        sectionParameterContainer.setSectionLengthSeconds(30);
        assertTrue(sectionParameterContainer.isComplete());
    }
}