package com.fraserjohnstone.tuner.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;

import com.fraserjohnstone.tuner.Tuner;
import com.fraserjohnstone.tuner.utils.ChromaticScale;
import com.fraserjohnstone.tuner.utils.Note;

import java.text.DecimalFormat;
import java.util.ArrayList;

import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.io.android.AudioDispatcherFactory;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchDetectionResult;
import be.tarsos.dsp.pitch.PitchProcessor;
import be.tarsos.dsp.pitch.PitchProcessor.PitchEstimationAlgorithm;

/**
 * This class is the main {@link Screen} where the Tuner is displayed.
 *
 * @version 1.01 - 22.04.2017
 * @author  Fraser Johnstone
 */
public class TunerScreen implements Screen{

    //loop limiter. This value restricts the updating of certain values through the life of the application
    private int mLoopCount = 0;
    private int mLoopSize = 8;

    //Tuner settings - further settings that do not require member score are defined in initTuner()

        //chops up sound stream and feeds data to pitch
        private AudioDispatcher mAudioDispatcher;
        //hertz value returned from the pitch detection
        private double mCurrentHertz;
        //how far away the detected pitch is from the desired pitch - (uses 'cents = 1200 * log2(f2/f1)'
        //where f2 is always the higher pitch)
        private double mOffsetInCents;
        //keeps track of whether the note is sharp of flat will be either 'flat' or 'sharp'
        private String mSharpOrFlat;
        //the note currently being targeted by the Tuner (used for note wheel rotation)
        private String mTargetNote;
        //will hold the target rotation value for each note on the note wheel
        private double mTargetRotation;
        //mAudioDetected will be false for any sound detected below this volume
        private int mSilenceThreshold = -70;
        private boolean mAudioDetected = false;

    //UI
        //fonts
        private BitmapFont mCandaraBlackFont;
        private BitmapFont mCandaraWhiteFont;
        private static GlyphLayout glyphLayout = new GlyphLayout();

        //bg
        private Sprite mBgLine;
        private ArrayList<Sprite> mBgLineArray = new ArrayList<Sprite>();
        private int mNumberOfBgLines = 20;

        //note wheel
        private Sprite mNoteWheel;
        private Sprite mHertzReadoutBg;
        private double mNoteWheelRotation = 0;

    //string fields
        //hertz readout
        private String mHertzReadout = "-";
        private float mHertzReadoutXPos;
        private float mHertzReadoutYPos;

        //how flat or sharp
        private String mHowSharpOrFlatText = "-";
        private float mHowSharpOrFlatTextXPos;
        private float mHowSharpOrFlatTextYPos;

    //how sharp or flat in cents visualization
    private Sprite mHowSharpOrFlatCenter;
    private Sprite mHowSharp;
    private Sprite mHowFlat;
    private Sprite mSharpOrFlatSymbol;

    //update allowed flag - nothing in the application will update if this is false
    private boolean mUpdateAllowed = false;

    //note definitions
    private ChromaticScale mChromaticScale;

    //reference to main Tuner class
    private Tuner mTuner;

    /**
     * Class constructor.
     *
     * @param _tuner  {@link Tuner}. Gives this Screen access to {@link Tuner} instance
     *                which extends {@link com.badlogic.gdx.Game}.
     */
    public TunerScreen(Tuner _tuner){
    	mTuner = _tuner;
    }

    @Override
    public void show(){
        //prepare the Tuner to start
        initTuner();

        //get the frequencies of all octaves of all notes
        mChromaticScale = new ChromaticScale();

        //create and add ui elements
        addUiElements();
    }

    /**
     * Calls the update(float) method and then calls the present(float) method to draw everything to
     * the screen.
     *
     * @param _delta  float. The amount of time taken to render the previous frame.
     */
    @Override
    public void render(float _delta){
        update(_delta);
        present(_delta);
    }

