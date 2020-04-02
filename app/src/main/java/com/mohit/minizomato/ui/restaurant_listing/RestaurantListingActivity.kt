package com.mohit.minizomato.ui.restaurant_listing

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.animation.Animation
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.mohit.minizomato.R
import com.mohit.minizomato.model.RestaurantListItem
import com.mohit.minizomato.ui.favorite_restaurant.FavoriteRestaurantListingActivity
import com.mohit.minizomato.ui.restaurant_listing.adapter.RestaurantAdapter
import dagger.android.AndroidInjection
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_restaurant_listing.*
import javax.inject.Inject


class RestaurantListingActivity : DaggerAppCompatActivity(), View.OnClickListener {

    private lateinit var mRestaurantAdapter: RestaurantAdapter
    private var sortDialog: AlertDialog? = null
    private var mRestaurantList = mutableListOf<RestaurantListItem>()
    private var sortOrder = 0
    private val reqPermission = 1
    private var currentLatitude = 0.0
    private var currentLongitude = 0.0
    private var fusedLocationClient: FusedLocationProviderClient? = null
    private var isFromSearchClick = false
    @Inject
    lateinit var rotateAnimation: Animation
    @Inject
    lateinit var mRestaurantListingViewModel: RestaurantListingViewModel
    @Inject
    lateinit var mLayoutManager: LinearLayoutManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_listing)
        AndroidInjection.inject(this)
        initView()
    }

    private fun showToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }


    private fun initView() {
        setListeners()
        setLoading()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        setCurrentLocation()
    }

    private fun setListeners() {
        ivSearch.setOnClickListener(this)
        ivSort.setOnClickListener(this)
        tvFavoriteRestaurants.setOnClickListener(this)

        etSearch.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                onSearchClick()
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
    }

    private fun onSearchClick() {
        isFromSearchClick = true
        sortOrder = 0
        getRestaurantList()
        closeKeyBoard()
    }

    private fun setLoading() {
        mRestaurantListingViewModel.getLoading().observe(this, Observer {
            if (it) {
                ivSearch.isEnabled = false
                pbLoading.visibility = View.VISIBLE
                ivSearch.startAnimation(rotateAnimation)
            } else {
                ivSearch.isEnabled = true
                pbLoading.visibility = View.GONE

                ivSearch.clearAnimation()
            }
        })
    }


    private fun closeKeyBoard() {
        val inputManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(currentFocus?.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }

    private fun setCurrentLocation() {

        if (hasPermission()) {
            fusedLocationClient?.lastLocation?.addOnCompleteListener {
                try {
                    if (it.isSuccessful && it.result != null) {
                        currentLatitude = it.result?.latitude ?: 0.0
                        currentLongitude = it.result?.longitude ?: 0.0
                        Log.d("Current Location", "$currentLatitude $currentLongitude ")
                        if (isFromSearchClick) {
                            getRestaurantList()
                            isFromSearchClick = false
                        }
                    } else {
                        showToast("Can't get current location")
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        } else {
            requestPermission()
        }
    }

    private fun getRestaurantList() {
        if (hasPermission() && currentLatitude != 0.0 && currentLongitude != 0.0) {

            mRestaurantListingViewModel.getRestaurants(etSearch.text.toString().trim(), sortOrder, currentLatitude, currentLongitude).observe(this, Observer {
                if (it != null && it.isSuccess && it.data != null) {
                    mRestaurantList = it.data
                    setAdapter()
                } else {
                    showToast(it.message ?: "Unknown Error")
                }
            })
        } else {
            setCurrentLocation()
        }
    }


    private fun setAdapter() {
        mRestaurantAdapter = RestaurantAdapter(mRestaurantList) { item, pos, isFav ->
            onFavClick(item, pos, isFav)
        }
        rvRestaurant.adapter = mRestaurantAdapter
        rvRestaurant.layoutManager = mLayoutManager

    }

    private fun onFavClick(listItem: RestaurantListItem, pos: Int, isFav: Boolean) {
        mRestaurantListingViewModel.toggleFav(listItem.restaurantObj, isFav) {
            mRestaurantList[pos].restaurantObj.isFav = !isFav
            mRestaurantAdapter.notifyDataSetChanged()
        }
    }

    private fun hasPermission(): Boolean {
        return (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), reqPermission)

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == reqPermission) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                showDeniedPermissionDialog()
            } else {
                setCurrentLocation()
            }
        }
    }

    private fun showDeniedPermissionDialog() {
        AlertDialog.Builder(this)
            .setTitle("Permission Required")
            .setMessage("Please allow Location permission to get restaurant near you")
            .setPositiveButton("Allow") { it1, it2 ->
                setCurrentLocation()
                it1.dismiss()
            }
            .setNegativeButton("Deny") { it1, it2 ->
                it1.dismiss()
            }.show()

    }


    private fun showSortDialog() {
        val alertDialog = AlertDialog.Builder(this)
        val dialogView = layoutInflater.inflate(R.layout.dialog_sort, null, false)
        alertDialog.setView(dialogView)
        sortDialog = alertDialog.create()
        val tvPopularity = dialogView.findViewById<TextView>(R.id.tvPopularity)
        val tvPriceHighToLow = dialogView.findViewById<TextView>(R.id.tvPriceHighToLow)
        val tvPriceLowToHigh = dialogView.findViewById<TextView>(R.id.tvPriceLowToHigh)
        val tvRatingHighToLow = dialogView.findViewById<TextView>(R.id.tvRatingHighToLow)

        tvPopularity.setOnClickListener {
            sortOrder = 0
            sortDialog?.dismiss()
            getRestaurantList()
        }

        tvPriceHighToLow.setOnClickListener {
            sortOrder = 1
            sortDialog?.dismiss()
            getRestaurantList()
        }

        tvPriceLowToHigh.setOnClickListener {
            sortOrder = 2
            sortDialog?.dismiss()
            getRestaurantList()
        }

        tvRatingHighToLow.setOnClickListener {
            sortOrder = 3
            sortDialog?.dismiss()
            getRestaurantList()
        }

        sortDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        sortDialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        sortDialog?.show()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.ivSort -> showSortDialog()
            R.id.ivSearch -> onSearchClick()
            R.id.tvFavoriteRestaurants -> {
                val intent = Intent(this, FavoriteRestaurantListingActivity::class.java)
                startActivity(intent)
            }
        }
    }


}
