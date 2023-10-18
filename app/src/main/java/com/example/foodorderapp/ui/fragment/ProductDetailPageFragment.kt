package com.example.foodorderapp.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.foodorderapp.data.model.cart.Cart
import com.example.foodorderapp.data.model.cart.CartResponce
import com.example.foodorderapp.databinding.FragmentProductDetailPageBinding
import com.example.foodorderapp.ui.viewmodel.ProductPageViewModel
import com.example.foodorderapp.utils.loadImage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductDetailPageFragment : Fragment() {


    private lateinit var binding: FragmentProductDetailPageBinding
    private lateinit var viewModel: ProductPageViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProductDetailPageBinding.inflate(layoutInflater)
        val tempViewModel: ProductPageViewModel by viewModels()
        viewModel = tempViewModel

        var list = emptyList<Cart>()
        val bundle: ProductDetailPageFragmentArgs by navArgs()
        val yemek = bundle.yemek

        binding.textViewCartCount.text = yemek.yemek_siparis_adet.toString()
        binding.textViewProductName.text = yemek.yemek_adi
        yemek.yemek_resim_adi?.let { binding.imageView.loadImage(it) }


        binding.buttonDeneme.setOnClickListener {
            viewModel.addProductToCart(yemek)
        }

        binding.buttonGetCart.setOnClickListener {
            viewModel.getProductInCart()
        }

        viewModel.cartListLiveData.observe(viewLifecycleOwner) {
            list = it.sepet_yemekler
            Log.e("gelen cevaplar", "liste geldi")
        }

        binding.buttonDelete.setOnClickListener {

            val hedefIcekIsim = yemek.yemek_adi

            val bulunanIcecek = list.find { it.yemek_adi == hedefIcekIsim }

            Log.e("gelen cevaplar", "${bulunanIcecek!!.sepet_yemek_id.toString()}-")
            viewModel.deleteProductInCart(bulunanIcecek!!.sepet_yemek_id)
        }


        binding.buttonAdd.setOnClickListener {
            var count = binding.textViewCartCount.text.toString().toInt() + 1
            binding.textViewCartCount.text = "$count"
        }

        binding.buttonMinus.setOnClickListener {
            if (binding.textViewCartCount.text.toString().toInt() > 0) {
                var count = binding.textViewCartCount.text.toString().toInt() - 1
                binding.textViewCartCount.text = "$count"
            }
        }
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

}