package com.eggman.localr;

import android.app.Application;

import com.eggman.localr.injection.AppComponent;
import com.eggman.localr.injection.AppModule;
import com.eggman.localr.injection.DaggerAppComponent;

import timber.log.Timber;

/**
 * Created by mharris on 7/29/16.
 * .
 */
public class LocalApplication extends Application {

    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        this.appComponent = DaggerAppComponent.builder().appModule(new AppModule(this)).build();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }

    public AppComponent component() {
        return this.appComponent;
    }
}
