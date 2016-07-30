package com.eggman.localr.ui.home;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.eggman.localr.R;
import com.eggman.localr.databinding.ActivityHomeBinding;
import com.eggman.localr.model.Photo;
import com.eggman.localr.ui.BaseActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import icepick.State;

/**
 * Created by mharris on 7/29/16.
 * DispatchHealth.
 */
public class HomeActivity extends BaseActivity implements HomeView {

    @Inject
    protected HomePresenter presenter;
    @Inject
    protected Picasso picasso;

    @State
    protected ArrayList<Photo> photos;

    private ActivityHomeBinding view;
    private PhotosAdapter adapter;

    private static final int REQUEST_LOCATION_PERMISSION = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        injector().inject(this);
        presenter.attach(this);

        view = DataBindingUtil.setContentView(this, R.layout.activity_home);

        setupView();
        checkLocationPermissions();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        presenter.detach();
    }

    @Override
    public void displayPhotos(List<Photo> photos) {
        setPhotos(photos);
        animateItemsIn();
    }

    @Override
    public void displayNoPhotosFound() {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationPermissionsGranted();
            } else {
                //todo: add  no permission handling
            }
        }
    }

    private void setupView() {
        this.view.actHomeContainer.setVisibility(View.INVISIBLE);

        this.adapter = new PhotosAdapter(picasso);
        RecyclerView photosList = this.view.actHomeRvPhotos;
        photosList.setAdapter(this.adapter);

        boolean isLandscape = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
        photosList.setLayoutManager(new StaggeredGridLayoutManager(isLandscape ? 3:2, StaggeredGridLayoutManager.VERTICAL));

    }

    private void locationPermissionsGranted(){
        if (photos == null) {
            presenter.getPhotosForUsersLocation();
        } else {
            this.view.actHomeContainer.setVisibility(View.VISIBLE);
            setPhotos(this.photos);
        }
    }

    private void checkLocationPermissions() {
        boolean hasLocationPermission = (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)) == PackageManager.PERMISSION_GRANTED;

        if (hasLocationPermission) {
            locationPermissionsGranted();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        }
    }

    private void setPhotos(List<Photo> photos){
        this.photos = (ArrayList<Photo>) photos;
        this.adapter.addPhotos(photos);
    }

    private void animateItemsIn() {
        //delay the animation so images have a chance to load.
        new Handler().postDelayed(() -> {
            view.actHomeContainer.setVisibility(View.VISIBLE);
            Animation bottomUp = AnimationUtils.loadAnimation(HomeActivity.this, R.anim.bottom_up);
            view.actHomeContainer.startAnimation(bottomUp);
        }, 750);
    }
}
