package com.example.foodorderapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.foodorderapp.data.model.cart.Cart
import com.example.foodorderapp.data.model.databasemodel.DataBaseProductModel
import com.example.foodorderapp.data.model.product.Yemekler
import com.example.foodorderapp.databinding.CartPageProductItemBinding
import com.example.foodorderapp.databinding.RvItemFavoritesPageBinding
import com.example.foodorderapp.ui.viewmodel.FavoritePageViewModel
import com.example.foodorderapp.ui.viewmodel.OrderPageViewModel
import com.example.foodorderapp.utils.loadImage

class FavoritePageProductAdapter(
    var productList: List<DataBaseProductModel>,
    val viewModel: FavoritePageViewModel
) :
    RecyclerView.Adapter<FavoritePageProductAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: RvItemFavoritesPageBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            RvItemFavoritesPageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val binding = holder.binding

        binding.imageViewFood.loadImage(productList[position].yemek_Resim_adi)

    }

    override fun getItemCount(): Int {
        return productList.size
    }


}