package com.fraserjohnstone.tuner;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.TextView;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.fraserjohnstone.tuner.screens.SplashScreen;

/**
 * This class is responsible for requesting the runtime permission to record audio from the user as
 * well as initialising the application for the android platform.
 */
public class AndroidLauncher extends AndroidApplication {

    //permissions
    static final int PERMISSION_REQUEST_RECORD_AUDIO = 1;

    //reference to the splash screen
    SplashScreen mSplashScreen;

    //reference to this instance
    private AndroidLauncher mRefToSelf;

    /**
     * Configures and initialises the application
     *
     * @param _savedInstanceState
     */
	@Override
	protected void onCreate (Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		ActionResolverAndroid actionResolver = new ActionResolverAndroid(this);
        mRefToSelf = this;

		initialize(new Tuner(actionResolver), config);
	}

    /**
     * Prompts for the users permission to record audio. This is mandatory for this application as
     * none of the features would work otherwise.
     *
     * @param _splashScreen
     */
    public void requestPermissions(SplashScreen _splashScreen){
        mSplashScreen = _splashScreen;
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {

            //let the user know that we need this permission for the application to function
            String text = "For the tuner to function you need to grant audio permissions.";
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            TextView msg = new TextView(this);
            msg.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            msg.setPadding(25, 25 , 25, 0);
            msg.setText(text);
            msg.setGravity(Gravity.CENTER_HORIZONTAL);
            builder.setView(msg);
            builder.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    ActivityCompat.requestPermissions(
                            mRefToSelf,
                            new String[]{android.Manifest.permission.RECORD_AUDIO},
                            PERMISSION_REQUEST_RECORD_AUDIO);
                }
            });
            builder.setNegativeButton("Exit Application", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    startActivity(intent);
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        }else{
            mSplashScreen.setScreenChangeAllowed(true);
        }
    }

    /**
     * Handles the result from the permission request.
     *
     * @param _requestCode
     * @param _permissions
     * @param _grantResults
     */
    @Override
    public void onRequestPermissionsResult(int _requestCode,
                                           String _permissions[], int[] _grantResults) {
        switch (_requestCode) {
            case PERMISSION_REQUEST_RECORD_AUDIO: {
                // If request is cancelled, the result arrays are empty.
                if (_grantResults.length > 0
                        && _grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //permissions have been granted so allow the change of screen
                    mSplashScreen.setScreenChangeAllowed(true);
                }else if(_grantResults.length > 0
                        && _grantResults[0] == PackageManager.PERMISSION_DENIED){
                    //permissions have not been granted
                }
            }
        }
    }
}
