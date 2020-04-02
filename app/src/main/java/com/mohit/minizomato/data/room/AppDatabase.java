package com.mohit.minizomato.data.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.mohit.minizomato.model.restaurant_listing.RestaurantObj;


@Database(entities = {RestaurantObj.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract RestaurantDao restaurantDao();
}
