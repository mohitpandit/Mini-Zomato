package com.mohit.minizomato.data.retrofit

import androidx.lifecycle.LiveData
import com.mohit.minizomato.model.restaurant_listing.RestaurantListingResponse
import retrofit2.http.GET
import retrofit2.http.QueryMap
import java.util.*

interface ApiInterface {

    @GET("search")
    fun searchRestaurant(@QueryMap map: HashMap<String, Any>): LiveData<ApiResponse<RestaurantListingResponse>>
}