    /**
     * @param _delta  float. The time taken to render the previous frame.
     */
    private void update(float _delta){
        //check that the ui has been created and that we are allowed to start processing from the
        //audio detected by the Tuner
        if(mUpdateAllowed){
            //get the current Hertz value of the sound being detected and process.
            if(mAudioDetected) {
                processHertz(mCurrentHertz);
            }
            //update the hertz string.
            DecimalFormat twoDecPlacesFormat = new DecimalFormat("#.00");
            String currentHertzTwoDecimalPlaces = twoDecPlacesFormat.format(mCurrentHertz);
            mHertzReadout = String.valueOf(currentHertzTwoDecimalPlaces);

            //update sprites
                //note wheel
                updateNoteWheelRotation();
                mNoteWheel.setRotation((float) mNoteWheelRotation);

            //update the how flat or sharp visualisation
            updateHowFlatOrSharp();

            if(mLoopCount < mLoopSize){ mLoopCount++; }
            else{ mLoopCount = 0; }
        }

    }

    /**
     * Draws the user interface elements to the screen. This method is called after update(float).
     *
     * @param _delta  float. The time taken to render the previous frame.
     */
    private void present(float _delta){
        //clear the screen
    	Gdx.gl.glClearColor(0, 0, 0, 0);
    	Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //start drawing
        if(mUpdateAllowed) {
            mTuner.mBatch.begin();

            //bg
            for(int i = 0; i< mBgLineArray.size(); i++){
                mBgLineArray.get(i).draw(mTuner.mBatch);
            }

            //note wheel
            mNoteWheel.draw(mTuner.mBatch);
            mHertzReadoutBg.draw(mTuner.mBatch);

            //how sharp or flat
            mHowSharpOrFlatCenter.draw(mTuner.mBatch);
            mHowSharp.draw(mTuner.mBatch);
            mHowFlat.draw(mTuner.mBatch);
            mSharpOrFlatSymbol.draw(mTuner.mBatch);

            //any text (drawn after everything else with the exception of the menu)
            //hertz text
            mCandaraWhiteFont.getData().setScale(0.75f);
            glyphLayout.setText(mCandaraWhiteFont, mHertzReadout);
            mCandaraWhiteFont.draw(
                    mTuner.mBatch,
                    mHertzReadout,
                    mHertzReadoutXPos -(glyphLayout.width/2),
                    mHertzReadoutYPos);

            //cents sharp or flat
            mCandaraBlackFont.getData().setScale(2f);
            glyphLayout.setText(mCandaraBlackFont, mHowSharpOrFlatText);
            mCandaraBlackFont.draw(
                    mTuner.mBatch,
                    mHowSharpOrFlatText,
                    mHowSharpOrFlatTextXPos -(glyphLayout.width/2),
                    mHowSharpOrFlatTextYPos);

            mTuner.mBatch.end();
        }
    }

    /**
     * Initiates the Tuner and starts it in a new thread
     */
    private void initTuner(){
        if(mAudioDispatcher == null){
            //set initial Tuner values
            PitchEstimationAlgorithm algo = PitchEstimationAlgorithm.FFT_YIN;
            mCurrentHertz = 440.00;
            int sampleRate = 44100;
            int bufferSize = 2048;
            int sampleOverlap = 1536;
            mOffsetInCents = 0;
            mSharpOrFlat = "";
            mTargetNote = "A";
            mTargetRotation = 0;

            //Get an audio stream from the microphone
            AudioDispatcher dispatcher =
                    AudioDispatcherFactory.fromDefaultMicrophone(sampleRate, bufferSize, sampleOverlap);
            dispatcher.addAudioProcessor(new PitchProcessor(algo,
                                         sampleRate,
                                         bufferSize,
                                         new PitchDetectionHandler() {
                                             @Override
                                             public void handlePitch(
                                                     PitchDetectionResult pitchDetectionResult,
                                                     AudioEvent audioEvent) {

                                                 //set the current pitch to the hertz value
                                                 double pitch = pitchDetectionResult.getPitch();
                                                 if(pitch != -1 && !audioEvent.isSilence(mSilenceThreshold)){
                                                     mAudioDetected = true;
                                                     mCurrentHertz = pitch;
                                                 }
                                                 else{
                                                     mAudioDetected = false;
                                                     mOffsetInCents = 0.00;
                                                 }
                                             }
                                         }));

            //start the audio Audio Dispatcher in a new thread
            new Thread(dispatcher,"Audio Dispatcher").start();
        }
    }

