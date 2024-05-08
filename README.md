# Singing Together

***The application is currently under development.***

This is a multi-platform application that was created to help those who want to learn how to sing and hit the notes.
This will be useful to everyone who:

* Not sure if he sings some notes correctly.
* Wants to expand his musical range.
* Wants to learn to sing a certain song.

### How it works?

It's simple!

The application analyzes the PCM data stream from the system's standard microphone.
Next, the voice frequency per unit time is calculated,
and the note that corresponds to this frequency is found.

A similar process occurs with the user-selected audio file.

### Operating modes.

There are 2 modes in total:

* **Acapella** mode. When recording, only your voice will be taken into account.
* **Cover** mode. Before recording, you will be asked to select a track.
  Your voice will be compared to this track.
  It is highly recommended to select audio without music (only the singer's voice), otherwise the analyzer will not work
  incorrect. (Often, along with the release of a song, a version with voice only or Instrumental, if there is none, is
  released
  You can use an online voice highlighting tool).


