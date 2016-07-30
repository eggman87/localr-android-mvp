package com.eggman.localr.analytics;

/**
 * Created by mharris on 7/29/16.
 * .
 */

public interface Analytics {

    void trackScreen(String screenName);

    void trackButtonPress(String action);
}
