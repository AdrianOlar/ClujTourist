package ro.adrian.tourist;

import android.app.Application;
import android.content.Context;

/**
 * Created by Adrian Olar on 13/04/2014.
 * Licence Thesis Project
 */
public class ClujTouristApplication extends Application {

    private static Context appContext;

    public static Context getAppContext() {
        return appContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = getApplicationContext();
    }
}
