package com.mohit.minizomato.data.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mohit.minizomato.model.restaurant_listing.RestaurantObj

@Dao
interface RestaurantDao {
    @Query("SELECT * FROM Restaurant order By id DESC")
    fun loadAll(): LiveData<MutableList<RestaurantObj>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addRestaurant(item: RestaurantObj)

    @Query("delete from Restaurant where id=:id")
    fun removeRestaurant(id: String)
}