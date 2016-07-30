package com.eggman.localr.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mharris on 7/30/16.
 * DispatchHealth.
 */
public class User {
    public String id;

    @SerializedName("username")
    public UserName userName;

    public class UserName {
        @SerializedName("_content")
        public String name;
    }
}
