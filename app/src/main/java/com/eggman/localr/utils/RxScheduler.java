package com.eggman.localr.utils;

import rx.Observable;

/**
 * Created by mharris on 7/29/16.
 * DispatchHealth.
 */
public interface RxScheduler {

    /**
     * Applies background threading behavior to our observable, implement based on the platform needs.
     *
     * @param <T> the observable emission type that will be applied to.
     * @return the transformer for applying schedulers.
     */
    <T> Observable.Transformer<T, T> applySchedulers();
}
