package com.eggman.localr.session;

import android.content.Context;

/**
 * Created by mharris on 7/29/16.
 * .
 */
public interface Session {

    boolean isAuthenticated();

    void setAccessToken(String accessToken, String accessTokenSecret);

    String getAccessToken();
    String getAccessTokenSecret();

    /**
     * Tell the session to persist itself if it needs to.
     */
    void shouldPersistIfNecessary(Context context);
}
