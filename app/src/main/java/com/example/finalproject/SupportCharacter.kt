package com.example.finalproject

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SupportCharacter(
    var name: String = "",
    var power: Int = 86,
    var ownerId: String = "",
    var imageAddress: String = "",
    var title: String = ""
) : Parcelable
