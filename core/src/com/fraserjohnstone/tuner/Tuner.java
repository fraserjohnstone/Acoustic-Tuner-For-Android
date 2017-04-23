package com.fraserjohnstone.tuner;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.assets.loaders.TextureLoader.TextureParameter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.fraserjohnstone.tuner.utils.ActionResolver;

/**
 * This is the main {@link Game} class and entry point for the application. It will create
 * and initialise the {@link SpriteBatch} and {@link AssetManager} used throughout, and then set
 * the screen to an instance of {@link com.fraserjohnstone.tuner.screens.SplashScreen} to be displayed
 * while the application assets load into {@link #mAssetManager}.
 *
 * @author Fraser Johnstone
 * @version 1.01 - 22.04.2017
 */
public class Tuner extends Game {

    private int mScreenHeightPixels;
    private int mScreenWidthPixels;
    private SpriteBatch mBatch;
    private AssetManager mAssetManager;
    private ActionResolver mActionResolver;

    /**
     * Class constructor
     */
    Tuner(ActionResolver _actionResolver) {
        mActionResolver = _actionResolver;
    }

    /**
     * Loads any assets, creates a sprite batch used throughout the application and sets an instance
     * of {@link com.fraserjohnstone.tuner.screens.SplashScreen} as the current screen
     */
    @Override
    public void create() {
        //get screen dimensions
        mScreenHeightPixels = Gdx.graphics.getHeight();
        mScreenWidthPixels = Gdx.graphics.getWidth();

        loadAssets();

        mBatch = new SpriteBatch();

        setScreen(new com.fraserjohnstone.tuner.screens.SplashScreen(this));
    }

    /**
     * Loads all of the assets into {@link #mAssetManager}.
     */
    private void loadAssets() {
        //create the parameters for image assets
        TextureParameter param = new TextureParameter();
        param.genMipMaps = true;
        param.minFilter = TextureFilter.MipMapLinearNearest;
        param.magFilter = TextureFilter.Nearest;

        mAssetManager = new AssetManager();

        //images
        for (int i = 0; i < com.fraserjohnstone.tuner.assets.LoadableAssets.assetArray.length; i++) {
            mAssetManager.load(com.fraserjohnstone.tuner.assets.LoadableAssets.assetArray[i], Texture.class, param);
        }

        //bitmap fonts
        for (int i = 0; i < com.fraserjohnstone.tuner.assets.LoadableAssets.bitmapFontAssetArray.length; i++) {
            mAssetManager.load(com.fraserjohnstone.tuner.assets.LoadableAssets.bitmapFontAssetArray[i], BitmapFont.class);
        }
    }

    /**
     * Safely disposes of any disposable objects (see {@link com.badlogic.gdx.utils.Disposable})
     * created in this class.
     */
    @Override
    public void dispose() {
        mBatch.dispose();
        if (mAssetManager != null) {
            mAssetManager.dispose();
        }
    }

    /**
     * Getters and Setters
     */

    public int getScreenHeightPix(){
        return mScreenHeightPixels;
    }

    public int getScreenWidthPix(){
        return mScreenWidthPixels;
    }

    public SpriteBatch getSpriteBatch(){
        return mBatch;
    }

    public ActionResolver getActionResolver(){
        return mActionResolver;
    }

    public AssetManager getAssetManager(){
        return mAssetManager;
    }
}
