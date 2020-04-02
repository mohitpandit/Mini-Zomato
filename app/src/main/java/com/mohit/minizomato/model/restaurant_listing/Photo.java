
package com.mohit.minizomato.model.restaurant_listing;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Photo {

    @SerializedName("photo")
    @Expose
    private PhotoObj photo;

    public PhotoObj getPhoto() {
        return photo;
    }

    public void setPhoto(PhotoObj photo) {
        this.photo = photo;
    }

}
