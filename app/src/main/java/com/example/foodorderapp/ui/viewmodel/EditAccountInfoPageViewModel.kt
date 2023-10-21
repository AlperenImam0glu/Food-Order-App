package com.example.foodorderapp.ui.viewmodel


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodorderapp.data.model.user.User
import com.example.foodorderapp.data.repository.AuthRepository
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditAccountInfoPageViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {
    val userInfoFlow = MutableStateFlow<User?>(null)
    val db = FirebaseFirestore.getInstance()
    val currentUser: FirebaseUser?
        get() = repository.currentUser
    init {
        getData()
    }

    fun getData() {
        viewModelScope.launch {
            val docRef = db.collection("users").document(currentUser!!.uid)
            docRef.get()
                .addOnSuccessListener { document ->
                    if (document != null) {
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

    fun changeLocationInFirabase(newLocation: String) {
        val docRef = db.collection("users").document(currentUser!!.uid)
        val map = HashMap<String, Any>().apply {
            put("name", userInfoFlow.value!!.name!!)
            put("location", newLocation)
            put("email", userInfoFlow.value!!.email!!)
        }
        docRef.update(map)
            .addOnSuccessListener {
                Log.d("Güncelleme Başarılı", "Belge başarıyla güncellendi.")
            }
            .addOnFailureListener { exception ->
                Log.w("Güncelleme Hatası", "Belge güncellenemedi", exception)
            }

    }

    fun logOut() = viewModelScope.launch {
        repository.logout()
    }
}