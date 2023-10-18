package com.example.foodorderapp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodorderapp.data.model.Resource
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
import kotlinx.coroutines.launch
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
    val productFlow =  MutableStateFlow<List<Yemekler>?>(null)


    val currentUser: FirebaseUser?
        get() = repository.currentUser

    init {
        if(repository.currentUser !=null){
            _loginFlow.value = Resource.Success(repository.currentUser!!)
        }
        getAllProduct()
    }



    fun getData(){

        viewModelScope.launch {
            val docRef = db.collection("users").document(currentUser!!.uid)
            docRef.get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        Log.d("gelen veri", "${document.data}")
                        val inputMap = document.data
                        val userData = User(
                            name = inputMap?.get("name").toString(),
                            location = inputMap?.get("location").toString(),
                            email = inputMap?.get("email").toString()
                        )
                        userInfoFlow.value = User(userData.name!!,userData.email!!,userData.location!!)


                    } else {
                        Log.d("gelen veri", "No such document")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d("gelen veri", "get failed with ", exception)
                }

        }

    }

    fun getAllProduct() {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                productFlow.value = productRepository.getAllProducts()
            } catch (e: Exception) {
                    Log.e("hata",e.toString())
            }
        }
    }



}