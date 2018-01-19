# Acoustic Tuner For Android Devices

For author details please read the included AUTHORS file. 

For licence details please read the included LICENCE file.

If you would like to contact me regarding this application or to submit an issue you have found, please 
do so by emailing me at fraserjohnstone12345@hotmail.com.

## Overview

This Application is a simple yet extremely accurate Acoustic tuner for any musical instrument. It utilises
the fantastic Digital Signal Processing library by Joren Six (http://github.com/JorenSix/TarsosDSP). 
More specifically, this application utilises the implementation of a Fast Fourier Transform to
determine the pitch of sampled audio.

The application has a user interface which displays not only a note wheel which rotates to give the
user a quick way of checking if they are in tune or not, but also a more detailed read out of the
frequency of the detected audio as well as how many cents sharp or flat the audio is from a target
pitch. The FFT algorithm provided by Joren alongside the combination of the different elements 
of the user interface create an application which is efficient and easy to use whilst offering even the 
most demanding classical musician with an extremely accurate tuner ideal for any professional situation.

## Tuning Tolerance

The tuner has a tolerance level which can be changed easily. By default, the tuner accepts any pitch
within 5 cents of the target pitch as being 'in tune'. To change this simply change the value of the local
variable 'acceptableInTuneLimit' in the method 'TunerScreen.updateHowFlatOrSharp()'.

## Usage

Simply download this repository and run in Android Studio to install on a connected Android device. Upon
running the application for the first time you will be asked to provide audio recording permissions.
This is essential as the tuner will not work otherwise and you will not be able to get past the splash 
screen.

The target device must be equipped with at least SDK 23. 
