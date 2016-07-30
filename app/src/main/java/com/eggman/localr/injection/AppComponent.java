package com.eggman.localr.injection;

import com.eggman.localr.ui.BaseActivity;
import com.eggman.localr.ui.splash.SplashActivity;
import com.eggman.localr.ui.home.HomeActivity;
import com.eggman.localr.ui.login.LoginActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by mharris on 7/29/16.
 * .
 */

@Component(modules = { AppModule.class })
@Singleton
public interface AppComponent {

    void inject(BaseActivity a);
    void inject(SplashActivity a);
    void inject(LoginActivity a);
    void inject(HomeActivity a);
}
