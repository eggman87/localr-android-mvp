package com.eggman.localr.session;

import android.content.Context;
import android.support.annotation.UiThread;

/**
 * Created by mharris on 7/29/16.
 * .
 */
public interface Session {

    @UiThread
    boolean isAuthenticated();
    @UiThread
    void setAccessToken(String accessToken, String accessTokenSecret);
    @UiThread
    String getAccessToken();
    @UiThread
    String getAccessTokenSecret();

    /**
     * Tell the session to persist itself if it needs to.
     */
    void shouldPersistIfNecessary(Context context);
}
