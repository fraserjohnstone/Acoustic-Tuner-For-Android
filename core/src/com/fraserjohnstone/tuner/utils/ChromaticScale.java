package com.fraserjohnstone.tuner.utils;

/**
 * Upon instantiation this class will hold a {@link Note} instance for each musical note name.
 *
 * @author Fraser Johnstone
 * @version 1.01 - 22.04.2017
 */
public class ChromaticScale {
    //array to hold all of the notes
    private Note[] noteArray = new Note[12];

    /**
     * Class constructor
     */
    public ChromaticScale() {
        //fill all notes with hertz and string rep
        Note C = new Note(16.35, 32.70, 65.41, 130.80, 261.60, 523.30, 1047.00, 2093.00, 4186.00, "C");
        noteArray[0] = C;
        Note Cs = new Note(17.32, 34.65, 69.30, 138.60, 277.20, 554.40, 1109.00, 2217.00, 4435.00, "C#");
        noteArray[1] = Cs;
        Note D = new Note(18.35, 36.71, 73.42, 146.80, 293.70, 587.30, 1175.00, 2349.00, 4699.00, "D");
        noteArray[2] = D;
        Note Ds = new Note(19.45, 38.89, 77.78, 155.60, 311.10, 622.30, 1245.00, 2489.00, 4978.00, "D#");
        noteArray[3] = Ds;
        Note E = new Note(20.60, 41.20, 82.41, 164.80, 329.60, 659.30, 1319.00, 2637.00, 5274.00, "E");
        noteArray[4] = E;
        Note F = new Note(21.83, 43.65, 87.31, 174.60, 349.20, 698.50, 1397.00, 2794.00, 5588.00, "F");
        noteArray[5] = F;
        Note Fs = new Note(23.12, 46.25, 92.50, 185.00, 370.00, 740.00, 1480.00, 2960.00, 5920.00, "F#");
        noteArray[6] = Fs;
        Note G = new Note(24.50, 49.00, 98.00, 196.00, 392.00, 784.00, 1568.00, 3136.00, 6272.00, "G");
        noteArray[7] = G;
        Note Gs = new Note(25.96, 51.91, 103.80, 207.70, 415.30, 830.60, 1661.00, 3322.00, 6645.00, "G#");
        noteArray[8] = Gs;
        Note A = new Note(27.50, 55.00, 110.00, 220.00, 440.00, 880.00, 1760.00, 3520.00, 7040.00, "A");
        noteArray[9] = A;
        Note As = new Note(29.14, 58.27, 116.50, 233.10, 466.20, 932.30, 1865.00, 3729.00, 7459.00, "A#");
        noteArray[10] = As;
        Note B = new Note(30.87, 61.74, 123.50, 246.90, 493.90, 987.80, 1976.00, 3951.00, 7902.00, "B");
        noteArray[11] = B;
    }

    /**
     * getters and setters
     */
    public Note[] getNoteArray() {
        return noteArray;
    }
}
