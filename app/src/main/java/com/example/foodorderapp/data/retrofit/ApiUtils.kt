package com.example.foodorderapp.data.retrofit

class ApiUtils {
    companion object {
        val BASE_URL = "http://kasimadalan.pe.hu/"

        fun getRetrofitDAO() : RetrofitDAO {
            return RetrofitClient.getClient(BASE_URL).create(RetrofitDAO::class.java)
        }
    }
}