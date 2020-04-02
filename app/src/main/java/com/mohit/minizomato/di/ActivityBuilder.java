package com.mohit.minizomato.di;


import com.mohit.minizomato.ui.favorite_restaurant.FavoriteRestaurantListingActivity;
import com.mohit.minizomato.ui.favorite_restaurant.FavoriteRestaurantListingActivityModule;
import com.mohit.minizomato.ui.restaurant_listing.RestaurantListingActivity;
import com.mohit.minizomato.ui.restaurant_listing.RestaurantListingActivityModule;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBuilder {


    @ContributesAndroidInjector(modules = RestaurantListingActivityModule.class)
    abstract RestaurantListingActivity provideRestaurantListingActivity();

    @ContributesAndroidInjector(modules = FavoriteRestaurantListingActivityModule.class)
    abstract FavoriteRestaurantListingActivity provideFavoriteRestaurantListingActivity();
}
