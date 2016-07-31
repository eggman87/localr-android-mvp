package com.eggman.localr.analytics;

import android.content.Context;

import com.eggman.localr.Config;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

/**
 * Created by mharris on 7/29/16.
 * .
 */

public class GoogleAnalytics implements Analytics {

    private Tracker tracker;

    public GoogleAnalytics(Context context) {
        this.tracker = com.google.android.gms.analytics.GoogleAnalytics
                .getInstance(context).newTracker(Config.GA_TRACKING_ID);
    }

    @Override
    public void trackScreen(String screenName) {
        tracker.setScreenName(screenName);
        tracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    public void trackButtonPress(String action) {
        tracker.send(new HitBuilders.EventBuilder()
                .setCategory("Button")
                .setAction(action)
                .build());

    }
}
