package com.eggman.localr.ui;

import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.eggman.localr.LocalApplication;
import com.eggman.localr.analytics.Analytics;
import com.eggman.localr.injection.AppComponent;
import com.eggman.localr.session.Session;
import com.eggman.localr.utils.RxBus;

import javax.inject.Inject;

import icepick.Icepick;
import rx.Subscription;

/**
 * Created by mharris on 7/29/16.
 *
 * Base activity that will restore any members annotated with @State using Icepick. Will also persist
 * session state in onStop when the session has data that needs to be saved.
 */

public abstract class BaseActivity extends AppCompatActivity {

    @Inject
    protected Session session;
    @Inject
    protected RxBus bus;
    @Inject
    protected Analytics analytics;

    private Subscription eventSubscription;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        injector().inject(this);

        Icepick.restoreInstanceState(this, savedInstanceState);
        analytics.trackScreen(getScreenName());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Icepick.saveInstanceState(this, outState);
    }

    @Override
    protected void onStart() {
        super.onStart();

        eventSubscription = bus.toObserverable().subscribe(this::handleEvent);
    }

    @Override
    protected void onStop() {
        super.onStop();

        eventSubscription.unsubscribe();
        session.shouldPersistIfNecessary(this);
    }

    protected int color(@ColorRes int color) {
        return ContextCompat.getColor(this, color);
    }

    protected void handleEvent(Object object) {

    }

    protected AppComponent injector() {
        return app().component();
    }

    protected LocalApplication app() {
        return (LocalApplication) getApplication();
    }

    protected abstract String getScreenName();
}
