package com.example.rafaelanastacioalves.desafioandroid.application;

import android.app.Application;
import android.util.Log;

import com.example.rafaelanastacioalves.desafioandroid.BuildConfig;
import com.orhanobut.hawk.Hawk;
import com.squareup.picasso.Picasso;

import timber.log.Timber;

/**
 * Created by rafaelanastacioalves on 5/20/17.
 */

public class MainApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
            Picasso.with(getApplicationContext())
                    .setIndicatorsEnabled(true);
            Picasso.with(getApplicationContext())
                    .setLoggingEnabled(true);
        } else {
            Timber.plant(new CrashReportingTree());
        }

        Hawk.init(this).build();
    }

    /**
     * A tree which logs important information for crash reporting.
     */
    private static class CrashReportingTree extends Timber.Tree {
        @Override
        protected void log(int priority, String tag, String message, Throwable t) {
            if (priority == Log.VERBOSE || priority == Log.DEBUG) {
                return;
            }

            FakeCrashLibrary.log(priority, tag, message);

            if (t != null) {
                if (priority == Log.ERROR) {
                    FakeCrashLibrary.logError(t);
                } else if (priority == Log.WARN) {
                    FakeCrashLibrary.logWarning(t);
                }
            }
        }
    }
}
