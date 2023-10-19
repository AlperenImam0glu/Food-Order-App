package com.example.foodorderapp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.foodorderapp.data.model.cart.Cart
import com.example.foodorderapp.data.model.cart.CartResponce
import com.example.foodorderapp.data.model.product.Yemekler
import com.example.foodorderapp.data.repository.AuthRepository
import com.example.foodorderapp.data.repository.ProductRepository
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderPageViewModel @Inject constructor(
    private val repository: AuthRepository, private val productRepository: ProductRepository
) : ViewModel() {

    val cartListFlow = MutableStateFlow<CartResponce?>(null)
    val currentUser: FirebaseUser?
        get() = repository.currentUser


    init {
        getProductInCart()
    }


    fun addProductToCart(product: Yemekler) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
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
                cartListFlow.value = responce
            } catch (e: Exception) {
                Log.e("hata", e.toString())
            }
        }
    }

    fun deleteProductInCart(yemek_id: Int) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val responce = productRepository.deleteProductInCart(yemek_id, "alperen_deneme")
                getProductInCart()
            } catch (e: Exception) {
                Log.e("hata", e.toString())
            }
        }
    }

    fun deleteAllProductInCart() {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                cartListFlow.value?.let {
                    for (i in it.sepet_yemekler!!) {
                        val responce = productRepository.deleteProductInCart(
                            i.sepet_yemek_id,
                            "alperen_deneme"
                        )
                    }
                }
                getProductInCart()
            } catch (e: Exception) {
                Log.e("hata", e.toString())
            }
        }
    }

    fun calculateCartTotalPrice(list: List<Cart>): Int {
        var totalPrice = 0
        for (i in list) {
            totalPrice += i.yemek_siparis_adet * i.yemek_fiyat.toInt()
        }
        return totalPrice
    }


}