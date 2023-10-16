package com.example.foodorderapp.data.datasource

import com.example.foodorderapp.data.model.product.FoodResponce
import com.example.foodorderapp.data.model.product.Yemekler
import com.example.foodorderapp.data.retrofit.RetrofitDAO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RemoteDataSource(var retrofitDAO: RetrofitDAO) {

    suspend fun kisileriYukle(): List<Yemekler> = withContext(Dispatchers.IO) {
        return@withContext retrofitDAO.getAllProducts().yemekler
    }
}