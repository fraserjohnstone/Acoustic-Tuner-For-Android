package com.fraserjohnstone.tuner;

import android.os.Handler;

import com.fraserjohnstone.tuner.screens.SplashScreen;
import com.fraserjohnstone.tuner.utils.ActionResolver;

/**
 * This class which implements {@link ActionResolver} allows us to communicate with the Android
 * system from the libGDX framework.
 */
public class ActionResolverAndroid implements ActionResolver {
	private Handler mUiThread;
	private AndroidLauncher mAndroidLauncher;

	/**
	 * Class constructor
	 *
	 * @param _androidLauncher {@link AndroidLauncher}
	 */
	ActionResolverAndroid(AndroidLauncher _androidLauncher) {
    	mUiThread = new Handler();
		mAndroidLauncher = _androidLauncher;
    }

    /**
     * Prompts the users express permission to access location information if needed.
	 *
	 * @param _splashScreen Instance of {@link SplashScreen}
     */
	@Override
	public void requestPermissions(final SplashScreen _splashScreen) {
		mUiThread.post(new Runnable(){
			@Override
			public void run() {
				mAndroidLauncher.requestPermissions(_splashScreen);
			}
		});
	}
}
