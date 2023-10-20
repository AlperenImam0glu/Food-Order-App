package com.example.foodorderapp.data.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.foodorderapp.data.model.databasemodel.DataBaseProductModel

@Dao
interface RoomDAO {

    @Query("SELECT * FROM yemekler")
    suspend fun getAllProduct(): List<DataBaseProductModel>

    @Delete
    suspend fun deleteProdcutInDB(product: DataBaseProductModel)

    @Insert
    suspend fun saveProdcutInDB(product: DataBaseProductModel)

    /*

    saveProdcutInDB


    @Query("SELECT * FROM yemekler")
    suspend fun kisileriYukle(): List<Kisiler>

    @Insert
    suspend fun kaydet(kisi: Kisiler)

    @Update
    suspend fun guncelle(kisi: Kisiler)

    @Delete
    suspend fun sil(kisi: Kisiler)

    @Query("Select * FROM kisiler WHERE kisi_ad like '%'||:aramaKelimesi||'%'")
    suspend fun ara(aramaKelimesi: String): List<Kisiler>
     */
}