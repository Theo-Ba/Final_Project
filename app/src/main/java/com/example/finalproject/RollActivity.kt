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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RollActivity : AppCompatActivity() {

    companion object {
        val TAG = "RollActivity"
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
            rollValue = 2
            Toast.makeText(this, "$rollValue", Toast.LENGTH_SHORT).show()
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
            else if(rollValue in 2..5) {
                val jikanRestService = RetrofitHelper.getInstance().create(JikanRestService::class.java)
                val characterIdCall = jikanRestService.getRandomCharacterID()
                characterIdCall.enqueue(object: Callback<RandomCharacterID> {
                    override fun onResponse(
                        call: Call<RandomCharacterID>,
                        response: Response<RandomCharacterID>
                    ) {
                        Log.d(TAG, "onResponse: ${response.body()}")
                        var id = response.body()!!.data.mal_id
                        getSupportCharacter(id)
                    }
                    override fun onFailure(call: Call<RandomCharacterID>, t: Throwable) {
                        Log.d(TAG, "onFailure: ${t.message}")
                    }
                })
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

    private fun saveSupportChar(supportCharacter: SupportCharacter) {
        Backendless.Data.of(SupportCharacter::class.java).save(supportCharacter, object:
            AsyncCallback<SupportCharacter> {
            override fun handleResponse(response: SupportCharacter?) {
                Toast.makeText(this@RollActivity, "you got ${supportCharacter.name}"
                    , Toast.LENGTH_SHORT).show()
            }

            override fun handleFault(fault: BackendlessFault?) {
                Log.d(TAG, "handleFault: ${fault!!.message}")
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
                Log.d(TAG, "handleFault: ${fault.message}")
            }
        })
    }

    fun getSupportCharacter(id: Int) {
        var supportCharacter = SupportCharacter()
        val jikanRestService = RetrofitHelper.getInstance().create(JikanRestService::class.java)
        val characterDataCall = jikanRestService.getCharacterData(id.toString())
        characterDataCall.enqueue(object : Callback<CharacterData> {
            override fun onResponse(call: Call<CharacterData>, response: Response<CharacterData>) {
                supportCharacter.name = response.body()!!.data.name
                supportCharacter.imageAddress = response.body()!!.data.images.jpg.image_url
                supportCharacter.power = response.body()!!.data.favorites
                Log.d(TAG, response.body()!!.data.anime.anime.title)
                supportCharacter.title = response.body()!!.data.anime.anime.title
                supportCharacter.ownerId = intent.getStringExtra(EXTRA_USERID).toString()
                saveSupportChar(supportCharacter)
            }

            override fun onFailure(call: Call<CharacterData>, t: Throwable) {
                Log.d(TAG, "onFailure: ${t.message}")
            }
        })
    }
}