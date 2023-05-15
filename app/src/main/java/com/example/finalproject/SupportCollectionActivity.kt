package com.example.finalproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.backendless.Backendless
import com.backendless.async.callback.AsyncCallback
import com.backendless.exceptions.BackendlessFault
import com.backendless.persistence.DataQueryBuilder
import com.example.finalproject.CharacterDetailActivity.Companion.EXTRA_HERE_TO_EQUIP
import com.example.finalproject.databinding.ActivitySupportCollectionBinding

class SupportCollectionActivity : AppCompatActivity() {

    companion object{
        val TAG = "SupportCollectionActivity"
    }

    private lateinit var binding: ActivitySupportCollectionBinding
    lateinit var adapter: SupportCollectionAdapter

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
        if(intent.getBooleanExtra(EXTRA_HERE_TO_EQUIP)) {
            
        }
    }

    private fun retrieveAllData(userId: String) {
        val whereClause = "ownerId = '$userId'"
        val queryBuilder = DataQueryBuilder.create()
        queryBuilder.whereClause = whereClause
        Backendless.Data.of(SupportCharacter::class.java).find(queryBuilder, object :
            AsyncCallback<MutableList<SupportCharacter?>?> {
            override fun handleResponse(response: MutableList<SupportCharacter?>?) {
                Log.d(CollectionActivity.TAG, "handleResponse: $response")
                if(response != null) {
                    adapter = SupportCollectionAdapter(response as MutableList<SupportCharacter>)
                }
                binding.recyclerViewSupportCollection.adapter = adapter
                binding.recyclerViewSupportCollection.layoutManager = LinearLayoutManager(this@SupportCollectionActivity)
            }

            override fun handleFault(fault: BackendlessFault?) {
                Log.d(TAG, "handleFault: ${fault!!.message}")
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.switch_to_main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.to_main -> {
                val collectionIntent = Intent(this, CollectionActivity::class.java)
                collectionIntent.putExtra(LoginActivity.EXTRA_USERID, intent.getStringExtra(LoginActivity.EXTRA_USERID))
                this.startActivity(collectionIntent)
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
