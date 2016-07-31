package com.eggman.localr.interactor;

import com.eggman.localr.model.User;
import com.eggman.localr.service.FlickrApi;
import com.eggman.localr.service.model.LoginResponse;
import com.eggman.localr.utils.RxScheduler;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by mharris on 7/30/16.
 * eggmanapps.
 */
public class UserInteractor {

    private RxScheduler scheduler;
    private FlickrApi flickrApi;

    @Inject
    public UserInteractor(RxScheduler scheduler, FlickrApi flickrApi) {
        this.scheduler = scheduler;
        this.flickrApi = flickrApi;
    }

    public Observable<User> validateUserStillAuthenticated() {
        return flickrApi.validateToken()
                .compose(scheduler.applySchedulers())
                .map(loginResponse -> loginResponse.user);
    }
}
