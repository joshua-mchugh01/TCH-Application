package com.redrumming.thecreaturehub.activities.splash;

import android.content.Context;

/**
 * Created by ME on 1/2/2016.
 */
public interface SplashScreenView {

    void displayNextActivity();
    void displayFailureAlert();
    Context getContext();
}
