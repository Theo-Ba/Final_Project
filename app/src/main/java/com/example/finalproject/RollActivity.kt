package com.example.finalproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.finalproject.databinding.ActivityCollectionBinding
import com.example.finalproject.databinding.ActivityRollBinding

class RollActivity : AppCompatActivity() {

    var characterAmount = 1

    private lateinit var binding: ActivityRollBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRollBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonRollRoll1.setOnClickListener {
            roll(1)
        }
    }

    fun roll(n: Int) {
        var x = 0
        while(x < n) {
            var rollValue = ((Math.random()+1)*10).toInt()
            if(rollValue == 1) {
                var characterRoll = ((Math.random()+1)*characterAmount).toInt()
                if(characterRoll == 1) {
                    var character = Character()
                    character.name = "Arthur Boyle"
                    character.archetype = "Fighter"
                    character.ability1Description = "Boyle uses his sword to strike the enemy, doing 20 damage"
                    character.id = 134151
                    character.ability1 = "Excalibur"
                    character.ability1Damage = 20
                    character.ability2 = "Blazing Sword"
                    character.ability2Damage = 30
                    character.ability2Description = "Boyle briefly increases the flame output of the sword, doing 30 damage"
                    character.health = 100
                }
            }
        }
    }
}