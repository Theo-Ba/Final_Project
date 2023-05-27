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
                                Log.d(RollActivity.TAG, "handleFault: ${fault!!.message}")
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