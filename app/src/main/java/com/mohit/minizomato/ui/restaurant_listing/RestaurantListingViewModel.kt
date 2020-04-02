package com.mohit.minizomato.ui.restaurant_listing

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.mohit.minizomato.BuildConfig
import com.mohit.minizomato.data.ApiHelper
import com.mohit.minizomato.data.AppDbHelper
import com.mohit.minizomato.data.retrofit.ApiErrorResponse
import com.mohit.minizomato.data.retrofit.ApiSuccessResponse
import com.mohit.minizomato.model.CommonResponse
import com.mohit.minizomato.model.RestaurantListItem
import com.mohit.minizomato.model.restaurant_listing.Restaurant
import com.mohit.minizomato.model.restaurant_listing.RestaurantObj
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.set

class RestaurantListingViewModel(var mApiHelper: ApiHelper, var mAppDbHelper: AppDbHelper) : ViewModel() {
    private val mRestaurantList = mutableListOf<RestaurantListItem>()
    private val restaurantMap = HashMap<String, ArrayList<RestaurantObj>>()
    private val isLoading = MutableLiveData<Boolean>()

    fun getRestaurants(query: String, sortOrder: Int, currentLatitude: Double, currentLongitude: Double): LiveData<CommonResponse> {

        val searchParams = HashMap<String, Any>()
        searchParams["q"] = query
        searchParams["apikey"] = BuildConfig.API_KEY
        searchParams["lat"] = currentLatitude
        searchParams["lon"] = currentLongitude
        searchParams["count"] = 10

        when (sortOrder) {
            0 -> {
                if (searchParams.containsKey("sort")) searchParams.remove("sort")
                if (searchParams.containsKey("order")) searchParams.remove("order")
            }
            1 -> {
                searchParams["order"] = "desc"
                searchParams["sort"] = "cost"
            }
            2 -> {
                searchParams["order"] = "asc"
                searchParams["sort"] = "cost"
            }
            3 -> {
                searchParams["order"] = "desc"
                searchParams["sort"] = "rating"
            }
        }
        if (query.isEmpty()) {
            val validationError = MutableLiveData<CommonResponse>()
            validationError.postValue(CommonResponse(false, "Please enter something to search", null))
            return validationError
        }
        isLoading.postValue(true)

        return Transformations.map(mApiHelper.getRestaurantListing(searchParams)) {
            mRestaurantList.clear()
            restaurantMap.clear()
            when (it) {

                is ApiSuccessResponse -> {
                    processList(it.body.restaurants)
                    isLoading.postValue(false)
                    return@map CommonResponse(true, "Success", mRestaurantList)
                }
                is ApiErrorResponse -> {
                    isLoading.postValue(false)
                    return@map CommonResponse(false, it.errorMessage, null)
                }
                else -> {
                    isLoading.postValue(false)
                    return@map CommonResponse(false, "Unknown Error Occurred", null)
                }
            }
        }
    }

    /**
     * @param restaurants
     * this function will process the raw list of restaurant and and map it with cuisines
     * as unique key and list of restaurants as values and make a Map of cuisine and restaurants
     * and finally put cuisines and its restaurants in a final custom list.
     */
    private fun processList(restaurants: MutableList<Restaurant>) {

        restaurants.forEach { restaurant ->
            val cuisineList = restaurant.restaurant.cuisines.split(",")
            cuisineList.forEach { cuisine ->
                val restaurantObjList = restaurantMap[cuisine.trim()]
                if (restaurantObjList == null) {
                    val newRestaurantList = arrayListOf<RestaurantObj>()
                    newRestaurantList.add(restaurant.restaurant)
                    restaurantMap[cuisine.trim()] = newRestaurantList
                } else {
                    restaurantObjList.add(restaurant.restaurant)
                    restaurantMap[cuisine.trim()] = restaurantObjList
                }
            }
        }
        //put Cuisine header and restaurant in the final list
        restaurantMap.forEach { t ->
            mRestaurantList.add(RestaurantListItem(1, t.key))
            t.value.forEach {
                mRestaurantList.add(RestaurantListItem(2, it))
            }
        }
    }

    fun getLoading(): LiveData<Boolean> {
        return isLoading
    }

    fun toggleFav(restaurantObj: RestaurantObj, isFav: Boolean, isFinished: () -> Unit) {
        if (isFav) {

            mAppDbHelper.removeRestaurant(restaurantObj.id) {
                isFinished()
            }
        } else {
            val copyObj = restaurantObj
            copyObj.isFav = true
            mAppDbHelper.addRestaurant(copyObj) {
                isFinished()
            }
        }
    }

    fun getFavRestaurants(): LiveData<MutableList<RestaurantObj>> {
        return mAppDbHelper.getRestaurants()
    }

}



