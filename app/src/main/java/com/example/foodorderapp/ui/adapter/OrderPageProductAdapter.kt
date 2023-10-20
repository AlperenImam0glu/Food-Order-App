package com.example.foodorderapp.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.foodorderapp.data.model.cart.Cart
import com.example.foodorderapp.data.model.product.Yemekler
import com.example.foodorderapp.databinding.RvItemOrderPageBinding
import com.example.foodorderapp.ui.fragment.OrderPageFragmentDirections
import com.example.foodorderapp.ui.viewmodel.OrderPageViewModel
import com.example.foodorderapp.utils.loadImage
import com.example.foodorderapp.utils.safeNav

class OrderPageProductAdapter(
    var productList: List<Cart>,
    val viewModel: OrderPageViewModel,
    val mContext: Context
) :
    RecyclerView.Adapter<OrderPageProductAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: RvItemOrderPageBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            RvItemOrderPageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val binding = holder.binding
        var product = productList[position]

        binding.textViewProductName.text = product.yemek_adi
        binding.textViewPrice.text =
            "${product.yemek_fiyat.toString().toInt() * product.yemek_siparis_adet} ₺"
        product.yemek_resim_adi?.let { binding.imageViewFood.loadImage(it) }
        binding.textViewCartCount.text = product.yemek_siparis_adet.toString()

        val currentCount = product.yemek_siparis_adet

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
            viewModel.deleteProductInCart(product.sepet_yemek_id)
            Toast.makeText(mContext, "Ürün silindi", Toast.LENGTH_SHORT).show()
            notifyDataSetChanged()
        }

        binding.cardView.setOnClickListener {
            val action =
                OrderPageFragmentDirections.orderPageToDetailPage(cartToYemeklerObject(product))
            Navigation.safeNav(it, action)
        }

    }

    override fun getItemCount(): Int {
        return productList.size
    }

    fun cartToYemeklerObject(data: Cart): Yemekler {
        val product = Yemekler(
            data.yemek_adi,
            data.yemek_fiyat,
            "",
            data.yemek_resim_adi,
            data.yemek_siparis_adet
        )
        return product
    }


}