package net.rhari;

import android.app.Application;

import timber.log.Timber;

public class ECommApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Timber.plant(new Timber.DebugTree());
    }
}
