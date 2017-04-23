package com.fraserjohnstone.tuner.utils;

import java.util.Random;

/**
 * This class generates random Integers and floats, with or without including negative values.
 *
 * @author Fraser Johnstone
 * @version 1.01 - 22.04.2017
 */
class RandomNum {

    /**
     * @param _min int
     * @param _max int
     */
    private static int randInt(int _min, int _max) {
        Random rand = new Random();
        int randomNum = rand.nextInt((_max - _min) + 1) + _min;
        return randomNum;
    }

    /**
     * @param _min int
     * @param _max int
     */
    public static int randIntPossNeg(int _min, int _max) {
        Random rand = new Random();
        int randomNum = rand.nextInt((_max - _min) + 1) + _min;

        //get either a 1 or a 2 to decide if its going to be negative
        int x = RandomNum.randInt(1, 2);
        if (x == 1) {
        } else {
            randomNum = randomNum * (-1);
        }

        return randomNum;
    }

    /**
     * @param _min float
     * @param _max float
     */
    public static float randFloat(float _min, float _max) {
        Random rand = new Random();
        float randomNum = rand.nextFloat() * (_max - _min) + _min;

        return randomNum;
    }

    /**
     * @param _min float
     * @param _max float
     */
    public static float randFloatPossNeg(float _min, float _max) {
        Random rand = new Random();
        float randomNum = rand.nextFloat() * (_max - _min) + _min;

        //get either a 1 or a 2 to decide if its going to be negative
        int x = RandomNum.randInt(1, 2);
        if (x != 1) {
            randomNum = randomNum * (-1);
        }

        return randomNum;
    }
}
