package com.mohit.minizomato.data

import androidx.lifecycle.LiveData
import com.mohit.minizomato.data.room.AppDatabase
import com.mohit.minizomato.model.restaurant_listing.RestaurantObj
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppDbHelper @Inject constructor(private val mAppDatabase: AppDatabase) {


    fun getRestaurants(): LiveData<MutableList<RestaurantObj>> {
        return mAppDatabase.restaurantDao().loadAll()
    }

    fun addRestaurant(rest: RestaurantObj, finished: () -> Unit) {
        val d = Observable.fromCallable {
            mAppDatabase.restaurantDao().addRestaurant(rest)
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
            finished()
        }, { throwable ->
            throwable.printStackTrace()
        })
    }

    fun removeRestaurant(id: String, finished: () -> Unit) {
        val d = Observable.fromCallable {
            mAppDatabase.restaurantDao().removeRestaurant(id)
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
            finished()
        }, { throwable ->
            throwable.printStackTrace()
        })
    }
}