package com.example.foodorderapp.utils

import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import com.bumptech.glide.Glide

fun ImageView.loadImage(imageName: String ){
    val url = "http://kasimadalan.pe.hu/yemekler/resimler/$imageName"

    Glide.with(this)
        .load(url)
        .centerInside()
        .into(this)
}


fun Navigation.safeNav(view: View,action: NavDirections){
    try {
        findNavController(view).navigate(action)
    }catch (e:Exception){
        Log.e("Navigation Exception","${e.message}")
    }

}