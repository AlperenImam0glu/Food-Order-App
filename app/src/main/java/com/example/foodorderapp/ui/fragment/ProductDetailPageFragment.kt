package com.example.foodorderapp.ui.fragment

import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.foodorderapp.R
import com.example.foodorderapp.data.model.cart.Cart
import com.example.foodorderapp.databinding.FragmentProductDetailPageBinding
import com.example.foodorderapp.ui.viewmodel.ProductDetailPageViewModel
import com.example.foodorderapp.utils.loadImage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductDetailPageFragment : Fragment() {


    private lateinit var binding: FragmentProductDetailPageBinding
    private lateinit var viewModel: ProductDetailPageViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProductDetailPageBinding.inflate(layoutInflater)
        val tempViewModel: ProductDetailPageViewModel by viewModels()
        viewModel = tempViewModel
        var isFavorited = false
        var prodcutDbId = 0

        var list = emptyList<Cart>()
        val bundle: ProductDetailPageFragmentArgs by navArgs()
        val product = bundle.yemek

        binding.textViewCartCount.text = product.yemek_siparis_adet.toString()
        binding.textViewProductName.text = product.yemek_adi
        binding.textViewProductPrice.text = "${product.yemek_fiyat} ₺"


        product.yemek_resim_adi?.let { binding.imageViewFood.loadImage(it) }

        viewModel.cartListLiveData.observe(viewLifecycleOwner) {
            list = it.sepet_yemekler!!
            for (i in list) {
                Log.e("Liste özeti", i.yemek_adi)
                if (i.yemek_adi == product.yemek_adi) {
                    binding.textViewCartCount.text = i.yemek_siparis_adet.toString()
                }
            }
        }

        viewModel.databaseLiveData.observe(viewLifecycleOwner) {
            if (it != null) {
                for (i in it) {
                    if (product.yemek_id == i.yemek_id && viewModel.currentUser?.uid ?: "" == i.kullanici_id) {
                        isFavorited = true
                        prodcutDbId = i.uid
                        binding.imageViewFavorite.setBackgroundResource(R.drawable.ic_favorite_filled_red);
                    }
                }
            }
        }


        binding.imageViewFavorite.setOnClickListener {
            if (isFavorited) {
                viewModel.deleteProductInDB(prodcutDbId)
                binding.imageViewFavorite.setBackgroundResource(R.drawable.ic_favorite_filled)
                Toast.makeText(requireContext(), "Favorilerden Kaldırıldı", Toast.LENGTH_SHORT)
                    .show()
            } else {
                viewModel.addProductInDB(product)
                binding.imageViewFavorite.setBackgroundResource(R.drawable.ic_favorite_filled_red)
                Toast.makeText(requireContext(), "Favorilere Eklendi", Toast.LENGTH_SHORT).show()
            }
        }


        binding.buttonAddToCart.setOnClickListener {
            val count = binding.textViewCartCount.text.toString().toInt()

            if (count != 0) {
                product.yemek_siparis_adet = count
                viewModel.addProductToCart(product)
                binding.buttonAddToCart.text = "Sepete Ekle"
                binding.buttonAddToCart.backgroundTintList = ColorStateList.valueOf(
                    requireContext().resources.getColor(
                        R.color.button_color
                    )
                )
                Toast.makeText(requireContext(), "Sepete Eklendi", Toast.LENGTH_SHORT).show()
            } else if (count == 0 && binding.buttonAddToCart.text == "Güncelle") {
                viewModel.deleteOneProdutInCart(product)
                binding.buttonAddToCart.text = "Sepete Ekle"
                binding.buttonAddToCart.backgroundTintList = ColorStateList.valueOf(
                    requireContext().resources.getColor(
                        R.color.button_color
                    )
                )
                Toast.makeText(requireContext(), "Ürün sepetten silindi", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Miktar Seçiniz", Toast.LENGTH_SHORT).show()
                binding.buttonAddToCart.backgroundTintList = ColorStateList.valueOf(
                    requireContext().resources.getColor(
                        R.color.custom_red
                    )
                )
            }
        }

        binding.buttonAdd.setOnClickListener {
            val count = binding.textViewCartCount.text.toString().toInt() + 1
            if (product.yemek_siparis_adet != 0) {
                binding.buttonAddToCart.text = "Güncelle"
            } else {
                binding.buttonAddToCart.text = "Sepete Ekle"
            }
            binding.textViewCartCount.text = "$count"
            binding.buttonAddToCart.backgroundTintList = ColorStateList.valueOf(
                requireContext().resources.getColor(
                    R.color.custom_red
                )
            )
        }

        binding.buttonMinus.setOnClickListener {
            if (binding.textViewCartCount.text.toString().toInt() > 0) {
                val count = binding.textViewCartCount.text.toString().toInt() - 1
                binding.textViewCartCount.text = "$count"
                if (product.yemek_siparis_adet != 0) {
                    binding.buttonAddToCart.text = "Güncelle"
                } else {
                    binding.buttonAddToCart.text = "Sepete Ekle"
                }
                binding.buttonAddToCart.backgroundTintList = ColorStateList.valueOf(
                    requireContext().resources.getColor(R.color.custom_red)
                )
            } else {
                Toast.makeText(requireContext(), "Miktar daha fazla azalamaz", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        return binding.root
    }


}