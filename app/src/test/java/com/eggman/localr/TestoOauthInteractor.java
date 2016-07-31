package com.eggman.localr;

import com.eggman.localr.interactor.OauthInteractor;
import com.eggman.localr.utils.RxScheduler;
import com.github.scribejava.core.model.OAuth1RequestToken;
import com.github.scribejava.core.oauth.OAuth10aService;

import org.junit.Rule;
import org.junit.Test;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;

import java.io.IOException;
import java.util.List;

import rx.observers.TestSubscriber;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

/**
 * Created by mharris on 7/30/16.
 * DispatchHealth.
 */
@PowerMockIgnore({"org.mockito.*", "org.robolectric.*", "android.*"})
@PrepareForTest({OAuth10aService.class})
public class TestoOauthInteractor {

    @Rule
    public PowerMockRule rule = new PowerMockRule();

    @Test
    public void testGetAuthorizationUrl() throws IOException {
        //given
        OAuth10aService authService = PowerMockito.mock(OAuth10aService.class);
        RxScheduler rxScheduler = new UnitTestRxScheduler();

        OauthInteractor interactor = new OauthInteractor(authService, rxScheduler);
        OAuth1RequestToken token = new OAuth1RequestToken("baba", "booey");

        when(authService.getRequestToken()).thenReturn(token);
        when(authService.getAuthorizationUrl(any(OAuth1RequestToken.class))).thenReturn("http://spacejam.com");

        TestSubscriber<String> testSubscriber = new TestSubscriber<>();
        //when
        interactor.getAuthorizationUrl().subscribe(testSubscriber);

        //then
        testSubscriber.assertNoErrors();
        testSubscriber.assertCompleted();

        List<String> urls = testSubscriber.getOnNextEvents();

        assertNotNull(urls);
        assertEquals("url emissions not right", 1, urls.size());
        assertEquals("url was not right", "http://spacejam.com&perms=read", urls.get(0));
    }

    @Test
    public void testGetAuthorizationUrlBadNetwork() {

    }
}
