package com.eggman.localr.ui.login;

import android.net.Uri;

import com.eggman.localr.utils.RxScheduler;
import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.model.OAuth1RequestToken;
import com.github.scribejava.core.oauth.OAuth10aService;

import javax.inject.Inject;

import rx.Observable;
import timber.log.Timber;

/**
 * Created by mharris on 7/29/16.
 * DispatchHealth.
 */
public class OauthInteractor {

    private OAuth10aService authService;
    private RxScheduler scheduler;

    private OAuth1RequestToken requestToken;

    @Inject
    public OauthInteractor(OAuth10aService authService, RxScheduler scheduler) {
        this.authService = authService;
        this.scheduler = scheduler;
    }

    public Observable<String> getAuthorizationUrl() {
        return Observable.create((Observable.OnSubscribe<String>) subscriber -> {
                try {
                    requestToken = authService.getRequestToken();
                    subscriber.onNext(authService.getAuthorizationUrl(requestToken) + "&perms=read");
                } catch (Exception e) {
                    Timber.e("getting authorization url failed", e);
                    subscriber.onError(e);
                } finally {
                    subscriber.onCompleted();
                }
            }).compose(scheduler.applySchedulers());
    }

    public Observable<OAuth1AccessToken> getAccessTokenFromVerifier(String verifierUrl) {
        String verifier = Uri.parse(verifierUrl).getQueryParameter("oauth_verifier");

        return Observable.create((Observable.OnSubscribe<OAuth1AccessToken>) subscriber -> {
            try {
                if (requestToken == null) {
                    throw new IllegalStateException("request token not present, you must call getAuthorizationUrl first.");
                }
                subscriber.onNext(authService.getAccessToken(requestToken, verifier));
            } catch (Exception e) {
                Timber.e("getting access token failed", e);
                subscriber.onError(e);
            } finally {
                subscriber.onCompleted();
            }
        }).compose(scheduler.applySchedulers());
    }
}
