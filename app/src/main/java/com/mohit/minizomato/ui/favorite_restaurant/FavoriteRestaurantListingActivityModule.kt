package com.mohit.minizomato.ui.favorite_restaurant

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.mohit.minizomato.data.ApiHelper
import com.mohit.minizomato.data.AppDbHelper
import com.mohit.minizomato.di.ViewModelProviderFactory
import dagger.Module
import dagger.Provides

@Module
class FavoriteRestaurantListingActivityModule {
    @Provides
    fun provideFavoriteRestaurantListingViewModel(mApiHelper: ApiHelper, mAppDbHelper: AppDbHelper): FavoriteRestaurantListingViewModel {
        return FavoriteRestaurantListingViewModel(mApiHelper, mAppDbHelper)
    }

    @Provides
    fun provideFavoriteRestaurantListingViewModelFactory(mFavoriteRestaurantListingViewModel: FavoriteRestaurantListingViewModel): ViewModelProvider.Factory {
        return ViewModelProviderFactory(mFavoriteRestaurantListingViewModel)
    }


    @Provides
    fun provideLayoutManager(mContext: Context): LinearLayoutManager {
        return LinearLayoutManager(mContext)
    }
}