package com.example.finalproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.finalproject.CollectionAdapter.Companion.EXTRA_CHARACTER
import com.example.finalproject.databinding.ActivityCharacterDetailBinding
import com.squareup.picasso.Picasso
import weborb.util.ThreadContext.context

class CharacterDetailActivity : AppCompatActivity() {

    companion object{
        val TAG = "CharacterDetailActivity"
    }

    private lateinit var binding: ActivityCharacterDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCharacterDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var passedCharacter = intent.getParcelableExtra<Character>(EXTRA_CHARACTER)
        var character = passedCharacter!!

        binding.textViewCharDetailName.text = character.name
        binding.textViewCharDetailTitle.text = "From: ${character.title}"
        binding.textViewCharDetailAbout.text = "to be implemented"
        binding.textViewCharDetailClass.text = "Class: ${character.archetype}"
        binding.textViewCharDetailAbility1.text = "${character.ability1}:"
        binding.textViewCharDetailAbility1Description.text = character.ability1Description
        binding.textViewCharDetailAbility2.text = "${character.ability2}:"
        binding.textViewCharDetailAbility2Description.text = character.ability2Description
        Picasso.with(this).load(character.imageAddress).fit()
            .into(binding.imageViewCharDetailImage)
    }
}