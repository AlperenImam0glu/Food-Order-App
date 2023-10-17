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
        val yemek = Yemekler(bundle.yemek.yemek_adi,bundle.yemek.yemek_fiyat,bundle.yemek.yemek_id,bundle.yemek.yemek_resim_adi)



        binding.textViewProductName.text = yemek.yemek_adi
        yemek.yemek_resim_adi?.let { binding.imageView.loadImage(it) }


        binding.buttonAdd.setOnClickListener {
            yemek.yemek_siparis_adet = yemek.yemek_siparis_adet + 1
            binding.textViewCartCount.text = yemek.yemek_siparis_adet.toString()

        }

        binding.buttonMinus.setOnClickListener {
            if(yemek.yemek_siparis_adet>0){
                yemek.yemek_siparis_adet = yemek.yemek_siparis_adet - 1
                binding.textViewCartCount.text = yemek.yemek_siparis_adet.toString()
            }
        }

        return binding.root

    }


}