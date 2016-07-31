package com.eggman.localr;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Settings;

import com.eggman.localr.session.JsonSharedPrefsSession;
import com.scottyab.aescrypt.AESCrypt;

import org.junit.Rule;
import org.junit.Test;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;

import java.security.GeneralSecurityException;

import static org.mockito.Mockito.*;

/**
 * Created by mharris on 7/30/16.
 * DispatchHealth.
 */
@PrepareForTest({Settings.Secure.class, AESCrypt.class})
public class JsonSharedPreferencesTest {

    @Rule
    public PowerMockRule rule = new PowerMockRule();

    @SuppressLint("CommitPrefEdits")
    @Test
    public void testSetAuthToken() throws GeneralSecurityException {
        PowerMockito.mockStatic(Settings.Secure.class, Object.class);
        PowerMockito.mockStatic(AESCrypt.class, Object.class);
        Context context = mock(Context.class);
        SharedPreferences preferences = mock(SharedPreferences.class);
        SharedPreferences.Editor editor = mock(SharedPreferences.Editor.class);
        ContentResolver resolver = mock(ContentResolver.class);

        when(context.getContentResolver()).thenReturn(resolver);

        String stateJson = "{\"accessToken\":\"accessToken\",\"accessTokenSecret\":\"accessTokenSecret\"}";
        String deviceId = "my_device_id";

        when(Settings.Secure.getString(resolver, "android_id")).thenReturn(deviceId);
        when(AESCrypt.encrypt(deviceId, stateJson)).thenReturn("encrypted_json");

        when(context.getSharedPreferences(anyString(), anyInt())).thenReturn(preferences);
        when(preferences.getString("session", "")).thenReturn("");
        when(preferences.edit()).thenReturn(editor);
        when(preferences.edit().putString(anyString(), anyString())).thenReturn(editor);

        JsonSharedPrefsSession sut = new JsonSharedPrefsSession(context);
        sut.setAccessToken("accessToken", "accessTokenSecret");
        sut.shouldPersistIfNecessary(context);

        verify(editor).putString("session", "encrypted_json");
    }
}
