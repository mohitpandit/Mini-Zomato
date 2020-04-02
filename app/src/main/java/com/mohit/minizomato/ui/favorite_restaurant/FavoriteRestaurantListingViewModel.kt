package com.mohit.minizomato.ui.favorite_restaurant

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.mohit.minizomato.data.ApiHelper
import com.mohit.minizomato.data.AppDbHelper
import com.mohit.minizomato.model.CommonResponse
import com.mohit.minizomato.model.RestaurantListItem

class FavoriteRestaurantListingViewModel(var mApiHelper: ApiHelper, var mAppDbHelper: AppDbHelper) : ViewModel() {

    fun getFavRestaurants(): LiveData<CommonResponse> {
        return Transformations.map(mAppDbHelper.getRestaurants()) {
            val mRestaurantList = mutableListOf<RestaurantListItem>()
            it.forEach { restaurant ->
                mRestaurantList.add(RestaurantListItem(2, restaurant))
            }
            return@map CommonResponse(true, "Success", mRestaurantList)

        }
    }


    fun removeFav(id: String, isFinished: () -> Unit) {
        mAppDbHelper.removeRestaurant(id) {
            isFinished()
        }
    }
}



