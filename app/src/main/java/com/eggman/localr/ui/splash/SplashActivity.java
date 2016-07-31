package com.eggman.localr.ui.splash;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.eggman.localr.R;
import com.eggman.localr.analytics.Screens;
import com.eggman.localr.ui.BaseActivity;
import com.eggman.localr.ui.home.HomeActivity;
import com.eggman.localr.ui.login.LoginActivity;

import javax.inject.Inject;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class SplashActivity extends BaseActivity implements SplashView {

    @Inject
    protected SplashPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        injector().inject(this);
        presenter.attach(this);

        startProcessing();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        presenter.detach();
    }

    @Override
    public void displayWelcomeBack(String name) {
        Toast.makeText(this, String.format(getString(R.string.welcome_back_format), name), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void navigateToLogin() {
        finish();

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public void navigateToHome() {
        finish();

        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    private void startProcessing() {
        presenter.checkLogin();
    }

    @Override
    protected String getScreenName() {
        return Screens.SPLASH;
    }
}