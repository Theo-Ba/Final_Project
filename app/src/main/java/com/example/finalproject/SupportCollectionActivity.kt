package com.example.finalproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.backendless.Backendless
import com.backendless.async.callback.AsyncCallback
import com.backendless.exceptions.BackendlessFault
import com.backendless.persistence.DataQueryBuilder
import com.example.finalproject.databinding.ActivityCollectionBinding
import com.example.finalproject.databinding.ActivitySupportCollectionBinding

class SupportCollectionActivity : AppCompatActivity() {

    companion object{
        val TAG = "SupportCollectionActivity"
    }

    private lateinit var binding: ActivitySupportCollectionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySupportCollectionBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
        val userId = intent.getStringExtra(LoginActivity.EXTRA_USERID)
        if(userId != null) {
            retrieveAllData(userId)
        }
    }

    private fun retrieveAllData(userId: String) {
        val whereClause = "ownerId = '$userId'"
        val queryBuilder = DataQueryBuilder.create()
        queryBuilder.whereClause = whereClause
        Backendless.Data.of(SupportCharacter::class.java).find(queryBuilder, object :
            AsyncCallback<MutableList<SupportCharacter?>?> {
            override fun handleResponse(response: MutableList<SupportCharacter?>?) {
                TODO("Not yet implemented")
            }

            override fun handleFault(fault: BackendlessFault?) {
                Log.d(TAG, "handleFault: ${fault!!.message}")
            }
        })
    }
}