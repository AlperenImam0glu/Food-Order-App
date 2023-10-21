package com.example.foodorderapp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
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
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class OrderPageViewModel @Inject constructor(
    private val repository: AuthRepository, private val productRepository: ProductRepository
) : ViewModel() {

    val cartListFlow = MutableStateFlow<CartResponce?>(null)
    val privateCartListFlow = MutableStateFlow<CartResponce?>(null)
    val currentUser: FirebaseUser?
        get() = repository.currentUser

    init {
        getProductInCart()
    }

    fun addProductToCart(product: Yemekler) {

        getProductsInCartprivate()

        var list = mutableListOf<Cart>()

        if (privateCartListFlow.value == null) {
            addNewItemCart(product)
        } else {
            var include = false
            privateCartListFlow.value?.let {
                list = it.sepet_yemekler as MutableList<Cart>
            }
            list?.let {
                for (i in 0..list.size - 1) {
                    if (list[i].yemek_adi == product.yemek_adi) {
                        list[i].yemek_siparis_adet = product.yemek_siparis_adet
                        include = true
                    }
                }
            }
            if (include) {
                deleteListInCart(list)
                addNewListInCart(list)
            } else {
                deleteListInCart(list)
                val cartObject = Cart(
                    0,
                    product.yemek_adi!!,
                    product.yemek_resim_adi!!,
                    product.yemek_fiyat!!,
                    product.yemek_siparis_adet,
                    ""
                )
                list.add(cartObject)
                addNewListInCart(list)
            }
        }
    }

    fun getProductsInCartprivate() {

        runBlocking {
            try {
                currentUser?.let {
                    val responce = productRepository.getProductInCart(it.uid)
                    privateCartListFlow.value = responce
                    Log.e("viewmodel sepet veri ekleme durumu", "başarı kodu ${responce.success}")
                }


            } catch (e: Exception) {
                Log.e("viewmodel sepet verileri hata", "sepet verileri alma hata: ${e.message}")
            }
        }
        CoroutineScope(Dispatchers.Main).launch {

        }
    }

    fun addNewItemCart(product: Yemekler) {
        runBlocking {
            try {
                var responce = CRUDResponce("", 0)
                currentUser?.let { responce = productRepository.addProductToCart(product, it.uid) }
                Log.e(
                    "viewmodel sepet veri ekleme durumu",
                    "${responce.success} - ${responce.message}"
                )
            } catch (e: Exception) {
                Log.e("viewmodel veri ekleme hata", "sepet veri ekleme hata  : ${e.message}")
            }
        }
    }

    fun deleteListInCart(list: List<Cart>) {
        runBlocking {
            try {
                list?.let {
                    for (i in 0..it.size - 1) {
                        currentUser?.let {
                            val responce = productRepository.deleteProductInCart(
                                list[i].sepet_yemek_id,
                                it.uid
                            )
                            Log.e(
                                "viewmodel sepet veri ekleme durumu",
                                "başarı kodu ${responce.success}"
                            )
                        }
                    }
                    Log.e("viewmodel sepet silme hata", "5 sepet silme tamamlandı")
                }
            } catch (e: Exception) {
                Log.e("viewmodel sepet silme hata", "sepet silme hata responce : ${e.message}")
            }
        }
    }


    fun addNewListInCart(list: List<Cart>) {
        runBlocking {
            try {
                list?.let {
                    for (i in 0..it.size - 1) {
                        val product = converCartObjectToProductObject(list[i])
                        var responce = CRUDResponce("", 0)
                        currentUser?.let {
                            responce = productRepository.addProductToCart(product, it.uid)
                        }
                        Log.e(
                            "viewmodel sepet veri ekleme durumu",
                            "${responce.success} - ${responce.message}"
                        )
                    }
                    Log.e("viewmodel sepet veri ekleme", "7 liste veri ekleme tamamlandı")
                }
            } catch (e: Exception) {
                Log.e("viewmodel veri ekleme hata", "sepet veri ekleme hata  : ${e.message}")
            }
        }
    }

    fun converCartObjectToProductObject(cartObject: Cart): Yemekler {
        val productObject = Yemekler(
            cartObject.yemek_adi,
            cartObject.yemek_fiyat,
            "",
            cartObject.yemek_resim_adi,
            cartObject.yemek_siparis_adet
        )
        return productObject
    }


    //alperen_deneme
    fun getProductInCart() {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                currentUser?.let {
                    val responce = productRepository.getProductInCart(it.uid)
                    cartListFlow.value = responce
                    Log.e("hesaplama", "başarı kodu ${responce.success}")
                }
            } catch (e: Exception) {
                cartListFlow.value = null
                Log.e("hesaplama", "veri güncelleme hata ")
            }
        }
    }

    fun deleteProductInCart(yemek_id: Int) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                currentUser?.let {
                    val responce = productRepository.deleteProductInCart(yemek_id, it.uid)
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
                            val responce =
                                productRepository.deleteProductInCart(i.sepet_yemek_id, it.uid)
                            Log.e(
                                "viewmodel sepet veri ekleme durumu",
                                "başarı kodu ${responce.success}"
                            )
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