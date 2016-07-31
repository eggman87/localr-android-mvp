package com.eggman.localr.ui.splash;

import com.eggman.localr.interactor.UserInteractor;
import com.eggman.localr.model.User;
import com.eggman.localr.session.Session;
import com.eggman.localr.ui.BasePresenter;

import javax.inject.Inject;

/**
 * Created by mharris on 7/29/16.
 * .
 */

public class SplashPresenter extends BasePresenter<SplashView> {

    private Session session;
    private UserInteractor userInteractor;

    @Inject
    public SplashPresenter(UserInteractor userInteractor, Session session) {
        super(SplashView.class);

        this.userInteractor = userInteractor;
        this.session = session;
    }

    public void checkLogin() {
        if (session.isAuthenticated()) {
            this.userInteractor.validateUserStillAuthenticated()
                    .subscribe(this::onUserIsStillAuthenticated, this::onAuthenticationFailure);
        } else  {
            view.navigateToLogin();
        }
    }

    private void onUserIsStillAuthenticated(User user) {
        view.displayWelcomeBack(user.userName.name);
        view.navigateToHome();
    }

    private void onAuthenticationFailure(Throwable tr) {
        //todo: verify throwable actually indicates bad token and not just network issue....
        session.setAccessToken(null, null);
        view.navigateToLogin();
    }
}
