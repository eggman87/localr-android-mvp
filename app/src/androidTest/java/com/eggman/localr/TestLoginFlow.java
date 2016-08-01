package com.eggman.localr;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.eggman.localr.ui.login.LoginActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by mharris on 7/31/16.
 * eggmanapps.
 */
@RunWith(AndroidJUnit4.class)
public class TestLoginFlow {

    @Rule
    public ActivityTestRule<LoginActivity> activityRule = new ActivityTestRule<>(LoginActivity.class);

    @Test
    public void testClickLogin() {
        onView(withText("LOGIN TO FLICKR")).perform(click());
    }

}
