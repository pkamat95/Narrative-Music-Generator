# Narrative-Music-Generator
Prerequisites

NMGS is a Java application and therefore requires the Java Runtime Environment (ver. 8+) to run. JRE 8 can be downloaded on multiple platforms including Windows, Mac OS and Linux here: http://www.oracle.com/technetwork/java/javase/downloads/jre8-downloads-2133155.html

Once this is installed, run the jar file to start the application.

Overview

Narrative Music Generation System (NMGS) is a music generation system designed to generate music based on emotions inputted by the user. The application has been designed to require little to no musical knowledge to be able to use, with simplicity as a strong goal. This goal aims to combat overly complex music generators that require significant musical knowledge to understand how to operate them.

Features

Help Area:
A help area can be found at the bottom of the application. This displays helpful text while the user hovers over different areas of the application, and acts as a feedback mechanism for, for example, a composition has been successfully generated.

Sections:
A full composition is divided into ‘sections’. Each section has its own target emotion, so by putting sections together, compositions with multiple target emotions throughout can be created.

A composition will be initialised with one empty section. To add a new section, click the ‘+’ icon (this will add a section to the end of the composition), and use the drop-down menu bar to change between sections. Clicking the ‘-‘ icon will delete the currently selected section.

NMGS allows a maximum of 9 sections, with each section having a minimum duration of 6 seconds, and a maximum duration of 4 minutes and 59 seconds.

Target Emotion Selector:
The target emotion selector can be opened by clicking the ‘Click to Set Target Emotion’ button. The target emotion for the currently selected section will be affected. To change target emotion, click at the desired point on the graph and click the ‘Set Target Emotion’ button

The 2D graph used by the target emotion selector is known as a circumplex model. This is a 2D model, containing valence and arousal dimensions. Valence (horizontal axis) represents how Positive or Negative the emotion is, and arousal (vertical axis) represents the intensity of the emotion.

Transitions:
Transitions allow the music to smoothly transition between two sections. The transition length must be set in bars*, and the duration of the transition will be calculated and displayed. The duration of the transition is separate to the duration of the section and will therefore increase the total composition duration.

In the application, transitions are set to follow the currently selected section. Due to this, the transition area will not appear for the last section in the composition.

The transition length must be even and the max transition length is 24.

*Each chord in the music lasts for one bar. The transition length must be set in bars as the tempo of the transition is interpolated from the sections surrounding it. This means that setting a specific duration for the transition would not be possible. As changing the target emotion for a section can change its tempo, changing the target emotion for either section surrounding a transition may change the duration of the transition.

Generate Composition Button:
Once all added sections have been completed (all section durations are not empty), clicking the ‘Generate Composition’ button will generate the music according to what the user has specified and will load this generated music for playback. There is an element of randomness to generation, therefore the button can be used repeatedly for the same inputs and will generate slightly different output each time.

Users will be notified in the help area if the music cannot be generated (for example, because one of the sections has not been completed). Likewise, users will be notified in the case of music being successfully generated.

Reset Composition Button:
Clicking the ‘Reset Composition’ button will reset all changes made to sections. The composition will be reset to a single empty section. The currently loaded music will not be affected and it will still be possible to export this.

Playback:
The application provides some playback features to play the currently loaded music. Clicking the ‘Play Composition’/’Stop Composition’ button will play and stop the composition, while a seek bar below this will allow the user to seek to a certain point in the music.

Export to MIDI:
The ‘Export to MIDI’ button will open a window that allows the currently loaded music to be exported as a standard MIDI file. The file will contain three tracks, a ‘Bass’ track, a ‘Chords’ track and a ‘Lead’ track. Users can open this file in the Digital Audio Workstation of their choice to edit the music (for example, change the instruments for each track).
