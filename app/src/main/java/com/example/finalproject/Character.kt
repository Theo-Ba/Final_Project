package com.example.finalproject

import android.media.Image

data class Character(
    var name: String = "",
    var ability1: String = "",
    var ability2: String = "",
    var ability1Damage: Int = 0,
    var ability2Damage: Int = 0,
    var health: Int = 0,
    var icon: Image? = null,
    var archetype: String = "",
    var ability1Description: String = "",
    var ability2Description: String = ""
)