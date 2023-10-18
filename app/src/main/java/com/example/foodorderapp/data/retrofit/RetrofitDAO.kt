package com.example.foodorderapp.data.retrofit

import com.example.foodorderapp.data.model.CRUDResponce
import com.example.foodorderapp.data.model.cart.CartResponce
import com.example.foodorderapp.data.model.product.FoodResponce
import com.example.foodorderapp.data.model.product.Yemekler
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface RetrofitDAO {


    @GET("yemekler/tumYemekleriGetir.php")
    suspend fun getAllProducts(): FoodResponce


    @POST("yemekler/sepeteYemekEkle.php")
    @FormUrlEncoded//Turkce karakterler icin
    suspend fun addProductToCart(
        @Field("yemek_adi") yemek_adi: String,
        @Field("yemek_resim_adi") yemek_resim_adi: String,
        @Field("yemek_fiyat") yemek_fiyat: Int,
        @Field("yemek_siparis_adet") yemek_siparis_adet: Int,
        @Field("kullanici_adi") kullanici_adi: String,
    ): CRUDResponce


    @POST("yemekler/sepettekiYemekleriGetir.php")
    @FormUrlEncoded//Turkce karakterler icin
    suspend fun getProductInCart(
        @Field("kullanici_adi") kullanici_adi: String,
    ): CartResponce


    @POST("yemekler/sepettenYemekSil.php")
    @FormUrlEncoded//Turkce karakterler icin
    suspend fun deleteProductInCart(
        @Field("sepet_yemek_id") yemek_id: Int,
        @Field("kullanici_adi") kullanici_adi: String,
        ): CRUDResponce

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