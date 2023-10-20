package com.example.foodorderapp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.foodorderapp.data.model.CRUDResponce
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

                var responce = CRUDResponce("",0)
                currentUser?.let { responce = productRepository.addProductToCart(product, it.uid) }
                Log.e("viewmodel sepet veri ekleme durumu", "${responce.success} - ${responce.message}")
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

                currentUser?.let {
                    val responce = productRepository.getProductInCart(it.uid)
                    cartListFlow.value = responce
                    Log.e("viewmodel sepet veri ekleme durumu", "başarı kodu ${responce.success}")
                }

            } catch (e: Exception) {

                Log.e("hata silme", "veri getirme hastası ${e.message}")
                Log.e("hata silme", "veri güncelleme önce  ${cartListFlow.value}")
                cartListFlow.value= null
                Log.e("hata silme", "veri güncelleme sonra  ${cartListFlow.value}")
            }
        }
    }

    fun deleteProductInCart(yemek_id: Int) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                currentUser?.let {
                    val responce =   productRepository.deleteProductInCart(yemek_id, it.uid)
                    Log.e("viewmodel sepet veri ekleme durumu", "başarı kodu ${responce.success}")
                }
                getProductInCart()
            } catch (e: Exception) {
                Log.e("hata silme", " silinme hatası ${e.message}")
            }
        }
    }

    fun deleteAllProductInCart() {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                cartListFlow.value?.let {
                    for (i in it.sepet_yemekler!!) {
                        currentUser?.let {
                            val responce =   productRepository.deleteProductInCart(i.sepet_yemek_id, it.uid)
                            Log.e("viewmodel sepet veri ekleme durumu", "başarı kodu ${responce.success}")
                        }
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