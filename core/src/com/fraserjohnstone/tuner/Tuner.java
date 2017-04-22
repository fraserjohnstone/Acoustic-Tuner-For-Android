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
 * This is the main {@link Game} instance and entry point for the application. It will create
 * and initialise the {@link SpriteBatch} and {@link AssetManager} used throughout, and then set
 * the screen to an instance of {@link com.fraserjohnstone.tuner.screens.SplashScreen} to be displayed
 * while the application assets load into {@link #mAssetManager}.
 *
 * @version 1.01 - 22.04.2017
 * @author  Fraser Johnstone
 */
public class Tuner extends Game {

	public int mScreenHeightPixels;
	public int mScreenWidthPixels;

	public SpriteBatch mBatch;
	public AssetManager mAssetManager;

	public ActionResolver mActionResolver;

	/**
	 * Class constructor.
	 * <p>
	 * Sets up all the objects required throughout the Application and displays the splash screen.
	 */
	public Tuner(ActionResolver _actionResolver){
		mActionResolver = _actionResolver;
	}

	/**
	 * Loads any assets, creates a sprite batch used throughout the application and sets an instance
     * of {@link com.fraserjohnstone.tuner.screens.SplashScreen} as the current screen
	 */
	@Override
	public void create () {
		//get screen dimensions
		mScreenHeightPixels = Gdx.graphics.getHeight();
		mScreenWidthPixels = Gdx.graphics.getWidth();

        //load assets
		loadAssets();

		//create SpriteBatch
		mBatch = new SpriteBatch();

        //set screen to splash screen
		setScreen(new com.fraserjohnstone.tuner.screens.SplashScreen(this));
	}

	/**
	 * Loads all of the assets into {@link #mAssetManager}.
	 */
	private void loadAssets(){
		//create the parameters for image assets
		TextureParameter param = new TextureParameter();
		param.genMipMaps = true;
		param.minFilter = TextureFilter.MipMapLinearNearest;
		param.magFilter = TextureFilter.Nearest;

        //load all images into the AssetManager
		mAssetManager = new AssetManager();

        //images
		for(int i = 0; i < com.fraserjohnstone.tuner.assets.LoadableAssets.assetArray.length; i++){
			mAssetManager.load(com.fraserjohnstone.tuner.assets.LoadableAssets.assetArray[i], Texture.class, param);
		}

        //bitmap fonts
        for(int i = 0; i< com.fraserjohnstone.tuner.assets.LoadableAssets.bitmapFontAssetArray.length; i++){
            mAssetManager.load(com.fraserjohnstone.tuner.assets.LoadableAssets.bitmapFontAssetArray[i], BitmapFont.class);
        }
	}

	@Override
	public void render(){
		super.render();
	}

    /**
     * This method safely disposes of any disposable objects (see {@link com.badlogic.gdx.utils.Disposable})
     * created in this class.
     */
	@Override
	public void dispose () {
		mBatch.dispose();
		if(mAssetManager != null){
			mAssetManager.dispose();
		}
	}
}