    /**
	 * Creates all of the UI elements
     */
	private void addUiElements(){
        //initialise fonts
        mCandaraBlackFont = mTuner.mAssetManager.get("ui/fonts/candara_black.fnt");
        mCandaraWhiteFont = mTuner.mAssetManager.get("ui/fonts/candara_white.fnt");

        //background lines
        for(int i = 0; i< mTuner.mScreenWidthPixels / mNumberOfBgLines; i++){
            mBgLine = new Sprite((Texture) mTuner.mAssetManager.get("images/bg/white_pixel.png"));
            mBgLineArray.add(mBgLine);
            mBgLine.setSize(1, mTuner.mScreenHeightPixels);
            mBgLine.setAlpha(0.15f);
            mBgLine.setPosition((i* mNumberOfBgLines)+(mNumberOfBgLines /2), 0);
        }

        //note wheel
        mNoteWheel = new Sprite((Texture) mTuner.mAssetManager.get("images/note_wheel_sharps.png"));
        mNoteWheel.setSize(mTuner.mScreenWidthPixels +(mTuner.mScreenWidthPixels *0.4f),
                mTuner.mScreenWidthPixels +(mTuner.mScreenWidthPixels *0.4f));
        mNoteWheel.setOriginCenter();
        mNoteWheel.setPosition((mTuner.mScreenWidthPixels /2)-(mNoteWheel.getWidth()/2),
                -(mTuner.mScreenHeightPixels)*.5f);

        //hertz readout (positioned relative to the note wheel)
            //bg
            mHertzReadoutBg =  new Sprite((Texture) mTuner.mAssetManager.get("images/hertz_readout_bg.png"));
            mHertzReadoutBg.setSize(mTuner.mScreenWidthPixels *0.5f, mTuner.mScreenWidthPixels *0.5f);
            mHertzReadoutBg.setPosition(mTuner.mScreenWidthPixels /2-(mHertzReadoutBg.getWidth()/2),
                    mNoteWheel.getY()+ mNoteWheel.getHeight()*0.55f);
        //text
            //hertz readout position
            mHertzReadoutXPos = mTuner.mScreenWidthPixels /2;
            mHertzReadoutYPos = mHertzReadoutBg.getY()+(mHertzReadoutBg.getHeight()*.52f);

        //How sharp or flat indication
            //center image
            mHowSharpOrFlatCenter = new Sprite((Texture) mTuner.mAssetManager.get("images/cents_sharp_or_flat_center.png"));
            mHowSharpOrFlatCenter.setSize(mTuner.mScreenWidthPixels *0.35f, mTuner.mScreenWidthPixels *0.35f);
            mHowSharpOrFlatCenter.setPosition((mTuner.mScreenWidthPixels /2)-(mHowSharpOrFlatCenter.getWidth()/2),
                    (mTuner.mScreenHeightPixels *.785f)-(mHowSharpOrFlatCenter.getHeight()/2));

            //how far flat
            mHowFlat = new Sprite((Texture) mTuner.mAssetManager.get("images/how_flat_cents.png"));
            float howFlatWidth = (mTuner.mScreenWidthPixels /2)-(mHowSharpOrFlatCenter.getWidth()/2);
            mHowFlat.setSize(howFlatWidth, howFlatWidth/4);
            mHowFlat.setOrigin(mHowFlat.getWidth(), mHowFlat.getHeight()/2);
            mHowFlat.setPosition(0, mHowSharpOrFlatCenter.getY()+((mHowSharpOrFlatCenter.getHeight()/2)
                    -(mHowFlat.getHeight()/2)));
            mHowFlat.setAlpha(.5f);
            mHowFlat.setScale(0f);

            //how far sharp
            mHowSharp = new Sprite((Texture) mTuner.mAssetManager.get("images/how_sharp_cents.png"));
            float howSharpWidth = (mTuner.mScreenWidthPixels /2)-(mHowSharpOrFlatCenter.getWidth()/2);
            mHowSharp.setSize(howSharpWidth, howSharpWidth/4);
            mHowSharp.setOrigin(0, mHowSharp.getHeight()/2);
            mHowSharp.setPosition(mTuner.mScreenWidthPixels /2+(mHowSharpOrFlatCenter.getWidth()/2),
                    mHowSharpOrFlatCenter.getY()+((mHowSharpOrFlatCenter.getHeight()/2)-(mHowSharp.getHeight()/2)));
            mHowSharp.setAlpha(.5f);
            mHowSharp.setScale(0f);

            //symbol

            mSharpOrFlatSymbol = new Sprite((Texture) mTuner.mAssetManager.get("images/flat_symbol.png"));
            mSharpOrFlatSymbol.setSize(mTuner.mScreenWidthPixels *.23f, mTuner.mScreenWidthPixels *.23f);
            mSharpOrFlatSymbol.setOriginCenter();
            mSharpOrFlatSymbol.setPosition(mTuner.mScreenWidthPixels /2-(mSharpOrFlatSymbol.getWidth()/2),
                    (mTuner.mScreenHeightPixels *0.5f)-(mSharpOrFlatSymbol.getHeight()*0.55f));
            //text
            mHowSharpOrFlatTextXPos = mTuner.mScreenWidthPixels /2;
            mHowSharpOrFlatTextYPos = mHowSharpOrFlatCenter.getY()+(mHowSharpOrFlatCenter.getHeight()*0.69f);

        //allow updates now that all of the ui is created
        mUpdateAllowed = true;
	}

