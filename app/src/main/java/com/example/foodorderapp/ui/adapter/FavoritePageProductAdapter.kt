package com.example.foodorderapp.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.foodorderapp.data.model.databasemodel.DataBaseProductModel
import com.example.foodorderapp.data.model.product.Yemekler
import com.example.foodorderapp.databinding.RvItemFavoritesPageBinding
import com.example.foodorderapp.ui.fragment.FavoritesPageFragmentDirections
import com.example.foodorderapp.ui.viewmodel.FavoritePageViewModel
import com.example.foodorderapp.utils.loadImage

class FavoritePageProductAdapter(
    var productList: List<DataBaseProductModel>,
    val viewModel: FavoritePageViewModel,
    val mContext: Context
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
        val product = productList[position]
        binding.imageViewFood.loadImage(productList[position].yemek_resim_adi)
        binding.textViewFoodName.text = productList[position].yemek_adi

        binding.cardView.setOnClickListener {
            val product = Yemekler(
                product.yemek_adi,
                product.yemek_fiyat,
                product.yemek_id,
                product.yemek_resim_adi,
                product.yemek_siparis_adet
            )
            val action = FavoritesPageFragmentDirections.navigateToDetailPage(product)
            Navigation.findNavController(it).navigate(action)
        }

        binding.imageViewLike.setOnClickListener {
            viewModel.deleteProductInDB(product.uid)
            Toast.makeText(mContext, "Favorilerden Kaldırıldı", Toast.LENGTH_SHORT).show()
        }

    }

    override fun getItemCount(): Int {
        return productList.size
    }


}