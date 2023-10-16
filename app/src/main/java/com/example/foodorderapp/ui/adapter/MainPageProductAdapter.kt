package com.example.foodorderapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodorderapp.data.model.product.Yemekler
import com.example.foodorderapp.databinding.MainpageProductItemBinding
import com.example.foodorderapp.utils.loadImage

class MainPageProductAdapter(val productList: List<Yemekler>) :
    RecyclerView.Adapter<MainPageProductAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: MainpageProductItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            MainpageProductItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val binding = holder.binding
        val food = productList[position]
        binding.textViewFoodName.text = food.yemek_adi
        binding.textView2.text = "${food.yemek_fiyat} ₺"
        binding.imageViewFood.loadImage(food.yemek_resim_adi)

    }

    override fun getItemCount(): Int {
        return productList.size
    }


}