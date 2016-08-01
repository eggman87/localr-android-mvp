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
import android.widget.Toast;

import com.eggman.localr.R;
import com.eggman.localr.analytics.Screens;
import com.eggman.localr.databinding.ActivityHomeBinding;
import com.eggman.localr.model.Photo;
import com.eggman.localr.ui.BaseActivity;
import com.eggman.localr.ui.shared.AlertDialogFragment;
import com.eggman.localr.ui.shared.DialogButtonClickedEvent;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import icepick.State;

/**
 * Created by mharris on 7/29/16.
 * eggmanapps.
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
    private static final int TAG_LOCATION_FAILED = 2;
    private static final int TAG_API_FAILED = 3;

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
    protected void handleEvent(Object event) {
        super.handleEvent(event);

        if (event instanceof DialogButtonClickedEvent) {
            dialogButtonClicked((DialogButtonClickedEvent) event);
        }
    }

    @Override
    public void displayPhotos(List<Photo> photos) {
        this.view.actHomeProgress.setVisibility(View.GONE);

        setPhotos(photos);
        animateItemsIn();
    }

    @Override
    public void displayNoPhotosFound() {
        this.view.actHomeContainer.setRefreshing(false);
    }

    @Override
    public void displayLocationNotFound() {
        AlertDialogFragment fragment = AlertDialogFragment.newInstance(TAG_LOCATION_FAILED,
                R.string.alert_location_failed, R.string.alert_retry, R.string.alert_exit);
        fragment.show(getSupportFragmentManager(), "location");
    }

    @Override
    public void displayErrorLoadingPhotos() {
        AlertDialogFragment fragment = AlertDialogFragment.newInstance(TAG_API_FAILED,
                R.string.alert_photos_error, R.string.alert_retry, R.string.alert_exit);
        fragment.show(getSupportFragmentManager(), "photos");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationPermissionsGranted();
            } else {
                Toast.makeText(this, R.string.location_permission_required, Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private void setupView() {
        this.view.actHomeContainer.setVisibility(View.INVISIBLE);
        this.view.actHomeContainer.setColorSchemeColors(color(R.color.colorPrimary), color(R.color.colorAccent), color(R.color.colorPrimaryDark));

        this.adapter = new PhotosAdapter(picasso);
        RecyclerView photosList = this.view.actHomeRvPhotos;
        photosList.setAdapter(this.adapter);

        boolean isLandscape = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
        photosList.setLayoutManager(new StaggeredGridLayoutManager(isLandscape ? 3:2, StaggeredGridLayoutManager.VERTICAL));

        view.actHomeContainer.setOnRefreshListener(() -> presenter.getPhotosForUsersLocation());
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
        this.view.actHomeContainer.setRefreshing(false);

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

    private void dialogButtonClicked(DialogButtonClickedEvent event) {
        //for now just allow retry or exiting app.
        if (event.getDialogTag() == TAG_LOCATION_FAILED || event.getDialogTag() == TAG_API_FAILED) {
            switch (event.getSelectedAction()) {
                case DialogButtonClickedEvent.CONFIRM_CLICKED:
                    presenter.getPhotosForUsersLocation();
                    break;
                case DialogButtonClickedEvent.CANCEL_CLICKED:
                    finish();
                    break;
            }
        }
    }

    @Override
    protected String getScreenName() {
        return Screens.HOME;
    }
}
