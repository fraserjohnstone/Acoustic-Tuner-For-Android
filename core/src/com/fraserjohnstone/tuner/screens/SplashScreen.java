package com.fraserjohnstone.tuner.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Scaling;
import com.fraserjohnstone.tuner.Tuner;

/**
 * This screen is displayed whilst the {@link Tuner#mAssetManager} is loading and the user is
 * prompted to provide permission to record audio.
 * <p>
 * A {@link TunerScreen} instance is set as the current screen once the {@link Tuner#mAssetManager}
 * has finished loading and the user has granted the application appropriate runtime permissions.
 * The permissions will be requested in the resume() method and the member variable
 * {@link #mScreenChangeAllowed} will only be true once the user has granted permission.
 */
public class SplashScreen implements Screen {

    //reference the main Tuner class instance to allow us access to sprite batch, asset manager etc.
    private Tuner mTuner;

    //flag to determine if the screen should be changed. Will be set to true once the user has given
    //any requested runtime permissions.
    private boolean mScreenChangeAllowed = false;

    //UI
    //stage
    private Stage mStage;

    //bg
    private Texture mBgTexture;
    private Image mBgImage;

    //spinner
    private Texture mSpinnerTexture;
    private Image mSpinnerImage;

    /**
     * Class constructor.
     * <p>
     * Creates all of the elements for the user interface and adds them to a stage.
     *
     * @param _tuner {@link Tuner}. Gives this class access to the main {@link com.badlogic.gdx.Game}
     *               class.
     */
    public SplashScreen(Tuner _tuner) {
        //reference the main Tuner class
        mTuner = _tuner;
        addUiElements();
    }

    /**
     * Creates the user interface objects and adds them to the stage.
     */
    private void addUiElements() {
        //create the stage
        mStage = new Stage();

        //create the bg
        mBgTexture = new Texture(Gdx.files.internal("images/splash/bg.png"), true);
        mBgTexture.setFilter(TextureFilter.MipMapLinearLinear, TextureFilter.Linear);
        mBgImage = new Image(mBgTexture);
        mBgImage.setScaling(Scaling.stretch);
        mBgImage.setPosition(0, 0);
        mBgImage.setSize(mTuner.getScreenWidthPix(), mTuner.getScreenHeightPix());

        //create the loader icons
        mSpinnerTexture = new Texture(Gdx.files.internal("images/splash/spinner.png"), true);
        mSpinnerTexture.setFilter(TextureFilter.MipMapLinearLinear, TextureFilter.Linear);

        mSpinnerImage = new Image(mSpinnerTexture);
        mSpinnerImage.setSize(mTuner.getScreenWidthPix() * 0.2f, mTuner.getScreenWidthPix() * 0.2f);
        mSpinnerImage.setPosition(mTuner.getScreenWidthPix() * 0.5f - mSpinnerImage.getWidth() * 0.5f,
                mTuner.getScreenHeightPix() * 0.5f - mSpinnerImage.getHeight() * 0.5f);
        mSpinnerImage.getColor().a = 0.75f;
        mSpinnerImage.setOrigin(mSpinnerImage.getWidth() * 0.5f, mSpinnerImage.getHeight() * 0.5f);

        //add ui elements to the stage
        mStage.addActor(mBgImage);
        mStage.addActor(mSpinnerImage);
    }

    /**
     * Draws the elements of the UI that have been added to the stage. The rotation of the spinner will
     * be set based on the loading progress of the {@link Tuner#mAssetManager}.
     * <p>
     * {@link #goToTunerScreen()} will only be called if the user has given permission to this
     * application to record audio, and the asset manager has finished loading.
     *
     * @param _delta float. The time taken to render the previous frame.
     */
    @Override
    public void render(float _delta) {
        //clear the screen and draw a black background
        Gdx.gl.glClearColor(0, 0, 0, 1); //black bg
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //check if the AssetManager instance has loaded and that the record audio permission
        //has been granted
        if (mScreenChangeAllowed && mTuner.getAssetManager().update()) {
            goToTunerScreen();
        }

        //update the spinner rotation
        mSpinnerImage.setRotation((360 / 100) * mTuner.getAssetManager().getProgress());

        //draw the stage
        mStage.draw();
    }

    /**
     * Changes the screen to an instance of {@link TunerScreen}
     */
    private void goToTunerScreen() {
        mScreenChangeAllowed = false;
        mTuner.setScreen(new TunerScreen(mTuner));
    }

    @Override
    public void resume() {
        //attempt to request the record audio permission if required
        mTuner.getActionResolver().requestPermissions(this);
    }

    @Override
    public void show(){}

    @Override
    public void hide(){}

    @Override
    public void pause(){}

    /**
     * @param _height int. The new width of the screen.
     * @param _width  int. The new height of the screen.
     */
    @Override
    public void resize(int _width, int _height) {}

    /**
     * Safely disposes of any disposable objects (see {@link com.badlogic.gdx.utils.Disposable})
     * created in this class.
     */
    @Override
    public void dispose() {
        mBgTexture.dispose();
        mSpinnerTexture.dispose();
        mStage.dispose();
    }

    /**
     * Getters and Setters
     *
     * @param _screenChangeAllowed
     */

    public void setScreenChangeAllowed(boolean _screenChangeAllowed) {
        mScreenChangeAllowed = _screenChangeAllowed;
    }
}
