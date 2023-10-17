package com.example.foodorderapp.data.model.product

import android.os.Parcel
import android.os.Parcelable

data class Yemekler(
    val yemek_adi: String?,
    val yemek_fiyat: String?,
    val yemek_id: String?,
    val yemek_resim_adi: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(yemek_adi)
        parcel.writeString(yemek_fiyat)
        parcel.writeString(yemek_id)
        parcel.writeString(yemek_resim_adi)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Yemekler> {
        override fun createFromParcel(parcel: Parcel): Yemekler {
            return Yemekler(parcel)
        }

        override fun newArray(size: Int): Array<Yemekler?> {
            return arrayOfNulls(size)
        }
    }
}