package com.eggman.localr;

import android.location.Location;

import com.eggman.localr.interactor.PhotosInteractor;
import com.eggman.localr.model.Photo;
import com.eggman.localr.ui.home.HomePresenter;
import com.eggman.localr.ui.home.HomeView;
import com.google.android.gms.location.LocationRequest;

import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import pl.charmas.android.reactivelocation.ReactiveLocationProvider;
import rx.Observable;

import static org.mockito.Mockito.*;

/**
 * Created by mharris on 7/31/16.
 * eggmanapps.
 */
public class HomePresenterTest {

    @Test
    public void testGetPhotosForUsersCollection() {
        ReactiveLocationProvider locationProvider = mock(ReactiveLocationProvider.class);
        PhotosInteractor photosInteractor = mock(PhotosInteractor.class);
        HomeView homeView = mock(HomeView.class);

        Location mockLocation = mock(Location.class);
        when(mockLocation.getLatitude()).thenReturn(40d);
        when(mockLocation.getLongitude()).thenReturn(41d);

        when(locationProvider.getUpdatedLocation(any(LocationRequest.class))).thenReturn(Observable.just(mockLocation));

        List<Photo> mockPhotos = new ArrayList<>();
        Photo photo = new Photo();
        photo.id = "unit_test_image_id";
        mockPhotos.add(photo);
        Observable<List<Photo>> photosObservable = Observable.just(mockPhotos);

        when(photosInteractor.getPhotos(40d, 41d)).thenReturn(photosObservable);

        HomePresenter sut = new HomePresenter(locationProvider, photosInteractor);
        sut.attach(homeView);
        sut.getPhotosForUsersLocation();

        verify(homeView).displayPhotos(mockPhotos);
    }

    @Test
    public void testGetPhotosLocationTimeout() {
        ReactiveLocationProvider locationProvider = mock(ReactiveLocationProvider.class);
        PhotosInteractor photosInteractor = mock(PhotosInteractor.class);
        HomeView homeView = mock(HomeView.class);

        when(locationProvider.getUpdatedLocation(any(LocationRequest.class))).thenReturn(Observable.error(new TimeoutException("timed out finding location")));

        HomePresenter sut = new HomePresenter(locationProvider, photosInteractor);
        sut.attach(homeView);
        sut.getPhotosForUsersLocation();

        verify(homeView).displayLocationNotFound();
    }

    @Test
    public void testGetPhotosFlickrApiError() {
        ReactiveLocationProvider locationProvider = mock(ReactiveLocationProvider.class);
        PhotosInteractor photosInteractor = mock(PhotosInteractor.class);
        HomeView homeView = mock(HomeView.class);

        Location mockLocation = mock(Location.class);

        when(mockLocation.getLatitude()).thenReturn(40d);
        when(mockLocation.getLongitude()).thenReturn(41d);
        when(locationProvider.getUpdatedLocation(any(LocationRequest.class))).thenReturn(Observable.just(mockLocation));
        when(photosInteractor.getPhotos(anyDouble(), anyDouble())).thenReturn(Observable.error(new IOException("500 Error")));

        HomePresenter sut = new HomePresenter(locationProvider, photosInteractor);
        sut.attach(homeView);
        sut.getPhotosForUsersLocation();

        verify(homeView).displayErrorLoadingPhotos();
    }

    @Test
    public void testGetPhotosNoPhotosFound() {
        ReactiveLocationProvider locationProvider = mock(ReactiveLocationProvider.class);
        PhotosInteractor photosInteractor = mock(PhotosInteractor.class);
        HomeView homeView = mock(HomeView.class);

        Location mockLocation = mock(Location.class);
        when(mockLocation.getLatitude()).thenReturn(40d);
        when(mockLocation.getLongitude()).thenReturn(41d);

        when(locationProvider.getUpdatedLocation(any(LocationRequest.class))).thenReturn(Observable.just(mockLocation));

        List<Photo> mockPhotos = new ArrayList<>();
        Observable<List<Photo>> photosObservable = Observable.just(mockPhotos);

        when(photosInteractor.getPhotos(40d, 41d)).thenReturn(photosObservable);

        HomePresenter sut = new HomePresenter(locationProvider, photosInteractor);
        sut.attach(homeView);
        sut.getPhotosForUsersLocation();

        verify(homeView).displayNoPhotosFound();
    }


}