    /**
     * Processes the current pitch being detected by the Tuner.
     * <p>
     * This method calculates the closest pitch to the observed sound.
     *
     * @param _htz  double. The pitch detected by the device microphone in hertz
     */
    private void processHertz(double _htz){
        Note[] noteArray = mChromaticScale.getNoteArray();
        double diffInHertz = 10000;

        //for each octave
        for(byte i=0; i<noteArray.length; i++){
            Note currentNote = noteArray[i];

            //for each octave representation of this note
            for(byte j=0; j<currentNote.getOctaves().length; j++){
                double currentOctave = currentNote.getOctaves()[j];
                double currentDiff = Math.abs(_htz-currentOctave);
                if((currentDiff < diffInHertz)){						//if the current difference is smaller than the previous smallest difference update it
                    diffInHertz = currentDiff;
                    mTargetNote = currentNote.toString();
                    if(_htz<currentOctave){
                        mOffsetInCents = Math.abs((int)Math.round(1200*log2(currentOctave/_htz)));
                        mSharpOrFlat = "flat";
                        if(mLoopCount == 0){
                            mHowSharpOrFlatText = "-" + String.valueOf((int) mOffsetInCents);
                        }
                    }
                    else if(_htz>currentOctave){
                        mOffsetInCents = Math.abs((int)Math.round(1200*log2(currentOctave/_htz)));
                        mSharpOrFlat = "sharp";
                        if(mLoopCount == 0){
                            mHowSharpOrFlatText = "+" + String.valueOf((int) mOffsetInCents);
                        }
                    }
                    else if(_htz == currentOctave){
                        mOffsetInCents = 0.00;
                        mHowSharpOrFlatText = "0";
                        mSharpOrFlat = "sharp";
                    }
                }
            }
        }
    }

    /**
     * Updates the rotation of the note wheel on the screen allowing the user to see a
     * rough estimate of their tuning without reading exact numbers
     */
    private void updateNoteWheelRotation(){
        double rotation = 0;
        if(mTargetNote.equals("A")){ mTargetRotation = 0; }
        if(mTargetNote.equals("A#")){ mTargetRotation = 30; }
        if(mTargetNote.equals("B")){ mTargetRotation = 60; }
        if(mTargetNote.equals("C")){ mTargetRotation = 90; }
        if(mTargetNote.equals("C#")){ mTargetRotation = 120; }
        if(mTargetNote.equals("D")){ mTargetRotation = 150; }
        if(mTargetNote.equals("D#")){ mTargetRotation = 180; }
        if(mTargetNote.equals("E")){ mTargetRotation = -150; }
        if(mTargetNote.equals("F")){ mTargetRotation = -120; }
        if(mTargetNote.equals("F#")){ mTargetRotation = -90; }
        if(mTargetNote.equals("G")){ mTargetRotation = -60; }
        if(mTargetNote.equals("G#")){ mTargetRotation = -30; }

        if(mSharpOrFlat.equals("flat")){ rotation = mTargetRotation - ((30.0/100.0)* mOffsetInCents); }
        if(mSharpOrFlat.equals("sharp")){ rotation = mTargetRotation + ((30.0/100.0)* mOffsetInCents); }
        if(mSharpOrFlat.equals("")){ rotation = 0; }

        mNoteWheelRotation = rotation;
    }

