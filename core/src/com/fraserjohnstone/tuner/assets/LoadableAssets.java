package com.fraserjohnstone.tuner.assets;

/**
 * This class holds strings of the locations of any assets used throughout the application. These
 * are loaded by the {@link com.fraserjohnstone.tuner.Tuner} instance while the splash screen is
 * displayed.
 *
 * @author Fraser Johnstone
 * @version 1.01 - 22.04.2017
 */
public class LoadableAssets {
    public static String[] assetArray = new String[]{
        //bg
        "images/bg/white_pixel.png",

        //note wheel
        "images/note_wheel_sharps.png",
        "images/hertz_readout_bg.png",
        "images/cents_sharp_or_flat_center.png",
        "images/how_flat_cents.png",
        "images/how_sharp_cents.png",
        "images/flat_symbol.png",
        "images/sharp_symbol.png",
        "images/cents_sharp_or_flat_center.png",
        "images/cents_sharp_or_flat_center_green.png"
    };
    public static String[] bitmapFontAssetArray = new String[]{
        "ui/fonts/candara_black.fnt",
        "ui/fonts/candara_white.fnt"
    };
}
