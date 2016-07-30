package com.eggman.localr.service.model;

import com.eggman.localr.model.Photo;

import java.util.List;

/**
 * Created by mharris on 7/29/16.
 *
 * Wraps a response in the structure that flickr returns.
 */
public class PhotosResponse {

    public PhotosBody photos;

    public class PhotosBody {
        public List<Photo> photo;
    }
}
