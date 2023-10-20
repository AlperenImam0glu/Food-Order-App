package com.example.foodorderapp.data.datasource

import com.example.foodorderapp.data.model.databasemodel.DataBaseProductModel
import com.example.foodorderapp.data.room.RoomDAO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LocalDataSource(var retrofitDAO: RoomDAO) {


    suspend fun getAllProductsInDB(): List<DataBaseProductModel> = withContext(Dispatchers.IO) {
        return@withContext retrofitDAO.getAllProduct()
    }
    /*
     suspend fun kaydet(kisi_ad: String, kisi_tel: String) {
       val yeniKisi = Kisiler(0,kisi_ad,kisi_tel)
        kdao.kaydet(yeniKisi)
    }

    suspend fun guncelle(kisi_id: Int, kisi_ad: String, kisi_tel: String) {
        val guncellenenKisi = Kisiler(kisi_id,kisi_ad,kisi_tel)
        kdao.guncelle(guncellenenKisi)
    }

    suspend fun sil(kisi_id: Int) {
        val silinenKisi = Kisiler(kisi_id,"","")
        kdao.sil(silinenKisi)
    }

    suspend fun kisileriYukle(): List<Kisiler> = withContext(Dispatchers.IO) {
        return@withContext kdao.kisileriYukle()
    }
     */
}