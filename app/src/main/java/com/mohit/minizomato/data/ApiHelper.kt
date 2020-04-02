package com.mohit.minizomato.data

import androidx.lifecycle.LiveData
import com.mohit.minizomato.data.retrofit.ApiInterface
import com.mohit.minizomato.data.retrofit.ApiResponse
import com.mohit.minizomato.model.restaurant_listing.RestaurantListingResponse
import javax.inject.Inject

class ApiHelper @Inject internal constructor(var mApiInterface: ApiInterface) {
    fun getRestaurantListing(searchParams: HashMap<String, Any>): LiveData<ApiResponse<RestaurantListingResponse>> {
        return mApiInterface.searchRestaurant(searchParams)
    }
}