    /**
     * This method updates the specific values concerning how sharp of flat the sound being detected
     * is compared to the target note.
     */
    private void updateHowFlatOrSharp(){
        //only if the Tuner has detected audio
        if(mAudioDetected){
            int acceptableInTuneLimit = 5;                     //lower values mean the Tuner is more fussy
            double basicScaleValue = (mOffsetInCents *0.02f);    //value chosen by trial and error to
                                                               //achieve an appropriate scaling of
                                                               //ui elements
            float minScale = 0.5f;                  //the smallest the scale of mSharpOrFlatSymbol
                                                    // can be

            //check if the sound detected is sharp or flat
            if(mSharpOrFlat.equals("flat")){
                mHowFlat.setScale((float) basicScaleValue);
                mHowSharp.setScale(0);
                //display flat symbol if mOffsetInCents is >= 1
                if(mOffsetInCents >= 1){
                    mSharpOrFlatSymbol.setScale((float) (basicScaleValue*0.5)+minScale);
                    mSharpOrFlatSymbol.setAlpha(1);
                    mSharpOrFlatSymbol.setTexture((Texture) mTuner.mAssetManager.get("images/flat_symbol.png"));
                    //change colour of the centre image if the cents are within a certain limit
                    if(mOffsetInCents <= acceptableInTuneLimit){
                        mHowSharpOrFlatCenter.setTexture((Texture) mTuner.mAssetManager
                                .get("images/cents_sharp_or_flat_center_green.png"));
                    }
                    else{
                        mHowSharpOrFlatCenter.setTexture((Texture) mTuner.mAssetManager
                                .get("images/cents_sharp_or_flat_center.png"));
                    }
                }
                else if(mOffsetInCents == 0){
                    mSharpOrFlatSymbol.setAlpha(0);
                }
            }else if(mSharpOrFlat.equals("sharp")){
                mHowSharp.setScale((float) basicScaleValue);
                mHowFlat.setScale(0);
                //display flat symbol if mOffsetInCents is >= 1
                if(mOffsetInCents >= 1){
                    mSharpOrFlatSymbol.setScale((float) (basicScaleValue*0.5)+minScale);
                    mSharpOrFlatSymbol.setAlpha(1);
                    mSharpOrFlatSymbol.setTexture((Texture) mTuner.mAssetManager.get("images/sharp_symbol.png"));
                    //change colour of the centre image if the cents are within a certain limit
                    if(mOffsetInCents <= acceptableInTuneLimit){
                        mHowSharpOrFlatCenter.setTexture((Texture) mTuner.mAssetManager
                                .get("images/cents_sharp_or_flat_center_green.png"));
                    }
                    else{
                        mHowSharpOrFlatCenter.setTexture((Texture) mTuner.mAssetManager
                                .get("images/cents_sharp_or_flat_center.png"));
                    }
                }
                else if(mOffsetInCents == 0){
                    mSharpOrFlatSymbol.setAlpha(0);
                }
            }
        }
        //no sound has been detected so set everything to a neutral value
        else{
            mHowSharp.setScale(0);
            mHowFlat.setScale(0);
            mHowSharpOrFlatText = "0";
            mSharpOrFlatSymbol.setAlpha(0);
            mHowSharpOrFlatCenter.setTexture((Texture) mTuner.mAssetManager.get("images/cents_sharp_or_flat_center.png"));
        }
    }

    /**
     * @param  _num double. The number for which we return the base 2 logarithm
     * @return double.      The base 2 logarithm of a number
     */
    private double log2(double _num) {
        return (Math.log(_num)/Math.log(2));
    }

    /**
     * @param _width   int. The new width.
     * @param _height  int. The new height.
     */
    @Override
    public void resize(int _width, int _height){
    }

    @Override
    public void pause(){
        if(mAudioDispatcher != null){
            mAudioDispatcher.stop();
            mAudioDispatcher = null;
        }
    }

    @Override
    public void resume(){
        if(mAudioDispatcher == null){
            initTuner();
        }
    }

    @Override
    public void hide() {
    }

    /**
     * This method safely disposes of any disposable objects (see {@link com.badlogic.gdx.utils.Disposable})
     * created in this class.
     */
    @Override
    public void dispose(){
        if(mAudioDispatcher != null){
            mAudioDispatcher.stop();
            mAudioDispatcher = null;
        }
    }
}
