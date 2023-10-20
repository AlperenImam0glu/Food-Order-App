package com.example.foodorderapp.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.provider.CalendarContract.Colors
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.foodorderapp.R
import com.example.foodorderapp.data.model.product.Yemekler
import com.example.foodorderapp.databinding.MainpageProductItemBinding
import com.example.foodorderapp.ui.fragment.MainPageFragmentDirections
import com.example.foodorderapp.ui.viewmodel.MainPageViewModel
import com.example.foodorderapp.utils.loadImage

class MainPageProductAdapter(
    var productList: List<Yemekler>,
    val viewModel: MainPageViewModel,
    val mContext: Context
) :
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

        val checkCount = binding.textViewCartCount.text.toString().toInt()
        binding.textViewFoodName.text = food.yemek_adi
        binding.textViewFoodPrice.text = "${food.yemek_fiyat} ₺"


        food.yemek_resim_adi?.let { binding.imageViewFood.loadImage(it) }

        /*  Log.e("güncelleme hata","${food.yemek_id},${food.yemek_adi}")
          if (food.yemek_siparis_adet != 0) {
              Log.e("güncelleme hata","Güncellendi ${food.yemek_id},${food.yemek_adi}")
              binding.buttonSepeteEkle.text = "Güncelle"
          }*/

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
            binding.buttonSepeteEkle.text = "Güncelle"
            binding.buttonSepeteEkle.backgroundTintList =
                ColorStateList.valueOf(mContext.resources.getColor(R.color.custom_red))

        }

        binding.buttonSepeteEkle.setOnClickListener {
            val count = binding.textViewCartCount.text.toString().toInt()

            if (count != 0) {
                food.yemek_siparis_adet = count
                viewModel.addProductToCart(food)
                binding.buttonSepeteEkle.text = "Sepete Ekle"
                Toast.makeText(mContext, "Sepete Eklendi", Toast.LENGTH_SHORT).show()
                binding.buttonSepeteEkle.backgroundTintList =
                    ColorStateList.valueOf(mContext.resources.getColor(R.color.button_color))

            } else if (count == 0 && binding.buttonSepeteEkle.text == "Güncelle") {
                viewModel.deleteOneProdutInCart(food)
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
                binding.buttonSepeteEkle.text = "Güncelle"
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