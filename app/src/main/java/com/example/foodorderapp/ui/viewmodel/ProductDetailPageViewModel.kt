package com.example.foodorderapp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
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
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class ProductDetailPageViewModel @Inject constructor(
    private val repository: AuthRepository, private val productRepository: ProductRepository
) : ViewModel() {


    val cartListLiveData = MutableLiveData<CartResponce>()

    val privateCartListFlow = MutableStateFlow<CartResponce?>(null)
    val currentUser: FirebaseUser?
        get() = repository.currentUser

    init {
        getProductInCart()
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

    fun addNewItemCart(product: Yemekler) {
        runBlocking {
            try {
                productRepository.addProductToCart(product, "alperen_deneme")
                Log.e("viewmodel sepet veri ekleme", "7 tek veri ekleme tamamlandı")
            } catch (e: Exception) {
                Log.e("viewmodel veri ekleme hata", "sepet veri ekleme hata  : ${e.message}")
            }
        }
    }


    fun getProductsInCartprivate() {

        runBlocking {
            try {
                val responce = productRepository.getProductInCart("alperen_deneme")
                privateCartListFlow.value = responce
                Log.e(
                    "viewmodel sepet verileri",
                    "sepet verileri alma responce : ${responce.success}"
                )
            } catch (e: Exception) {
                Log.e("viewmodel sepet verileri hata", "sepet verileri alma hata: ${e.message}")
            }
        }
        CoroutineScope(Dispatchers.Main).launch {

        }
    }

    fun deleteListInCart(list: List<Cart>) {
        runBlocking {
            try {
                list?.let {
                    for (i in 0..it.size - 1) {
                        val response = productRepository.deleteProductInCart(
                            list[i].sepet_yemek_id,
                            "alperen_deneme"
                        )
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
                        productRepository.addProductToCart(product, "alperen_deneme")
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

    fun deleteOneProdutInCart(product: Yemekler) {
        getProductsInCartprivate()
        var list = mutableListOf<Cart>()

        privateCartListFlow.value?.let {
            list = it.sepet_yemekler as MutableList<Cart>
        }
        list?.let {
            for (i in 0..list.size - 1) {
                if (list[i].yemek_adi == product.yemek_adi) {
                    deleteOneProductInCartWithId(list[i].sepet_yemek_id)
                    break
                }
            }
        }

    }

    fun deleteOneProductInCartWithId(yemek_sepet_id: Int) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val responce =
                    productRepository.deleteProductInCart(yemek_sepet_id, "alperen_deneme")
                Log.e("deleteOneProductInCart", "${responce.success}")
            } catch (e: Exception) {
                Log.e("deleteOneProductInCart", e.toString())
            }
        }
    }


}