package com.example.foodorderapp.data.datasource

import com.example.foodorderapp.data.model.CRUDResponce
import com.example.foodorderapp.data.model.cart.CartResponce
import com.example.foodorderapp.data.model.product.FoodResponce
import com.example.foodorderapp.data.model.product.Yemekler
import com.example.foodorderapp.data.retrofit.RetrofitDAO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RemoteDataSource(var retrofitDAO: RetrofitDAO) {

    suspend fun getAllProduct(): List<Yemekler> = withContext(Dispatchers.IO) {
        return@withContext retrofitDAO.getAllProducts().yemekler
    }

    suspend fun addProductToCart(product: Yemekler, userName: String): CRUDResponce =
        withContext(Dispatchers.IO) {
            return@withContext retrofitDAO.addProductToCart(
                product.yemek_adi ?: "",
                product.yemek_resim_adi ?: "",
                product.yemek_fiyat.toString().toInt() ?:0,
                product.yemek_siparis_adet,
                userName
            )
        }

    suspend fun getProductInCart(userName: String): CartResponce =
        withContext(Dispatchers.IO) {
            return@withContext retrofitDAO.getProductInCart(userName)
        }

    suspend fun deleteProductInCart(yemek_id: Int, userName: String): CRUDResponce =
        withContext(Dispatchers.IO) {
            return@withContext retrofitDAO.deleteProductInCart(
                yemek_id,
                userName
            )
        }


}