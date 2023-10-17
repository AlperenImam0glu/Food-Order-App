package com.example.foodorderapp.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.example.foodorderapp.data.model.product.Yemekler
import com.example.foodorderapp.databinding.FragmentProductDetailPageBinding
import com.example.foodorderapp.utils.loadImage


class ProductDetailPageFragment : Fragment() {


    private lateinit var binding: FragmentProductDetailPageBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProductDetailPageBinding.inflate(layoutInflater)


        val bundle: ProductDetailPageFragmentArgs by navArgs()
        val yemek = bundle.yemek
        binding.textViewCartCount.text = yemek.yemek_siparis_adet.toString()


        binding.textViewProductName.text = yemek.yemek_adi

        yemek.yemek_resim_adi?.let { binding.imageView.loadImage(it) }


        binding.buttonAdd.setOnClickListener {
            var count = binding.textViewCartCount.text.toString().toInt()+1
            binding.textViewCartCount.text = "$count"

        }

          binding.buttonMinus.setOnClickListener {
            if ( binding.textViewCartCount.text.toString().toInt() > 0) {
                var count = binding.textViewCartCount.text.toString().toInt()-1
                binding.textViewCartCount.text = "$count"
            }
        }

        return binding.root

    }

}