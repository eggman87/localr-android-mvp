package com.eggman.localr.interactor;

import com.eggman.localr.model.Photo;
import com.eggman.localr.service.FlickrApi;
import com.eggman.localr.utils.RxScheduler;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by mharris on 7/30/16.
 * eggmanapps.
 */
public class PhotosInteractor {

    private RxScheduler scheduler;
    private FlickrApi flickrApi;

    @Inject
    public PhotosInteractor(RxScheduler scheduler, FlickrApi flickrApi) {
        this.scheduler = scheduler;
        this.flickrApi = flickrApi;
    }

    public Observable<List<Photo>> getPhotos(double lat, double lng) {
        return flickrApi.getPhotosForLocation(lat, lng)
                .compose(scheduler.applySchedulers())
                .map(photosResponse -> photosResponse.photos.photo);
    }
}
