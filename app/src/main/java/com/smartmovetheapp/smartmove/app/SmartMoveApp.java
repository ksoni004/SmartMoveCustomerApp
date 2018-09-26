package com.smartmovetheapp.smartmove.app;

import android.app.Application;

import com.google.firebase.messaging.FirebaseMessaging;
import com.smartmovetheapp.smartmove.data.sharedpref.SharedPrefs;

public class SmartMoveApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        SharedPrefs.initialize(this);
        FirebaseMessaging.getInstance().subscribeToTopic("customer");
    }
}
