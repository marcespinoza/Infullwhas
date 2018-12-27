package com.full.wasah.Util;

import android.app.Application;
import android.content.Context;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;

public class ReservaApplication extends Application {

    private static Context context;

    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        ReservaApplication.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return ReservaApplication.context;
    }
}
