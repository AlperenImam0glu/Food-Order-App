package com.example.foodorderapp.utils

import android.widget.ImageView
import com.bumptech.glide.Glide

fun ImageView.loadImage(imageName: String ){
    val url = "http://kasimadalan.pe.hu/yemekler/resimler/$imageName"

    Glide.with(this)
        .load(url)
        .centerInside()
        .into(this)
}