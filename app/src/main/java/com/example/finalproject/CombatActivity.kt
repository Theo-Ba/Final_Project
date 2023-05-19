package com.example.finalproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.finalproject.databinding.ActivityCombatBinding

class CombatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCombatBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCombatBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }

    fun initializeCharacters() {

    }

    fun initializeEnemies() {

    }
}