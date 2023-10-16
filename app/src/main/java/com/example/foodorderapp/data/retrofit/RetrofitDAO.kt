package com.example.foodorderapp.data.retrofit

import com.example.foodorderapp.data.model.product.FoodResponce
import retrofit2.http.GET

interface RetrofitDAO {


    @GET("yemekler/tumYemekleriGetir.php")
    suspend fun getAllProducts(): FoodResponce

    /*
    @GET("kisiler/tum_kisiler.php")
    suspend fun kisileriYukle(): KisilerCevap

    @POST("kisiler/insert_kisiler.php")
    @FormUrlEncoded//Turkce karakterler icin
    suspend fun kaydet(
        @Field("kisi_ad") kisi_ad: String,
        @Field("kisi_tel") kisi_tel: String,
    ): CRUDCevap

    @POST("kisiler/update_kisiler.php")
    @FormUrlEncoded
    suspend fun guncelle(
        @Field("kisi_id") kisi_id: Int,
        @Field("kisi_ad") kisi_ad: String,
        @Field("kisi_tel") kisi_tel: String,
    ): CRUDCevap

    @POST("kisiler/delete_kisiler.php")
    @FormUrlEncoded
    suspend fun sil(
        @Field("kisi_id") kisi_id:Int
    ) : CRUDCevap

    @POST("kisiler/tum_kisiler_arama.php")
    @FormUrlEncoded
    suspend fun ara(
        @Field("kisi_ad") aramaKelimesi: String
    ) : KisilerCevap*/
}