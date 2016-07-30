package com.eggman.localr.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by mharris on 7/29/16.
 *
 */
public class Photo implements Parcelable {

    public String id;
    public String owner;
    public String secret;
    public String title;
    public boolean isPublic;
    public boolean isFriend;
    public boolean isFamily;
    @SerializedName("dateupload")
    public long dateUploaded;
    @SerializedName("datetaken")
    public String dateTaken;
    @SerializedName("ownername")
    public String ownerName;
    public int views;
    public double latitude;
    public double longitude;
    @SerializedName("url_t")
    public String urlThumb;
    @SerializedName("height_t")
    public int thumbHeight;
    @SerializedName("width_t")
    public int thumbWidth;
    @SerializedName("url_l")
    public String urlLarge;
    @SerializedName("height_l")
    public int largeHeight;
    @SerializedName("width_l")
    public int largeWidth;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.owner);
        dest.writeString(this.secret);
        dest.writeString(this.title);
        dest.writeByte(this.isPublic ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isFriend ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isFamily ? (byte) 1 : (byte) 0);
        dest.writeLong(this.dateUploaded);
        dest.writeString(this.dateTaken);
        dest.writeString(this.ownerName);
        dest.writeInt(this.views);
        dest.writeDouble(this.latitude);
        dest.writeDouble(this.longitude);
        dest.writeString(this.urlThumb);
        dest.writeInt(this.thumbHeight);
        dest.writeInt(this.thumbWidth);
        dest.writeString(this.urlLarge);
        dest.writeInt(this.largeHeight);
        dest.writeInt(this.largeWidth);
    }

    public Photo() {
    }

    protected Photo(Parcel in) {
        this.id = in.readString();
        this.owner = in.readString();
        this.secret = in.readString();
        this.title = in.readString();
        this.isPublic = in.readByte() != 0;
        this.isFriend = in.readByte() != 0;
        this.isFamily = in.readByte() != 0;
        this.dateUploaded = in.readLong();
        this.dateTaken = in.readString();
        this.ownerName = in.readString();
        this.views = in.readInt();
        this.latitude = in.readDouble();
        this.longitude = in.readDouble();
        this.urlThumb = in.readString();
        this.thumbHeight = in.readInt();
        this.thumbWidth = in.readInt();
        this.urlLarge = in.readString();
        this.largeHeight = in.readInt();
        this.largeWidth = in.readInt();
    }

    public static final Parcelable.Creator<Photo> CREATOR = new Parcelable.Creator<Photo>() {
        @Override
        public Photo createFromParcel(Parcel source) {
            return new Photo(source);
        }

        @Override
        public Photo[] newArray(int size) {
            return new Photo[size];
        }
    };
}
