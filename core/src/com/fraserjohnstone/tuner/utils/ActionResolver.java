package com.fraserjohnstone.tuner.utils;


import com.fraserjohnstone.tuner.screens.SplashScreen;

/**
 * This interface allows the libGDX application to communicate directly with the android system,
 * in this case to request runtime user permissions.
 */
public interface ActionResolver {
    void requestPermissions(SplashScreen _splashScreen);
}
