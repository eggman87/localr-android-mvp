package com.eggman.localr.ui.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.eggman.localr.R;
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

        injector().inject(this);
        presenter.attach(this);

        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(this::startProcessing, 500);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        presenter.detach();
    }

    @Override
    public void displayWelcomeBack() {
        Toast.makeText(this, "Welcome Back!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void navigateToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public void navigateToHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    private void startProcessing() {
        presenter.checkLogin();
    }
}