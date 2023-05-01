package com.example.finalproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.backendless.Backendless
import com.backendless.async.callback.AsyncCallback
import com.backendless.exceptions.BackendlessFault
import com.backendless.persistence.DataQueryBuilder
import com.example.finalproject.databinding.ActivityCollectionBinding

class CollectionActivity : AppCompatActivity() {

    companion object{
        val TAG = "CollectionActivity"
    }

    private lateinit var binding: ActivityCollectionBinding
    lateinit var adapter: CollectionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCollectionBinding.inflate(layoutInflater)
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
        Backendless.Data.of(Character::class.java).find(queryBuilder, object :
            AsyncCallback<MutableList<Character?>?> {
            override fun handleResponse(foundLoans: MutableList<Character?>?) {
                Log.d(TAG, "handleResponse: $foundLoans")
                if(foundLoans != null) {
                    adapter = CollectionAdapter(foundLoans as MutableList<Character>)
                }
                binding.recyclerViewCollection.adapter = adapter
                binding.recyclerViewCollection.layoutManager = LinearLayoutManager(this@CollectionActivity)
            }

            override fun handleFault(fault: BackendlessFault) {
                // an error has occurred, the error code can be retrieved with fault.getCode()
                Log.d(LoginActivity.TAG, "handleFault: ${fault.message}")
            }
        })
    }
}