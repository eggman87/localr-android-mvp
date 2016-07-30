package com.eggman.localr.ui.splash;

import com.eggman.localr.session.Session;
import com.eggman.localr.ui.BasePresenter;

import javax.inject.Inject;

/**
 * Created by mharris on 7/29/16.
 * .
 */

public class SplashPresenter extends BasePresenter<SplashView> {

    private Session session;

    @Inject
    public SplashPresenter(Session session) {
        super(SplashView.class);

        this.session = session;
    }

    public void checkLogin() {
        if (session.isAuthenticated()) {
            view.displayWelcomeBack();
            view.navigateToHome();
        } else  {
            view.navigateToLogin();
        }
    }
}
