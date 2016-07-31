package com.eggman.localr.utils;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by mharris on 7/29/16.
 * eggmanapps.
 */
public class RxAndroidScheduler implements RxScheduler {

    /**
     * Applies android specific background threading behavior to our observable.
     *
     * @param <T> the observable emission type that will be applied to.
     * @return the transformer for applying schedulers.
     */
    public <T> Observable.Transformer<T, T> applySchedulers() {
        return observable -> observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
