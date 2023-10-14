package com.example.foodorderapp.di

import com.example.foodorderapp.data.repository.AuthRepository
import com.example.foodorderapp.data.repository.AuthRepositoryImpl
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@InstallIn(SingletonComponent::class)
@Module
class AppModule {

    @Provides
    fun provideFirebaseAuth():FirebaseAuth = FirebaseAuth.getInstance()


    @Provides
    fun provideAuthRepository(impl : AuthRepositoryImpl): AuthRepository = impl

}