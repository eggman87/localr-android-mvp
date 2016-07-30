package com.eggman.localr.ui.login;

import com.eggman.localr.session.Session;
import com.eggman.localr.ui.BasePresenter;
import com.github.scribejava.core.model.OAuth1AccessToken;

import javax.inject.Inject;

/**
 * Created by mharris on 7/29/16.
 * DispatchHealth.
 */
public class LoginPresenter extends BasePresenter<LoginView> {

    private OauthInteractor authInteractor;
    private Session session;

    @Inject
    public LoginPresenter(OauthInteractor authInteractor, Session session) {
        super(LoginView.class);

        this.authInteractor = authInteractor;
        this.session = session;
    }

    public void startAuthenticationProcess() {
        authInteractor.getAuthorizationUrl()
                .subscribe(view::displayAuthorizationUrlToUser, this::onError);
    }

    public void getAccessTokenForVerifier(String verifierUrl) {
        authInteractor.getAccessTokenFromVerifier(verifierUrl)
                .subscribe(this::onAccessTokenGranted, this::onError);
    }

    private void onAccessTokenGranted(OAuth1AccessToken token) {
        session.setAccessToken(token.getToken(), token.getTokenSecret());

        view.navigateToHome();
    }

    private void onError(Throwable throwable) {
        view.displayErrorToUser("Failure trying to authorize localr" + throwable.getMessage());
    }
}
