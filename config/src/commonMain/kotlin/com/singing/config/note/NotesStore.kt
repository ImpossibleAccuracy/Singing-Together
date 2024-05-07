package com.singing.config.note

object NotesStore {
    private val Notes: Array<NoteFrequency> = arrayOf(
        NoteFrequency("C0", 16.35),
        NoteFrequency("C#0/Db0", 17.32),
        NoteFrequency("D0", 18.35),
        NoteFrequency("D#0/Eb0", 19.45),
        NoteFrequency("E0", 20.60),
        NoteFrequency("F0", 21.83),
        NoteFrequency("F#0/Gb0", 23.12),
        NoteFrequency("G0", 24.50),
        NoteFrequency("G#0/Ab0", 25.96),
        NoteFrequency("A0", 27.50),
        NoteFrequency("A#0/Bb0", 29.14),
        NoteFrequency("B0", 30.87),
        NoteFrequency("C1", 32.70),
        NoteFrequency("C#1/Db1", 34.65),
        NoteFrequency("D1", 36.71),
        NoteFrequency("D#1/Eb1", 38.89),
        NoteFrequency("E1", 41.20),
        NoteFrequency("F1", 43.65),
        NoteFrequency("F#1/Gb1", 46.25),
        NoteFrequency("G1", 49.00),
        NoteFrequency("G#1/Ab1", 51.91),
        NoteFrequency("A1", 55.00),
        NoteFrequency("A#1/Bb1", 58.27),
        NoteFrequency("B1", 61.74),
        NoteFrequency("C2", 65.41),
        NoteFrequency("C#2/Db2", 69.30),
        NoteFrequency("D2", 73.42),
        NoteFrequency("D#2/Eb2", 77.78),
        NoteFrequency("E2", 82.41),
        NoteFrequency("F2", 87.31),
        NoteFrequency("F#2/Gb2", 92.50),
        NoteFrequency("G2", 98.00),
        NoteFrequency("G#2/Ab2", 103.83),
        NoteFrequency("A2", 110.00),
        NoteFrequency("A#2/Bb2", 116.54),
        NoteFrequency("B2", 123.47),
        NoteFrequency("C3", 130.81),
        NoteFrequency("C#3/Db3", 138.59),
        NoteFrequency("D3", 146.83),
        NoteFrequency("D#3/Eb3", 155.56),
        NoteFrequency("E3", 164.81),
        NoteFrequency("F3", 174.61),
        NoteFrequency("F#3/Gb3", 185.00),
        NoteFrequency("G3", 196.00),
        NoteFrequency("G#3/Ab3", 207.65),
        NoteFrequency("A3", 220.00),
        NoteFrequency("A#3/Bb3", 233.08),
        NoteFrequency("B3", 246.94),
        NoteFrequency("C4", 261.63),
        NoteFrequency("C#4/Db4", 277.18),
        NoteFrequency("D4", 293.66),
        NoteFrequency("D#4/Eb4", 311.13),
        NoteFrequency("E4", 329.63),
        NoteFrequency("F4", 349.23),
        NoteFrequency("F#4/Gb4", 369.99),
        NoteFrequency("G4", 392.00),
        NoteFrequency("G#4/Ab4", 415.30),
        NoteFrequency("A4", 440.00),
        NoteFrequency("A#4/Bb4", 466.16),
        NoteFrequency("B4", 493.88),
        NoteFrequency("C5", 523.25),
        NoteFrequency("C#5/Db5", 554.37),
        NoteFrequency("D5", 587.33),
        NoteFrequency("D#5/Eb5", 622.25),
        NoteFrequency("E5", 659.25),
        NoteFrequency("F5", 698.46),
        NoteFrequency("F#5/Gb5", 739.99),
        NoteFrequency("G5", 783.99),
        NoteFrequency("G#5/Ab5", 830.61),
        NoteFrequency("A5", 880.00),
        NoteFrequency("A#5/Bb5", 932.33),
        NoteFrequency("B5", 987.77),
        NoteFrequency("C6", 1046.50),
        NoteFrequency("C#6/Db6", 1108.73),
        NoteFrequency("D6", 1174.66),
        NoteFrequency("D#6/Eb6", 1244.51),
        NoteFrequency("E6", 1318.51),
        NoteFrequency("F6", 1396.91),
        NoteFrequency("F#6/Gb6", 1479.98),
        NoteFrequency("G6", 1567.98),
        NoteFrequency("G#6/Ab6", 1661.22),
        NoteFrequency("A6", 1760.00),
        NoteFrequency("A#6/Bb6", 1864.66),
        NoteFrequency("B6", 1975.53),
        NoteFrequency("C7", 2093.00),
        NoteFrequency("C#7/Db7", 2217.46),
        NoteFrequency("D7", 2349.32),
        NoteFrequency("D#7/Eb7", 2489.02),
        NoteFrequency("E7", 2637.02),
        NoteFrequency("F7", 2793.83),
        NoteFrequency("F#7/Gb7 ", 2959.96),
        NoteFrequency("G7", 3135.96),
        NoteFrequency("G#7/Ab7", 3322.44),
        NoteFrequency("A7", 3520.00),
        NoteFrequency("A#7/Bb7", 3729.31),
        NoteFrequency("B7", 3951.07),
        NoteFrequency("C8", 4186.01),
        NoteFrequency("C#8/Db8", 4434.92),
        NoteFrequency("D8", 4698.63),
        NoteFrequency("D#8/Eb8", 4978.03),
        NoteFrequency("E8", 5274.04),
        NoteFrequency("F8", 5587.65),
        NoteFrequency("F#8/Gb8", 5919.91),
        NoteFrequency("G8", 6271.93),
        NoteFrequency("G#8/Ab8", 6644.88),
        NoteFrequency("A8", 7040.00),
        NoteFrequency("A#8/Bb8", 7458.62),
        NoteFrequency("B8", 7902.13),
    )

    fun default(): NoteFrequency = Notes.first()

    fun findNote(frequency: Double): String =
        Notes
            .firstOrNull {
                it.frequency >= frequency
            }
            ?.note
            ?: default().note
}