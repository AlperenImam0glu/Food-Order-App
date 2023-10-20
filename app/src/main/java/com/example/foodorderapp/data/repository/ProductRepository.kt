package com.example.foodorderapp.data.repository

import com.example.foodorderapp.data.datasource.LocalDataSource
import com.example.foodorderapp.data.datasource.RemoteDataSource
import com.example.foodorderapp.data.model.CRUDResponce
import com.example.foodorderapp.data.model.cart.CartResponce
import com.example.foodorderapp.data.model.databasemodel.DataBaseProductModel
import com.example.foodorderapp.data.model.product.FoodResponce
import com.example.foodorderapp.data.model.product.Yemekler

interface ProductRepository {
    val remoteDataSource: RemoteDataSource
    val localDataSource: LocalDataSource
    suspend fun getAllProducts(): List<Yemekler>

    // suspend fun getProductsInCart()
    suspend fun addProductToCart(product: Yemekler, userName: String): CRUDResponce

    suspend fun getProductInCart(userName: String): CartResponce

    suspend fun deleteProductInCart(yemek_id: Int,userName: String): CRUDResponce

    suspend fun getAllProductInDB() : List<DataBaseProductModel>

    suspend fun deleteProductInDB(product_id:Int)

    suspend fun saveProdcutInDB(product:Yemekler,userId:String)

    // suspend fun removeProductFromCart()
    //  suspend fun removeAllProductFromCart()
}