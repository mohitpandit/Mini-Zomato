package com.mohit.minizomato.model.restaurant_listing;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Restaurant {

    @SerializedName("restaurant")
    @Expose
    private RestaurantObj restaurant;

    public RestaurantObj getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(RestaurantObj restaurant) {
        this.restaurant = restaurant;
    }
}
