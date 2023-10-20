package com.example.foodorderapp.data.model.databasemodel

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull


@Entity(tableName = "yemekler")
data class DataBaseProductModel(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "yemek_uid") @NotNull var uid: Int,
    @ColumnInfo(name = "yemek_adi") @NotNull var yemek_adi: String,
    @ColumnInfo(name = "yemek_resim_adi") @NotNull var yemek_resim_adi: String,
    @ColumnInfo(name = "yemek_fiyat") @NotNull var yemek_fiyat: String,
    @ColumnInfo(name = "yemek_id") @NotNull var yemek_id: String,
    @ColumnInfo(name = "yemek_siparis_adet") @NotNull var yemek_siparis_adet: Int,
    @ColumnInfo(name = "kullanici_id") @NotNull var kullanici_id: String
)
