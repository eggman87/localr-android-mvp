package com.eggman.localr.ui.login;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.eggman.localr.ui.BaseActivity;
import com.eggman.localr.ui.home.HomeActivity;

import javax.inject.Inject;

/**
 * Created by mharris on 7/29/16.
 * DispatchHealth.
 */
public class LoginActivity extends BaseActivity implements LoginView {

    @Inject
    protected LoginPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        injector().inject(this);
        presenter.attach(this);
        presenter.startAuthenticationProcess();
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
    }

    @Override
    public void displayErrorToUser(String error) {
        //todo: show dialog, with retry logic.
    }
}
