package com.example.foodorderapp.ui.viewmodel

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodorderapp.data.model.CRUDResponce
import com.example.foodorderapp.data.model.Resource
import com.example.foodorderapp.data.model.cart.Cart
import com.example.foodorderapp.data.model.cart.CartResponce
import com.example.foodorderapp.data.model.product.Yemekler
import com.example.foodorderapp.data.model.user.User
import com.example.foodorderapp.data.repository.AuthRepository
import com.example.foodorderapp.data.repository.ProductRepository
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class MainPageViewModel @Inject constructor(
    private val repository: AuthRepository, private val productRepository: ProductRepository
) : ViewModel() {

    private val _loginFlow = MutableStateFlow<Resource<FirebaseUser>?>(null)
    val loginFlow: StateFlow<Resource<FirebaseUser>?> = _loginFlow
    val userInfoFlow = MutableStateFlow<User?>(null)
    val db = FirebaseFirestore.getInstance()
    var productLiveData = MutableLiveData<List<Yemekler>?>()
    val productListFlow = MutableStateFlow<List<Yemekler>?>(null)
    val cartListFlow = MutableStateFlow<CartResponce?>(null)
    val privateCartListFlow = MutableStateFlow<CartResponce?>(null)
    val progressFlow = MutableLiveData<Boolean>(false)

    val combineFlow = productListFlow.combine(cartListFlow) { flow1Value, flow2Value ->
    }

    val currentUser: FirebaseUser?
        get() = repository.currentUser

    init {
        if (repository.currentUser != null) {
            _loginFlow.value = Resource.Success(repository.currentUser!!)
        }
        getAllProduct()
        getProductsInCart()
    }


    fun getData() {
        viewModelScope.launch {
            val docRef = db.collection("users").document(currentUser!!.uid)
            docRef.get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        Log.d("viewmodel kullanıcı bilgileri alma başarılı", "${document.data}")
                        val inputMap = document.data
                        val userData = User(
                            name = inputMap?.get("name").toString(),
                            location = inputMap?.get("location").toString(),
                            email = inputMap?.get("email").toString()
                        )
                        userInfoFlow.value =
                            User(userData.name!!, userData.email!!, userData.location!!)
                    } else {
                        Log.d("viewmodel kullanıcı bilgileri alma", "No such document")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d("viewmodel kullanıcı bilgileri alma hatta", "get failed with ", exception)
                }

        }

    }

    fun deleteOneProductInCartWithId(yemek_sepet_id: Int) {
        CoroutineScope(Dispatchers.Main).launch {
            try {

                currentUser?.let {
                    val responce =   productRepository.deleteProductInCart(yemek_sepet_id, it.uid)
                    Log.e("viewmodel sepet veri ekleme durumu", "başarı kodu ${responce.success}")
                }

            } catch (e: Exception) {
                Log.e("deleteOneProductInCart", e.toString())
            }
        }
    }


    fun getAllProduct() {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val responce = productRepository.getAllProducts()
                productListFlow.value = responce
                Log.e(
                    "viewmodel ürün verisi alma",
                    "ürün verisi alma veri adedi : ${responce.size}"
                )
            } catch (e: Exception) {
                Log.e("viewmodel ürün verisi alma hata", "ürün verisi alma hata : ${e.message}")
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

    fun deleteListInCart(list: List<Cart>) {
        runBlocking {
            try {
                list?.let {
                    for (i in 0..it.size - 1) {
                        currentUser?.let {
                            val responce =   productRepository.deleteProductInCart(list[i].sepet_yemek_id, it.uid)
                            Log.e("viewmodel sepet veri ekleme durumu", "başarı kodu ${responce.success}")
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
            } catch (e: Exception) {
                Log.e("viewmodel sepet silme hata", "sepet silme hata responce : ${e.message}")
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

    fun getProductsInCart() {

        CoroutineScope(Dispatchers.Main).launch {
            try {
                currentUser?.let {
                    val responce = productRepository.getProductInCart(it.uid)
                    cartListFlow.value = responce
                    Log.e("viewmodel sepet veri ekleme durumu", "başarı kodu ${responce.success}")
                }
            } catch (e: Exception) {
                Log.e("viewmodel sepet verileri hata", "sepet verileri alma hata: ${e.message}")
            }
        }
        CoroutineScope(Dispatchers.Main).launch {

        }
    }

    fun mergeFlowsData(): List<Yemekler> {
        var productList = productListFlow.value
        var cartList = cartListFlow.value?.sepet_yemekler

        try {
            if (productList != null && cartList != null) {
                for (i in productList.indices) {
                    for (j in cartList.indices) {
                        if (productList[i].yemek_adi == cartList[j].yemek_adi) {
                            Log.e(
                                "hataya düştü liste --",
                                "${productList[i].yemek_adi} - ${cartList[j].yemek_adi}"
                            )
                            productList[i].yemek_siparis_adet = cartList[j].yemek_siparis_adet
                        }
                    }
                }
            }
            productListFlow.value = productList
        } catch (e: Exception) {

        }
        return productList ?: ArrayList<Yemekler>()
    }

    fun saveProductInDB(prodcut: Yemekler) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                currentUser?.let { productRepository.saveProdcutInDB(prodcut, it.uid) }
            } catch (e: Exception) {
                Log.e("veri tabanı hata", e.toString())
            }
        }
    }

}