package com.fraserjohnstone.tuner.utils;

/**
 * Each instance of this class represents one musical note by name. Each octave of this note name
 * has a frequency in Hertz. Each of these are stored in the array {@link #octaves}.
 *
 * @author Fraser Johnstone
 * @version 1.01 - 22.04.2017
 */
public class Note {
    private double[] octaves = new double[9];
    private String stringRepresentation;

    /**
     * Class constructor
     *
     * @param octave_0
     * @param octave_1
     * @param octave_2
     * @param octave_3
     * @param octave_4
     * @param octave_5
     * @param octave_6
     * @param octave_7
     * @param octave_8
     * @param stringRepresentation
     */
    Note(double octave_0,
                double octave_1,
                double octave_2,
                double octave_3,
                double octave_4,
                double octave_5,
                double octave_6,
                double octave_7,
                double octave_8,
                String stringRepresentation) {
        octaves[0] = octave_0;
        octaves[1] = octave_1;
        octaves[2] = octave_2;
        octaves[3] = octave_3;
        octaves[4] = octave_4;
        octaves[5] = octave_5;
        octaves[6] = octave_6;
        octaves[7] = octave_7;
        octaves[8] = octave_8;

        this.stringRepresentation = stringRepresentation;
    }

    /**
     * @return String. The name of this note (F, Bb, etc)
     */
    public String toString() {
        return stringRepresentation;
    }

    /**
     * @return double[]. The array of frequencies for each octave of this note
     */
    public double[] getOctaves() {
        return octaves;
    }
}
