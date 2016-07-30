package com.eggman.localr.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.eggman.localr.LocalApplication;
import com.eggman.localr.injection.AppComponent;
import com.eggman.localr.session.Session;

import javax.inject.Inject;

import icepick.Icepick;

/**
 * Created by mharris on 7/29/16.
 *
 * Base activity that will restore any members annotated with @State using Icepick. Will also persist
 * session state in onStop when the session has data that needs to be saved.
 */

public class BaseActivity extends AppCompatActivity {

    @Inject
    protected Session session;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        injector().inject(this);

        Icepick.restoreInstanceState(this, savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Icepick.saveInstanceState(this, outState);
    }

    @Override
    protected void onStop() {
        super.onStop();

        session.shouldPersistIfNecessary(this);
    }

    protected AppComponent injector() {
        return app().component();
    }

    protected LocalApplication app() {
        return (LocalApplication) getApplication();
    }
}
