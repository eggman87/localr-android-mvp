package com.eggman.localr.ui.home;

import android.databinding.DataBindingUtil;
import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.eggman.localr.R;
import com.eggman.localr.databinding.ListItemPhotoBinding;
import com.eggman.localr.model.Photo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mharris on 7/30/16.
 * DispatchHealth.
 *
 */
public class PhotosAdapter extends RecyclerView.Adapter<PhotosAdapter.PhotoViewHolder> {

    private Picasso picasso;
    private List<Photo> photos = new ArrayList<>();

    public PhotosAdapter(Picasso picasso) {
        this.picasso = picasso;
    }

    @Override
    public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ListItemPhotoBinding binding = DataBindingUtil.inflate(inflater, R.layout.list_item_photo, parent, false);

        return new PhotoViewHolder(binding.getRoot(), binding);
    }

    @Override
    public void onBindViewHolder(PhotoViewHolder holder, int position) {
        Photo photo = photos.get(position);
        holder.bind(photo, picasso);
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }

    @UiThread
    public void addPhotos(List<Photo> photos) {
        this.photos.addAll(photos);
        notifyDataSetChanged();
    }

    @UiThread
    public void clearItems() {
        this.photos.clear();
        notifyDataSetChanged();
    }

    public static class PhotoViewHolder extends RecyclerView.ViewHolder {

        private final ListItemPhotoBinding binding;

        public PhotoViewHolder(View itemView, ListItemPhotoBinding binding) {
            super(itemView);

            this.binding = binding;
        }

        @UiThread
        public void bind(Photo photo, Picasso picasso) {
            this.binding.setPhoto(photo);
            this.binding.listItemPhtoIvThumb.setImageDrawable(null);
            this.binding.listItemPhtoIvThumb.setLayoutParams(new FrameLayout.LayoutParams(photo.largeWidth, photo.largeHeight));
            picasso.load(photo.urlLarge).into(this.binding.listItemPhtoIvThumb);
        }
    }
}
