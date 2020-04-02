
package com.mohit.minizomato.model.restaurant_listing;

import androidx.room.Embedded;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RatingObj {

    @SerializedName("title")
    @Expose
    @Embedded
    private Title title;
    @SerializedName("bg_color")
    @Expose
    @Embedded
    private BgColor bgColor;

    public Title getTitle() {
        return title;
    }

    public void setTitle(Title title) {
        this.title = title;
    }

    public BgColor getBgColor() {
        return bgColor;
    }

    public void setBgColor(BgColor bgColor) {
        this.bgColor = bgColor;
    }

}
