package com.example.finalproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.finalproject.databinding.ActivityCollectionBinding

class CollectionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCollectionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCollectionBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}