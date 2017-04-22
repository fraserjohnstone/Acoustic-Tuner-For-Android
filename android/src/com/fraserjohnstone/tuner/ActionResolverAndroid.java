package com.fraserjohnstone.tuner;

import android.os.Handler;

import com.fraserjohnstone.tuner.screens.SplashScreen;
import com.fraserjohnstone.tuner.utils.ActionResolver;

/**
 * This class which implements {@link ActionResolver} allows us to communicate with the Android
 * system from the libGDX framework.
 *
 * @version 1.01 - 22.04.2017
 * @author  Fraser Johnstone
 */
public class ActionResolverAndroid implements ActionResolver {
	private Handler uiThread;
	private AndroidLauncher androidLauncher;

	/**
	 * Class constructor
	 *
	 * @param _androidLauncher {@link AndroidLauncher}
	 */
	ActionResolverAndroid(AndroidLauncher _androidLauncher) {
    	uiThread = new Handler();
		androidLauncher = _androidLauncher;
    }

    /**
     * Prompts the users express permission to access location information if needed.
	 *
	 * @param _splashScreen Instance of {@link SplashScreen}
     */
	@Override
	public void requestPermissions(final SplashScreen _splashScreen) {
		uiThread.post(new Runnable(){
			@Override
			public void run() {
				androidLauncher.requestPermissions(_splashScreen);
			}
		});
	}
}
