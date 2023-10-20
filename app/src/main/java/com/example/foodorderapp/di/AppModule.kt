package com.example.foodorderapp.di


import android.content.Context
import androidx.room.Room
import com.example.foodorderapp.data.datasource.LocalDataSource
import com.example.foodorderapp.data.datasource.RemoteDataSource
import com.example.foodorderapp.data.repository.AuthRepository
import com.example.foodorderapp.data.repository.AuthRepositoryImpl
import com.example.foodorderapp.data.repository.ProductRepository
import com.example.foodorderapp.data.repository.ProductRepositoryImpl
import com.example.foodorderapp.data.retrofit.ApiUtils
import com.example.foodorderapp.data.retrofit.RetrofitDAO
import com.example.foodorderapp.data.room.RoomDAO
import com.example.foodorderapp.data.room.RoomDB
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
class AppModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Singleton
    @Provides
    fun provideAuthRepository(impl: AuthRepositoryImpl): AuthRepository = impl

    @Singleton
    @Provides
    fun provideLocalDataSource(roomDAO: RoomDAO): LocalDataSource {
        return LocalDataSource(roomDAO)
    }

    @Singleton
    @Provides
    fun provideRemoteDataSource(retrofitDAO: RetrofitDAO): RemoteDataSource {
        return RemoteDataSource(retrofitDAO)
    }

    @Singleton
    @Provides
    fun provideRetrofitDao(): RetrofitDAO {
        return ApiUtils.getRetrofitDAO()
    }

    @Singleton
    @Provides
    fun provideProductRepository(impl: ProductRepositoryImpl): ProductRepository = impl

    @Provides
    @Singleton
    fun provideKisilerDataAccessObject(@ApplicationContext context: Context): RoomDAO {
        val vt = Room.databaseBuilder(context, RoomDB::class.java, "product.sqlite")
            .createFromAsset("product.sqlite").build()
        return vt.getRoomDAO()
    }

}