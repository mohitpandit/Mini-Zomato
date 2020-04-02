package com.mohit.minizomato.ui.favorite_restaurant

import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.mohit.minizomato.R
import com.mohit.minizomato.model.RestaurantListItem
import com.mohit.minizomato.ui.restaurant_listing.adapter.RestaurantAdapter
import dagger.android.AndroidInjection
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_favorite_restaurant_listing.*
import javax.inject.Inject


class FavoriteRestaurantListingActivity : DaggerAppCompatActivity() {
    @Inject
    lateinit var mFavoriteRestaurantListingViewModel: FavoriteRestaurantListingViewModel
    @Inject
    lateinit var mLayoutManager: LinearLayoutManager
    private var mRestaurantAdapter: RestaurantAdapter? = null
    private var mRestaurantList = mutableListOf<RestaurantListItem>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite_restaurant_listing)
        AndroidInjection.inject(this)
        initView()
    }

    private fun initView() {
        mFavoriteRestaurantListingViewModel.getFavRestaurants().observe(this, Observer {

            if (it.data != null) mRestaurantList = it.data
            mRestaurantAdapter = RestaurantAdapter(mRestaurantList) { item, pos, isFav ->
                onfavClick(item, pos, isFav)
            }
            rvFavRestaurant.adapter = mRestaurantAdapter
            rvFavRestaurant.layoutManager = mLayoutManager
        })
    }

    private fun showToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    private fun onfavClick(item: RestaurantListItem, pos: Int, fav: Boolean) {
        mFavoriteRestaurantListingViewModel.removeFav(item.restaurantObj.id) {
            showToast("Restaurant removed from Favorite.")
        }
    }


}
