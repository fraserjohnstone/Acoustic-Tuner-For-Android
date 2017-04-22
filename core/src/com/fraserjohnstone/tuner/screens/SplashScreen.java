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
 * This screen is displayed whilst whilst the {@link Tuner#mAssetManager} is loading.
 * <p>
 * A {@link TunerScreen} instance is set as the current screen once the {@link Tuner#mAssetManager}
 * has finished loading and the user has granted the application appropriate runtime permissions.
 * The permissions will be requested in the resume() method.
 *
 * @author Fraser Johnstone
 * @version 1.01 - 22.04.2017
 */
public class SplashScreen implements Screen {

    //reference the main Tuner class instance to allow us access to sprite batch, asset manager etc.
    private Tuner tuner;

    //flag to determine if the screen should be changed. Will be set to true once the user has given
    //any requested runtime permissions.
    private boolean screenChangeAllowed = false;

    //UI
    //stage
    private Stage stage;

    //bg
    private Texture bgTexture;
    private Image bgImage;

    //spinner
    private Texture spinnerTexture;
    private Image spinnerImage;

    /**
     * Class constructor.
     *
     * @param _tuner {@link Tuner}. Gives this screen access to the main {@link com.badlogic.gdx.Game}
     *               class.
     */
    public SplashScreen(Tuner _tuner) {
        //reference the main Tuner class
        tuner = _tuner;

        //create the stage
        stage = new Stage();

        //create the User interface
        addUiElements();
    }

    /**
     * Creates the user interface objects and adds them to the stage.
     */
    private void addUiElements() {
        //create the bg
        bgTexture = new Texture(Gdx.files.internal("images/splash/bg.png"), true);
        bgTexture.setFilter(TextureFilter.MipMapLinearLinear, TextureFilter.Linear);
        bgImage = new Image(bgTexture);
        bgImage.setScaling(Scaling.stretch);
        bgImage.setPosition(0, 0);
        bgImage.setSize(tuner.mScreenWidthPixels, tuner.mScreenHeightPixels);

        //create the loader icons
        spinnerTexture = new Texture(Gdx.files.internal("images/splash/spinner.png"), true);
        spinnerTexture.setFilter(TextureFilter.MipMapLinearLinear, TextureFilter.Linear);

        spinnerImage = new Image(spinnerTexture);
        spinnerImage.setSize(tuner.mScreenWidthPixels * 0.2f, tuner.mScreenWidthPixels * 0.2f);
        spinnerImage.setPosition(tuner.mScreenWidthPixels * 0.5f - spinnerImage.getWidth() * 0.5f, tuner.mScreenHeightPixels * 0.5f - spinnerImage.getHeight() * 0.5f);
        spinnerImage.getColor().a = 0.75f;
        spinnerImage.setOrigin(spinnerImage.getWidth() * 0.5f, spinnerImage.getHeight() * 0.5f);

        //add ui elements to the stage
        stage.addActor(bgImage);
        stage.addActor(spinnerImage);
    }

    /**
     * Draws the elements of the UI that have been added to the stage. The rotation of the spinner will
     * be set based on the loading progress of the {@link Tuner#mAssetManager}.
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
        if (screenChangeAllowed && tuner.mAssetManager.update()) {
            goToTunerScreen();
        }

        //update the spinner rotation
        spinnerImage.setRotation((360 / 100) * tuner.mAssetManager.getProgress());

        //draw the stage
        stage.draw();
    }

    /**
     * Changes the screen to an instance of {@link TunerScreen}
     */
    private void goToTunerScreen() {
        screenChangeAllowed = false;
        tuner.setScreen(new TunerScreen(tuner));
    }

    @Override
    public void show() {
    }

    /**
     * @param _height int. The new width of the screen.
     * @param _width  int. The new height of the screen.
     */
    @Override
    public void resize(int _width, int _height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
        //request the record audio permission
        tuner.mActionResolver.requestPermissions(this);
    }

    @Override
    public void hide() {
    }

    /**
     * Safely disposes of any disposable objects (see {@link com.badlogic.gdx.utils.Disposable})
     * created in this class.
     */
    @Override
    public void dispose() {
        bgTexture.dispose();
        spinnerTexture.dispose();
        stage.dispose();
    }

    public void setScreenChangeAllowed(boolean _screenChangeAllowed) {
        screenChangeAllowed = _screenChangeAllowed;
    }
}
