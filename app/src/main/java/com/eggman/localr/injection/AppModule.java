package com.eggman.localr.injection;

import android.content.Context;

import com.eggman.localr.BuildConfig;
import com.eggman.localr.Config;
import com.eggman.localr.LocalApplication;
import com.eggman.localr.service.FlickrApi;
import com.eggman.localr.service.Oauth1SigningInterceptor;
import com.eggman.localr.session.JsonSharedPrefsSession;
import com.eggman.localr.session.Session;
import com.eggman.localr.utils.RxAndroidScheduler;
import com.eggman.localr.utils.RxScheduler;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.oauth.OAuth10aService;
import com.squareup.picasso.Picasso;

import java.util.Random;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import pl.charmas.android.reactivelocation.ReactiveLocationProvider;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

/**
 * Created by mharris on 7/29/16.
 * .
 */
@Module
public class AppModule {

    private LocalApplication app;

    //scope objects to flickr if they have flickr specific configuration.
    private static final String DEPENDENCY_NAME_FLICKR = "flickr";

    public AppModule(LocalApplication application) {
        this.app = application;
    }

    @Provides
    public Context context() {
        return app;
    }

    @Provides
    @Named(DEPENDENCY_NAME_FLICKR)
    public Oauth1SigningInterceptor oauthSigningInterceptor(@Named(DEPENDENCY_NAME_FLICKR) Oauth1SigningInterceptor.TokenProvider tokenProvider) {
        return new Oauth1SigningInterceptor.Builder()
                .consumerKey(Config.FLICKR_CONSUMER_KEY)
                .consumerSecret(Config.FLICKR_CONSUMER_SECRET)
                .random(new Random(1000))
                .clock(new Oauth1SigningInterceptor.Clock())
                .tokenProvider(tokenProvider)
                .build();
    }

    @Provides
    public HttpLoggingInterceptor loggingInterceptor() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(message -> Timber.d(message));

        if (BuildConfig.DEBUG) {
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        }
        return interceptor;
    }

    @Provides
    @Named(DEPENDENCY_NAME_FLICKR)
    public OkHttpClient httpClient(@Named(DEPENDENCY_NAME_FLICKR) Oauth1SigningInterceptor authInterceptor, HttpLoggingInterceptor logInterceptor) {
        return new OkHttpClient.Builder()
                .addInterceptor(authInterceptor)
                .addInterceptor(logInterceptor)
                .build();
    }

    @Provides
    @Named(DEPENDENCY_NAME_FLICKR)
    public Oauth1SigningInterceptor.TokenProvider tokenProvider(Session session) {
        return new Oauth1SigningInterceptor.TokenProvider() {
            @Override
            public String accessToken() {
                return session.getAccessToken();
            }

            @Override
            public String accessTokenSecret() {
                return session.getAccessTokenSecret();
            }
        };
    }

    @Provides
    @Singleton
    public Session session(Context context) {
        return new JsonSharedPrefsSession(context);
    }

    @Provides
    @Singleton
    public FlickrApi flickrService(@Named(DEPENDENCY_NAME_FLICKR) OkHttpClient client) {
        return new Retrofit.Builder()
                .baseUrl(Config.FLICKR_API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(client)
                .build()
                .create(FlickrApi.class);
    }

    @Provides
    public OAuth10aService authService() {
        return new ServiceBuilder()
                .apiKey(Config.FLICKR_CONSUMER_KEY)
                .apiSecret(Config.FLICKR_CONSUMER_SECRET)
                .callback(Config.AUTH_CALLBACK)
                .build(com.github.scribejava.apis.FlickrApi.instance());
    }

    @Provides
    @Singleton
    public RxScheduler androidRxScheduler() {
        return new RxAndroidScheduler();
    }

    @Provides
    public ReactiveLocationProvider locationProvider(Context context) {
        return new ReactiveLocationProvider(context);
    }

    @Provides
    public Picasso picasso(Context context) {
        return Picasso.with(context);
    }
}
