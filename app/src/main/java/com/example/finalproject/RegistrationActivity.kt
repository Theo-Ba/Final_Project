package com.example.finalproject

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.backendless.Backendless
import com.backendless.BackendlessUser
import com.backendless.async.callback.AsyncCallback
import com.backendless.exceptions.BackendlessFault
import com.example.finalproject.databinding.ActivityRegistrationBinding

class RegistrationActivity : AppCompatActivity() {

    companion object {
        val TAG = "RegistrationActivity"
    }

    private lateinit var binding: ActivityRegistrationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Backendless.initApp( this, Constants.APP_ID, Constants.API_KEY )

        binding.buttonRegistrationRegister.setOnClickListener {
            val password = binding.editTextRegistrationPassword.text.toString()
            val confirm = binding.editTextRegistrationConfirmPassword.text.toString()
            val username = binding.editTextRegistrationUsername.text.toString()
            val name = binding.editTextRegistrationName.text.toString()
            val email = binding.editTextRegistrationEmail.text.toString()
            if(RegistrationUtil.validatePassword(password, confirm) &&
                RegistrationUtil.validateUsername(username) &&
                RegistrationUtil.validateName(name) && RegistrationUtil.validateEmail(email)) {

                val user = BackendlessUser()
                user.setProperty("username", username)
                user.email = email
                user.password = password

                Backendless.UserService.register(user, object : AsyncCallback<BackendlessUser?> {
                    override fun handleResponse(registeredUser: BackendlessUser?) {
                        var ticket = Ticket(10)
                        ticket.ownerId = user.userId
                        Backendless.Data.of(Ticket::class.java).save(ticket, object:
                            AsyncCallback<Ticket> {
                            override fun handleResponse(response: Ticket?) {
                                var character = Character()
                                character.name = "Arthur Boyle"
                                character.archetype = "Fighter"
                                character.ability1Description = "Boyle uses his sword to strike the enemy, doing 20 slashing damage"
                                character.id = 134151
                                character.ability1 = "Excalibur"
                                character.ability1Damage = 20
                                character.ability2 = "Blazing Sword"
                                character.ability2Damage = 30
                                character.ability2Description = "Boyle briefly increases the flame output of the sword, doing 30 fire damage"
                                character.health = 100
                                character.ownerId = user.userId
                                character.imageAddress = "https://cdn.myanimelist.net/images/characters/2/384687.jpg"
                                character.title = "Fire Force"
                                character.ability1DamageType = "slashing"
                                character.ability2DamageType = "fire"
                                Backendless.Data.of(Character::class.java).save(character, object:
                                    AsyncCallback<Character> {
                                    override fun handleResponse(response: Character?) {
                                        var character1 = Character()
                                        character1.name = "Shinei Nouzen"
                                        character1.archetype = "Pilot"
                                        character1.ability1Description = "Shin fires the central cannon at the enemy, doing 20 piercing damage"
                                        character1.id = 150823
                                        character1.ability1 = "Main Cannon"
                                        character1.ability1Damage = 20
                                        character1.ability2 = "Slashing Blades"
                                        character1.ability2Damage = 30
                                        character1.ability2Description = "Shin lunges the mech forwards and uses the two blades located at the front, doing 30 slashing damage"
                                        character1.health = 100
                                        character1.ownerId = user.userId
                                        character1.imageAddress = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRI2Df3VbVy7BU9e9hewEPR9oTju9suI2xgkVxQmpuixTo9UkarTdrnmrH9-zAJSeuhQnjImEgQYyI&usqp=CAU&ec=48600112"
                                        character1.title = "86 [EIGHTY-SIX]"
                                        character1.ability1DamageType = "piercing"
                                        character1.ability2DamageType = "slashing"
                                        Backendless.Data.of(Character::class.java).save(character1, object:
                                            AsyncCallback<Character> {
                                            override fun handleResponse(response: Character?) {
                                                val resultIntent = Intent().apply {
                                                    putExtra(
                                                        LoginActivity.EXTRA_USERNAME,
                                                        binding.editTextRegistrationUsername.text.toString()
                                                    )
                                                    putExtra(LoginActivity.EXTRA_PASSWORD, password)
                                                }
                                                setResult(Activity.RESULT_OK, resultIntent)
                                                finish()
                                            }

                                            override fun handleFault(fault: BackendlessFault?) {
                                                Log.d(TAG, "handleFault: ${fault!!.message}")
                                            }

                                        })
                                    }

                                    override fun handleFault(fault: BackendlessFault?) {
                                        Log.d(TAG, "handleFault: ${fault!!.message}")
                                    }
                                })
                            }

                            override fun handleFault(fault: BackendlessFault?) {
                                Log.d(TAG, "handleFault: ${fault!!.message}")
                            }
                        })
                    }

                    override fun handleFault(fault: BackendlessFault) {
                        Log.d(TAG, "handleFault: ${fault.message}")
                    }
                })
            }
            else {
                var failedText = "failed to register due to "
                if(!RegistrationUtil.validatePassword(password, confirm))
                    failedText += "password needs to be secure "
                if(!RegistrationUtil.validateUsername(username))
                    failedText += "username already taken "
                if(!RegistrationUtil.validateName(name))
                    failedText += "name not working "
                if(!RegistrationUtil.validateEmail(email))
                    failedText += "email not working "
                Toast.makeText(this, failedText, Toast.LENGTH_SHORT).show()
            }
        }
    }
}