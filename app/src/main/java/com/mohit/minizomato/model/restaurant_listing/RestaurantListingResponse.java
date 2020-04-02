
package com.mohit.minizomato.model.restaurant_listing;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RestaurantListingResponse {

    @SerializedName("results_found")
    @Expose
    private int resultsFound;
    @SerializedName("results_start")
    @Expose
    private int resultsStart;
    @SerializedName("results_shown")
    @Expose
    private int resultsShown;
    @SerializedName("restaurants")
    @Expose
    private List<Restaurant> restaurants = null;

    public int getResultsFound() {
        return resultsFound;
    }

    public void setResultsFound(int resultsFound) {
        this.resultsFound = resultsFound;
    }

    public int getResultsStart() {
        return resultsStart;
    }

    public void setResultsStart(int resultsStart) {
        this.resultsStart = resultsStart;
    }

    public int getResultsShown() {
        return resultsShown;
    }

    public void setResultsShown(int resultsShown) {
        this.resultsShown = resultsShown;
    }

    public List<Restaurant> getRestaurants() {
        return restaurants;
    }

    public void setRestaurants(List<Restaurant> restaurants) {
        this.restaurants = restaurants;
    }

}
