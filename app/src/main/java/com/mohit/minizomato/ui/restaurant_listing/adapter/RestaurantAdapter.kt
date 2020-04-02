package com.mohit.minizomato.ui.restaurant_listing.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mohit.minizomato.R
import com.mohit.minizomato.model.RestaurantListItem
import kotlin.random.Random

class RestaurantAdapter(var mRestaurantList: MutableList<RestaurantListItem>, val favClickListener: (item: RestaurantListItem, pos: Int, isFav: Boolean) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        const val VIEW_TYPE_RESTAURANT = 1
        const val VIEW_TYPE_HEADER = 2
        const val VIEW_TYPE_EMPTY = 3
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View
        when (viewType) {
            VIEW_TYPE_RESTAURANT -> {
                view = LayoutInflater.from(parent.context).inflate(R.layout.item_restaurant, parent, false)
                return RestaurantViewHolder(view)
            }
            VIEW_TYPE_HEADER -> {
                view = LayoutInflater.from(parent.context).inflate(R.layout.item_cuisine_header, parent, false)
                return HeaderViewHolder(view)
            }
            VIEW_TYPE_EMPTY -> {
                view = LayoutInflater.from(parent.context).inflate(R.layout.item_empty_list, parent, false)
                return EmptyViewHolder(view)
            }
        }
        view = LayoutInflater.from(parent.context).inflate(R.layout.item_restaurant, parent, false)
        return RestaurantViewHolder(view)
    }

    override fun getItemCount(): Int {
        return if (mRestaurantList.size == 0) 1 else mRestaurantList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when (holder.itemViewType) {
            VIEW_TYPE_HEADER -> {
                val item = mRestaurantList[holder.adapterPosition]
                val mHeaderViewHolder = holder as HeaderViewHolder
                val color = Color.argb(255, Random.nextInt(50), Random.nextInt(50), Random.nextInt(50))
                mHeaderViewHolder.tvCuisine.setBackgroundColor(color)
                mHeaderViewHolder.tvCuisine.text = item.name

            }
            VIEW_TYPE_RESTAURANT -> {
                val item = mRestaurantList[holder.adapterPosition].restaurantObj
                val mRestaurantViewHolder = holder as RestaurantViewHolder
                mRestaurantViewHolder.tvRestaurantName.text = item.name
                mRestaurantViewHolder.tvAddress.text = item.location.address
                mRestaurantViewHolder.tvPrice.text = "Price of Two ${item.currency}${item.averageCostForTwo}"
                mRestaurantViewHolder.tvRating.text = item.userRating.aggregateRating
                mRestaurantViewHolder.tvRating.setBackgroundColor(Color.parseColor("#" + item.userRating.ratingColor))
                Glide.with(mRestaurantViewHolder.ivImage).load(item.featuredImage).into(mRestaurantViewHolder.ivImage)
                if (item.isFav) {
                    mRestaurantViewHolder.ivFav.setImageResource(R.mipmap.ic_selectwishlist)
                } else mRestaurantViewHolder.ivFav.setImageResource(R.mipmap.ic_wishlist)
            }

            VIEW_TYPE_EMPTY -> {
                val mEmptyViewHolder = holder as EmptyViewHolder
                mEmptyViewHolder.tvEmptyText.text = "Sorry No Results Found matching your Search Query."
            }
        }
    }


    override fun getItemViewType(position: Int): Int {
        return when {
            mRestaurantList.size == 0 -> VIEW_TYPE_EMPTY
            mRestaurantList[position].type == 1 -> VIEW_TYPE_HEADER
            else -> VIEW_TYPE_RESTAURANT
        }
    }

    inner class RestaurantViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tvRestaurantName: TextView = view.findViewById(R.id.tvRestaurantName)
        var tvAddress: TextView = view.findViewById(R.id.tvAddress)
        var tvPrice: TextView = view.findViewById(R.id.tvPrice)
        var ivImage: ImageView = view.findViewById(R.id.ivImage)
        var ivFav: ImageView = view.findViewById(R.id.ivFav)
        var tvRating: TextView = view.findViewById(R.id.tvRating)

        init {
            ivFav.setOnClickListener {
                favClickListener(mRestaurantList[adapterPosition], adapterPosition, mRestaurantList[adapterPosition].restaurantObj.isFav)
            }
        }

    }


    inner class HeaderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tvCuisine: TextView = view.findViewById(R.id.tvCuisine)

    }

    inner class EmptyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tvEmptyText: TextView = view.findViewById(R.id.tvEmptyText)

    }

}