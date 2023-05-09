package com.example.finalproject

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Character(
    var name: String = "",
    var ability1: String = "",
    var ability2: String = "",
    var ability1Damage: Int = 0,
    var ability2Damage: Int = 0,
    var health: Int = 0,
    var archetype: String = "",
    var ability1Description: String = "",
    var ability2Description: String = "",
    var id: Int = 0,
    var ownerId: String = "",
    var imageAddress: String = "",
    var title: String = ""
): Parcelable