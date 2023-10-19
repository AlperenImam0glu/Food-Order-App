package com.example.foodorderapp.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.foodorderapp.data.model.product.Yemekler
import com.example.foodorderapp.databinding.MainpageProductItemBinding
import com.example.foodorderapp.ui.fragment.MainPageFragmentDirections
import com.example.foodorderapp.ui.viewmodel.MainPageViewModel
import com.example.foodorderapp.utils.loadImage

class MainPageProductAdapter(var productList: List<Yemekler>, val viewModel: MainPageViewModel) :
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
        var food = productList[position]

        binding.textViewFoodName.text = food.yemek_adi
        binding.textViewFoodPrice.text = "${food.yemek_fiyat} ₺"
        food.yemek_resim_adi?.let { binding.imageViewFood.loadImage(it) }
        binding.textViewCartCount.text = food.yemek_siparis_adet.toString()

        binding.cardView.setOnClickListener {
            food?.let { food ->
                val action =
                    MainPageFragmentDirections.actionMainPageFragmentToProductDetailPageFragment(
                        food
                    )
                Navigation.findNavController(it).navigate(action)
            }
        }

        binding.buttonAdd.setOnClickListener {
            val count = binding.textViewCartCount.text.toString().toInt() + 1
            binding.textViewCartCount.text = "$count"
        }

        binding.buttonSepeteEkle.setOnClickListener {
            val count = binding.textViewCartCount.text.toString().toInt()
            food.yemek_siparis_adet=count
            viewModel.addProductToCart(food)

        }


        binding.buttonNimus.setOnClickListener {
            if (binding.textViewCartCount.text.toString().toInt() > 0) {
                val count = binding.textViewCartCount.text.toString().toInt() - 1
                binding.textViewCartCount.text = "$count"

            }
        }

    }

    override fun getItemCount(): Int {
        return productList.size
    }


}