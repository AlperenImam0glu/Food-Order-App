package com.example.foodorderapp.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.foodorderapp.data.model.cart.Cart
import com.example.foodorderapp.data.model.product.Yemekler
import com.example.foodorderapp.databinding.CartPageProductItemBinding
import com.example.foodorderapp.ui.viewmodel.OrderPageViewModel
import com.example.foodorderapp.utils.loadImage

class OrderPageProductAdapter(var productList: List<Cart>, val viewModel: OrderPageViewModel,val mContext: Context) :
    RecyclerView.Adapter<OrderPageProductAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: CartPageProductItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            CartPageProductItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val binding = holder.binding
        var food = productList[position]

        binding.textViewProductName.text = food.yemek_adi
        binding.textViewPrice.text = "${food.yemek_fiyat.toString().toInt() * food.yemek_siparis_adet} ₺"
        food.yemek_resim_adi?.let { binding.imageViewFood.loadImage(it) }
        binding.textViewCartCount.text = food.yemek_siparis_adet.toString()

        binding.buttonAdd.setOnClickListener {
            val count = binding.textViewCartCount.text.toString().toInt() + 1
            binding.textViewCartCount.text = "$count"
        }

        binding.buttonNimus.setOnClickListener {
            if (binding.textViewCartCount.text.toString().toInt() > 0) {
                val count = binding.textViewCartCount.text.toString().toInt() - 1
                binding.textViewCartCount.text = "$count"
            }
        }

        binding.imageViewDelete.setOnClickListener {
            viewModel.deleteProductInCart(food.sepet_yemek_id)
            Toast.makeText(mContext,"Ürün silindi",Toast.LENGTH_SHORT).show()
            notifyDataSetChanged()
        }

    }

    override fun getItemCount(): Int {
        return productList.size
    }


}