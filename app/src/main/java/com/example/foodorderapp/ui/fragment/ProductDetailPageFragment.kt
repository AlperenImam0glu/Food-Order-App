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

        var list = emptyList<Cart>()
        val bundle: ProductDetailPageFragmentArgs by navArgs()
        val yemek = bundle.yemek

        binding.textViewCartCount.text = yemek.yemek_siparis_adet.toString()
        binding.textViewProductName.text = yemek.yemek_adi
        binding.textViewProductPrice.text = "${yemek.yemek_fiyat} ₺"

        yemek.yemek_resim_adi?.let { binding.imageView.loadImage(it) }

        viewModel.cartListLiveData.observe(viewLifecycleOwner) {
            list = it.sepet_yemekler!!
            for (i in list) {
                Log.e("Liste özeti", i.yemek_adi)
                if (i.yemek_adi == yemek.yemek_adi) {
                    binding.textViewCartCount.text = i.yemek_siparis_adet.toString()
                }
            }
        }

        binding.buttonAddToCart.setOnClickListener {
            val count = binding.textViewCartCount.text.toString().toInt()

            if (count != 0) {
                yemek.yemek_siparis_adet = count
                viewModel.addProductToCart(yemek)
                binding.buttonAddToCart.text="Sepete Ekle"
                binding.buttonAddToCart.backgroundTintList = ColorStateList.valueOf(
                    requireContext().resources.getColor(
                        R.color.button_color
                    )
                )
                Toast.makeText(requireContext(), "Sepete Eklendi", Toast.LENGTH_SHORT).show()
            } else if (count == 0 && binding.buttonAddToCart.text == "Güncelle") {
                viewModel.deleteOneProdutInCart(yemek)
                binding.buttonAddToCart.text="Sepete Ekle"
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
            binding.buttonAddToCart.text="Güncelle"
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
                binding.buttonAddToCart.text="Güncelle"
                binding.buttonAddToCart.backgroundTintList = ColorStateList.valueOf(
                    requireContext().resources.getColor(R.color.custom_red))
            } else {
                Toast.makeText(requireContext(), "Miktar daha fazla azalamaz", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        return binding.root
    }


}