package com.example.foodorderapp.data.repository

import com.example.foodorderapp.data.datasource.LocalDataSource
import com.example.foodorderapp.data.datasource.RemoteDataSource
import com.example.foodorderapp.data.model.CRUDResponce
import com.example.foodorderapp.data.model.cart.CartResponce
import com.example.foodorderapp.data.model.databasemodel.DataBaseProductModel
import com.example.foodorderapp.data.model.product.Yemekler
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    override val remoteDataSource: RemoteDataSource, override val localDataSource: LocalDataSource
) : ProductRepository {

    override suspend fun getAllProducts(): List<Yemekler> = remoteDataSource.getAllProduct()
    override suspend fun addProductToCart(product: Yemekler, userName: String): CRUDResponce =
        remoteDataSource.addProductToCart(product, userName)

    override suspend fun getProductInCart(userName: String): CartResponce =
        remoteDataSource.getProductInCart(userName)

    override suspend fun deleteProductInCart(yemek_id: Int, userName: String): CRUDResponce =
        remoteDataSource.deleteProductInCart(yemek_id, userName)

    override suspend fun getAllProductInDB(): List<DataBaseProductModel> =
        localDataSource.getAllProductsInDB()

    override suspend fun deleteProductInDB(product_id: Int) =
        localDataSource.deleteProdcutInDB(product_id)

    override suspend fun saveProdcutInDB(product: Yemekler)  = localDataSource.saveProdcutInDB(product)


}