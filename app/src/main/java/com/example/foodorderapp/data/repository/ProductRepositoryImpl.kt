package com.example.foodorderapp.data.repository

import com.example.foodorderapp.data.datasource.LocalDataSource
import com.example.foodorderapp.data.datasource.RemoteDataSource
import com.example.foodorderapp.data.model.product.Yemekler
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    override val remoteDataSource: RemoteDataSource, override val localDataSource: LocalDataSource
) : ProductRepository {

    override suspend fun getAllProducts() : List<Yemekler> = remoteDataSource.kisileriYukle()

}