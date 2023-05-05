package com.example.finalproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.backendless.Backendless
import com.backendless.async.callback.AsyncCallback
import com.backendless.exceptions.BackendlessFault
import com.backendless.persistence.DataQueryBuilder
import com.example.finalproject.LoginActivity.Companion.EXTRA_USERID
import com.example.finalproject.databinding.ActivityCollectionBinding
import com.example.finalproject.databinding.ActivityRollBinding

class RollActivity : AppCompatActivity() {

    companion object {
        val TAG = "Roll Activity"
    }

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
            x++
            var rollValue = ((Math.random()*10)+1).toInt()
//            rollValue = 1
//            Toast.makeText(this, "$rollValue", Toast.LENGTH_SHORT).show()
            if(rollValue == 1) {
                var characterRoll = ((Math.random()*characterAmount)+1).toInt()
                var character = Character()
                if(characterRoll == 1) {
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
                    character.ownerId = intent.getStringExtra(EXTRA_USERID).toString()
                }
                notOwned(character)
            }
        }
    }

    private fun save(character: Character) {
        Backendless.Data.of(Character::class.java)
            .save(character, object : AsyncCallback<Character> {
                override fun handleResponse(savedLoan: Character) {
                    Backendless.Data.of(Character::class.java)
                        .save(savedLoan, object : AsyncCallback<Character?> {
                            override fun handleResponse(response: Character?) {
                                Toast.makeText(
                                    this@RollActivity, "you got ${character.name}",
                                    Toast.LENGTH_SHORT).show()
                            }

                            override fun handleFault(fault: BackendlessFault) {
                                Log.d(TAG, "handleFault: ${fault.message}")
                            }
                        })
                }

                override fun handleFault(fault: BackendlessFault) {
                    Log.d(TAG, "handleFault: ${fault.message}")
                }
            })
    }

    private fun notOwned(character: Character){
        var notOwned = true

        val whereClause = "ownerId = '${intent.getStringExtra(EXTRA_USERID)}'"
        val queryBuilder = DataQueryBuilder.create()
        queryBuilder.whereClause = whereClause
        Backendless.Data.of(Character::class.java).find(queryBuilder, object :
            AsyncCallback<MutableList<Character?>?> {
            override fun handleResponse(foundCharacters: MutableList<Character?>?) {
                Log.d(TAG, "handleResponse: $foundCharacters")
                var x = 0
                if (foundCharacters != null) {
                    while(x < foundCharacters.size) {
                        if(foundCharacters[x]!!.id == character.id) {
                            notOwned = false
                        }
                        x++
                    }
                }
                if(notOwned) {
                    save(character)
                }
                else {
                    Toast.makeText(this@RollActivity, "Got duplicate, 1 ticket received instead", Toast.LENGTH_SHORT).show()
                }
            }
            override fun handleFault(fault: BackendlessFault) {
                Log.d(LoginActivity.TAG, "handleFault: ${fault.message}")
            }
        })
    }
}