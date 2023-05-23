package com.example.finalproject

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Enemy(
    var name: String = "",
    var damage: Int = 0,
    var health: Int = 0,
    var imageAddress: String = "",
    var strongAgainst: String = "",
    var weakTo1: String = "",
    var weakTo2: String = "",
    var objectId: String? = null
) : Parcelable
