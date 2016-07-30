package com.eggman.localr.ui.login;

/**
 * Created by mharris on 7/29/16.
 * DispatchHealth.
 */
public interface LoginView {

    void displayAuthorizationUrlToUser(String url);

    void navigateToHome();

    void displayErrorToUser(String error);

}
