package com.example.foodorderapp.ui.viewmodel


import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.foodorderapp.data.model.cart.Cart
import com.example.foodorderapp.data.model.cart.CartResponce
import com.example.foodorderapp.data.model.databasemodel.DataBaseProductModel
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
class FavoritePageViewModel @Inject constructor(
    private val repository: AuthRepository, private val productRepository: ProductRepository
) : ViewModel() {

    val cartListLiveData = MutableLiveData<List<DataBaseProductModel>?>()


    val currentUser: FirebaseUser?
        get() = repository.currentUser
    init {
        getProductInDB()
    }



    fun getProductInDB() {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                cartListLiveData.value = productRepository.getAllProductInDB()
            } catch (e: Exception) {
                Log.e("veri tabanı hata", e.toString())
            }
        }
    }

    fun deleteProductInDB(prodcutID: Int) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                productRepository.deleteProductInDB(prodcutID)
                getProductInDB()
            } catch (e: Exception) {
                Log.e("veri tabanı hata", e.toString())
            }
        }
    }




}