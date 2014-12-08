package de.sitewaerts.cleverdox.viewer;

import android.app.Activity;
import android.os.Bundle;

/**
 * this activity is launched if the app is NOT started from cordova/phonegap
 * @author Philipp Bohnenstengel (raumobil GmbH)
 *
 */
public final class SplashScreenActivity
        extends Activity
{
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
    }
}
