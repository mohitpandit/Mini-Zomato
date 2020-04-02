package com.mohit.minizomato.model;

import com.mohit.minizomato.model.restaurant_listing.RestaurantObj;

public class RestaurantListItem {

    private int type;
    private String name;
    private RestaurantObj restaurantObj;

    public RestaurantListItem(int type, String name) {
        this.type = type;
        this.name = name;
    }

    public RestaurantListItem(int type, RestaurantObj restaurantObj) {
        this.type = type;
        this.restaurantObj = restaurantObj;
    }

    public int getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public RestaurantObj getRestaurantObj() {
        return restaurantObj;
    }
}
