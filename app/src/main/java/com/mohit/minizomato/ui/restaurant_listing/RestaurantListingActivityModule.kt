package com.mohit.minizomato.ui.restaurant_listing

import android.content.Context
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.mohit.minizomato.R
import com.mohit.minizomato.data.ApiHelper
import com.mohit.minizomato.data.AppDbHelper
import com.mohit.minizomato.di.ViewModelProviderFactory
import dagger.Module
import dagger.Provides

@Module
class RestaurantListingActivityModule {
    @Provides
    fun provideRestaurantListing(mApiHelper: ApiHelper, mAppDbHelper: AppDbHelper): RestaurantListingViewModel {
        return RestaurantListingViewModel(mApiHelper, mAppDbHelper)
    }

    @Provides
    fun provideRestaurantListingViewModelFactory(mRestaurantListingViewModel: RestaurantListingViewModel): ViewModelProvider.Factory {
        return ViewModelProviderFactory(mRestaurantListingViewModel)
    }

    @Provides
    fun provideLayoutManager(mContext: Context): LinearLayoutManager {
        return LinearLayoutManager(mContext)
    }

    @Provides
    fun provideRotateAnimation(mContext: Context): Animation {
        return AnimationUtils.loadAnimation(mContext, R.anim.rotate)

    }
}