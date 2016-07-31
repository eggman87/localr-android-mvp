package com.eggman.localr.ui.home;

import android.location.Location;

import com.eggman.localr.interactor.PhotosInteractor;
import com.eggman.localr.model.Photo;
import com.eggman.localr.ui.BasePresenter;
import com.google.android.gms.location.LocationRequest;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import pl.charmas.android.reactivelocation.ReactiveLocationProvider;

/**
 * Created by mharris on 7/29/16.
 * DispatchHealth.
 *
 * Using singleton for presenters this app is so simple, on a real app I would recommend coming up with a way
 * so that the presenter stays alive for activity config change but can be killed once the activity
 * is no longer in use.
 */
@Singleton
public class HomePresenter extends BasePresenter<HomeView> {

    private ReactiveLocationProvider locationProvider;
    private PhotosInteractor photosInteractor;

    @Inject
    public HomePresenter(ReactiveLocationProvider locationProvider, PhotosInteractor photosInteractor) {
        super(HomeView.class);

        this.locationProvider = locationProvider;
        this.photosInteractor = photosInteractor;
    }

    public void getPhotosForUsersLocation() {
        LocationRequest request = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setNumUpdates(1)
                .setInterval(100);

        locationProvider.getUpdatedLocation(request)
                .subscribe(this::locationReceived);
    }

    private void locationReceived(Location location) {
        photosInteractor.getPhotos(location.getLatitude(), location.getLongitude())
                .subscribe(this::onPhotosLoaded);
    }

    private void onPhotosLoaded(List<Photo> photos) {
        view.displayPhotos(photos);
    }
}
