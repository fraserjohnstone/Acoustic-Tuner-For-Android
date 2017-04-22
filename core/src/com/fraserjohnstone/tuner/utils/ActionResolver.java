package com.fraserjohnstone.tuner.utils;

/**
 * This interface allows the libGDX application to communicate directly with the android system,
 * in this case to request runtime user permissions.
 *
 * @version 1.01 - 22.04.2017
 * @author Fraser Johnstone
 */

import com.fraserjohnstone.tuner.screens.SplashScreen;

public interface ActionResolver {
    void requestPermissions(SplashScreen _splashScreen);
}
