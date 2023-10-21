package com.example.foodorderapp.ui.adapter


import android.content.Context
import android.content.res.ColorStateList
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.foodorderapp.R
import com.example.foodorderapp.data.model.databasemodel.DataBaseProductModel
import com.example.foodorderapp.data.model.product.Yemekler
import com.example.foodorderapp.databinding.RvItemMainPageBinding
import com.example.foodorderapp.ui.fragment.MainPageFragmentDirections
import com.example.foodorderapp.ui.viewmodel.MainPageViewModel
import com.example.foodorderapp.utils.loadImage

class MainPageProductAdapter(
    var productList: List<Yemekler>,
    val viewModel: MainPageViewModel,
    val mContext: Context,
    var favoriteList: List<DataBaseProductModel>
) :
    RecyclerView.Adapter<MainPageProductAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: RvItemMainPageBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            RvItemMainPageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val binding = holder.binding
        var product = productList[position]
        binding.textViewFoodName.text = product.yemek_adi
        binding.textViewFoodPrice.text = "${product.yemek_fiyat} ₺"
        product.yemek_resim_adi?.let { binding.imageViewFood.loadImage(it) }
        binding.textViewCartCount.text = product.yemek_siparis_adet.toString()

        binding.imageViewFavorite.setBackgroundResource(R.drawable.ic_favorite_filled)
        favoriteList?.let {
            for (i in favoriteList.indices) {
                if (productList[position].yemek_id == favoriteList[i].yemek_id  && viewModel.currentUser?.uid ?: "" == favoriteList[i].kullanici_id) {
                    binding.imageViewFavorite.setBackgroundResource(R.drawable.ic_favorite_filled_red)
                }
            }
        }

        var isFavorited = false
        var productDBuid = 0
        binding.imageViewFavorite.setOnClickListener {
            favoriteList?.let {
                for (i in favoriteList.indices) {
                    if (product.yemek_id == favoriteList[i].yemek_id) {
                        isFavorited = true
                        productDBuid = favoriteList[i].uid
                    }
                }
                if (isFavorited) {
                    viewModel.deleteProductInDB(productDBuid)
                    binding.imageViewFavorite.setBackgroundResource(R.drawable.ic_favorite_filled)
                    Toast.makeText(mContext, "Favorilerden Kaldırıldı", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    viewModel.addProductInDB(product)
                    binding.imageViewFavorite.setBackgroundResource(R.drawable.ic_favorite_filled_red)
                    Toast.makeText(mContext, "Favorilere Eklendi", Toast.LENGTH_SHORT)
                        .show()
                }
            }

        }


        binding.cardView.setOnClickListener {
            product?.let { food ->
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

            if (product.yemek_siparis_adet != 0) {
                binding.buttonSepeteEkle.text = "Güncelle"
            } else {
                binding.buttonSepeteEkle.text = "Sepete Ekle"
            }

            binding.buttonSepeteEkle.backgroundTintList =
                ColorStateList.valueOf(mContext.resources.getColor(R.color.custom_red))
        }

        binding.buttonSepeteEkle.setOnClickListener {
            val count = binding.textViewCartCount.text.toString().toInt()

            if (count != 0) {
                product.yemek_siparis_adet = count
                viewModel.addProductToCart(product)
                binding.buttonSepeteEkle.text = "Sepete Ekle"
                Toast.makeText(mContext, "Sepete Eklendi", Toast.LENGTH_SHORT).show()
                binding.buttonSepeteEkle.backgroundTintList =
                    ColorStateList.valueOf(mContext.resources.getColor(R.color.button_color))

            } else if (count == 0 && binding.buttonSepeteEkle.text == "Güncelle") {
                viewModel.deleteOneProdutInCart(product)
                binding.buttonSepeteEkle.text = "Ekle"
                binding.buttonSepeteEkle.text = "Sepete Ekle"
                binding.buttonSepeteEkle.backgroundTintList =
                    ColorStateList.valueOf(mContext.resources.getColor(R.color.button_color))

                Toast.makeText(mContext, "Sepetten Silindi", Toast.LENGTH_SHORT).show()
            } else {
                binding.buttonSepeteEkle.backgroundTintList =
                    ColorStateList.valueOf(mContext.resources.getColor(R.color.button_color))
                Toast.makeText(mContext, "Miktar Seçiniz", Toast.LENGTH_SHORT).show()
            }
        }

        binding.buttonNimus.setOnClickListener {
            if (binding.textViewCartCount.text.toString().toInt() > 0) {
                val count = binding.textViewCartCount.text.toString().toInt() - 1
                binding.textViewCartCount.text = "$count"
                if (product.yemek_siparis_adet != 0) {
                    binding.buttonSepeteEkle.text = "Güncelle"
                } else {
                    binding.buttonSepeteEkle.text = "Sepete Ekle"
                }
                binding.buttonSepeteEkle.backgroundTintList =
                    ColorStateList.valueOf(mContext.resources.getColor(R.color.custom_red))
            } else {
                Toast.makeText(mContext, "Miktar daha fazla azalamaz", Toast.LENGTH_SHORT).show()
            }
        }

    }

    override fun getItemCount(): Int {
        return productList.size
    }


}