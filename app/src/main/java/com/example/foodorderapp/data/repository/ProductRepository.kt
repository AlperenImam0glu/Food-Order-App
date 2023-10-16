package com.example.foodorderapp.data.repository

import com.example.foodorderapp.data.datasource.LocalDataSource
import com.example.foodorderapp.data.datasource.RemoteDataSource
import com.example.foodorderapp.data.model.product.Yemekler

interface ProductRepository {
    val remoteDataSource: RemoteDataSource
    val localDataSource: LocalDataSource
    suspend fun getAllProducts() : List<Yemekler>
   // suspend fun getProductsInCart()
  //  suspend fun addProductToCart()
   // suspend fun removeProductFromCart()
  //  suspend fun removeAllProductFromCart()
}