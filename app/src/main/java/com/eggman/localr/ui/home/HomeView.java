package com.eggman.localr.ui.home;

import com.eggman.localr.model.Photo;

import java.util.List;

/**
 * Created by mharris on 7/29/16.
 * eggmanapps.
 */
public interface HomeView {

    void displayPhotos(List<Photo> photos);
    void displayNoPhotosFound();
}
