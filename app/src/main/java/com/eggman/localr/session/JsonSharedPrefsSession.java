package com.eggman.localr.session;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.util.Log;

import com.google.gson.Gson;
import com.scottyab.aescrypt.AESCrypt;

import static android.content.ContentValues.TAG;

/**
 * Created by mharris on 7/29/16.
 *
 * Manages state for the current session. Will persist to shared preferences as an encrypted json
 * string when necessary. This is a good store for simple data that has no need to be queried heavily.
 * For that kind of data, look at an ORM such as Realm or a standard database layer like SQLite. Keep
 * in mind that even though this data is encrypted, someone can still reverse engineer the data if they
 * spend enough time (root device, decompile apk, figure out how we are encrypting) - so I do not
 * recommend storing any kind of sensitive data here.
 *
 */
public class JsonSharedPrefsSession implements Session {

    private SessionState state;
    private boolean pendingState;

    private static final String PREFS_KEY = "prefs";
    private static final String SESSION_KEY = "session";

    public JsonSharedPrefsSession(Context context) {
        restoreState(context);
    }

    @Override
    public boolean isAuthenticated() {
        return this.state.accessToken != null && this.state.accessTokenSecret != null;
    }

    @Override
    public void setAccessToken(String accessToken, String accessTokenSecret) {
        this.state.accessToken = accessToken;
        this.state.accessTokenSecret = accessTokenSecret;
        this.pendingState = true;
    }

    @Override
    public String getAccessToken() {
        return this.state.accessToken;
    }

    @Override
    public String getAccessTokenSecret() {
        return this.state.accessTokenSecret;
    }

    @Override
    public void shouldPersistIfNecessary(Context context) {
        if (pendingState) {
            persistState(context);
            pendingState = false;
        }
    }

    private void restoreState(Context context) {
        try {
            SharedPreferences preferences = context.getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE);
            String deviceId = getDeviceId(context);

            String encryptedJson = preferences.getString(SESSION_KEY, "");
            String sessionJson = AESCrypt.decrypt(deviceId, encryptedJson);
            state = new Gson().fromJson(sessionJson, SessionState.class);

            if (state == null) {
                state = new SessionState();
            }
        } catch (Exception e) {
            Log.e(TAG, "something went wrong while restoring session state, resetting state.", e);
            state = new SessionState();
        }
    }

    private void persistState(Context context) {
        try {
            SharedPreferences preferences = context.getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE);
            String deviceId = getDeviceId(context);

            String sessionJson = new Gson().toJson(state, SessionState.class);
            String encryptedJson = AESCrypt.encrypt(deviceId, sessionJson);

            preferences.edit().putString(SESSION_KEY, encryptedJson).apply();
        } catch (Exception e) {
            Log.e(TAG, "something went wrong while saving session state", e);
        }
    }

    private String getDeviceId(Context context) {
        @SuppressLint("HardwareIds")
        String deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        //device id has been reported by some to be null on certain devices, use a compiled in key in these cases.
        if (deviceId == null) {
            deviceId = "DefaultAndroid";
        }
        return deviceId;
    }
}
