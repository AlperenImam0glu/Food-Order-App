package com.example.foodorderapp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.foodorderapp.data.model.cart.CartResponce
import com.example.foodorderapp.data.model.product.Yemekler
import com.example.foodorderapp.data.repository.AuthRepository
import com.example.foodorderapp.data.repository.ProductRepository
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailPageViewModel @Inject constructor(
    private val repository: AuthRepository, private val productRepository: ProductRepository
) : ViewModel() {


    val cartListLiveData = MutableLiveData<CartResponce>()
    val currentUser: FirebaseUser?
        get() = repository.currentUser

    init {
        getProductInCart()
    }


    fun addProductToCart( product: Yemekler) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
               product.yemek_siparis_adet=1
                val responce = productRepository.addProductToCart(product, "alperen_deneme")
                Log.e(
                    "gelen cevap",
                    "${responce.success.toString()} - ${responce.message.toString()}"
                )
            } catch (e: Exception) {
                Log.e("hata", e.toString())
            }
        }
    }

    //alperen_deneme
    fun getProductInCart() {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val responce = productRepository.getProductInCart("alperen_deneme")
                Log.e(
                    "gelen cevap",
                    "${responce.success.toString()} - ${responce.sepet_yemekler.toString()}"
                )
                cartListLiveData.value = responce
            } catch (e: Exception) {
                Log.e("hata", e.toString())
            }
        }
    }

    fun deleteProductInCart(yemek_id: Int) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val responce = productRepository.deleteProductInCart(yemek_id,"alperen_deneme")
                Log.e(
                    "gelen cevap",
                    "${responce.success.toString()} - ${responce.message.toString()}"
                )
            } catch (e: Exception) {
                Log.e("hata", e.toString())
            }
        }
    }


}