package com.example.foodorderapp.data.repository

import com.example.foodorderapp.data.model.Resource
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore


interface AuthRepository {
    val currentUser:FirebaseUser?
    val firestore:FirebaseFirestore
    suspend fun login(email:String, password:String): Resource<FirebaseUser>
    suspend fun signup(name:String,email:String, password:String,location:String): Resource<FirebaseUser>
    suspend fun logout()
}