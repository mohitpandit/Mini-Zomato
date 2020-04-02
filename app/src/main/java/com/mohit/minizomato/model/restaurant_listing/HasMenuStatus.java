
package com.mohit.minizomato.model.restaurant_listing;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HasMenuStatus {

    @SerializedName("delivery")
    @Expose
    private int delivery;
    @SerializedName("takeaway")
    @Expose
    private int takeaway;

    public int getDelivery() {
        return delivery;
    }

    public void setDelivery(int delivery) {
        this.delivery = delivery;
    }

    public int getTakeaway() {
        return takeaway;
    }

    public void setTakeaway(int takeaway) {
        this.takeaway = takeaway;
    }

}
