package com.eggman.localr.service;

import com.eggman.localr.service.model.LoginResponse;
import com.eggman.localr.service.model.PhotosResponse;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by mharris on 7/29/16.
 * .
 */

public interface FlickrApi {

    @GET("?method=flickr.photos.search&nojsoncallback=1&format=json&extras=description,date_upload,date_taken,owner_name," +
            "geo,tags,views,media,url_t,url_l")
    Observable<PhotosResponse> getPhotosForLocation(@Query("lat") double lat, @Query("lon") double lon);

    @GET("?method=flickr.test.login&nojsoncallback=1&format=json")
    Observable<LoginResponse> validateToken();
}
