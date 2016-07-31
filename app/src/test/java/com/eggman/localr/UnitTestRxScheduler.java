package com.eggman.localr;

import com.eggman.localr.utils.RxScheduler;

import rx.Observable;

/**
 * Created by mharris on 7/30/16.
 * eggmanapps.
 */
public class UnitTestRxScheduler implements RxScheduler {
    @Override
    public <T> Observable.Transformer<T, T> applySchedulers() {
        return observable -> observable;
    }
}
