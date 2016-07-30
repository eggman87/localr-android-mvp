package com.eggman.localr.ui;

import com.eggman.localr.utils.NullObject;

/**
 * Created by mharris on 7/29/16.
 * .
 */

public abstract class BasePresenter<TView> {

    protected TView view;
    //this will always be a proxy to avoid NPEs when view detaches...
    private final TView nullView;

    public BasePresenter(Class<TView> viewClazz) {
        nullView = NullObject.create(viewClazz);
    }

    public void attach(TView view) {
        this.view = view;
    }

    public void detach() {
        this.view = nullView;
    }
}
