package com.example.foodorderapp.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.foodorderapp.data.model.databasemodel.DataBaseProductModel

@Database(entities = [DataBaseProductModel ::class], version = 1)
abstract class RoomDB : RoomDatabase() {
     abstract fun getRoomDAO() : RoomDAO
}