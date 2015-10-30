package com.example.bursny.talent;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.parse.Parse;
import com.parse.ParseFacebookUtils;

/**
 * Created by Lukas on 15-10-21.
 * Application class
 */
public class shine extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "zHCz2gexEqae4Dk3Vt7MiUpL8LQProT6FFkKd2JX", "eXDxOIUMPoBDAyNSyi74HjOn8zP862jIvSCO8auj");
        FacebookSdk.sdkInitialize(this);
        ParseFacebookUtils.initialize(this);
    }
}
