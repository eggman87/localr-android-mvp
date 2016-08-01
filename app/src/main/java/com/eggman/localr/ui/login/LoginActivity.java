package com.eggman.localr.ui.login;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.eggman.localr.R;
import com.eggman.localr.analytics.Screens;
import com.eggman.localr.ui.BaseActivity;
import com.eggman.localr.ui.home.HomeActivity;

import javax.inject.Inject;

/**
 * Created by mharris on 7/29/16.
 * eggmanapps.
 */
public class LoginActivity extends BaseActivity implements LoginView {

    @Inject
    protected LoginPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        injector().inject(this);
        presenter.attach(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        presenter.detach();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if (intent.getAction().equals(Intent.ACTION_VIEW)) {
            Toast.makeText(this, R.string.login_received, Toast.LENGTH_SHORT).show();
            presenter.getAccessTokenForVerifier(intent.getData().toString());
        }
    }

    @Override
    public void displayAuthorizationUrlToUser(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }

    @Override
    public void navigateToHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void displayErrorToUser(String error) {
        String errorMessage = String.format(getString(R.string.authorization_failure_format), error);
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }

    public void loginClicked(View view) {
        presenter.startAuthenticationProcess();
    }

    @Override
    protected String getScreenName() {
        return Screens.LOGIN;
    }
}
