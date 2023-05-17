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
import com.example.finalproject.LoginActivity.Companion.EXTRA_USERID
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
            override fun handleResponse(foundCharacters: MutableList<Character?>?) {
                Log.d(TAG, "handleResponse: $foundCharacters")
                if(foundCharacters != null) {
                    adapter = CollectionAdapter(foundCharacters as MutableList<Character>, userId)
                }
                binding.recyclerViewCollection.adapter = adapter
                binding.recyclerViewCollection.layoutManager = LinearLayoutManager(this@CollectionActivity)
            }

            override fun handleFault(fault: BackendlessFault) {
                // an error has occurred, the error code can be retrieved with fault.getCode()
                Log.d(TAG, "handleFault: ${fault.message}")
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.switch_to_support_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.to_support -> {
                val supportCollectionIntent = Intent(this, SupportCollectionActivity::class.java)
                supportCollectionIntent.putExtra(EXTRA_USERID, intent.getStringExtra(LoginActivity.EXTRA_USERID))
                supportCollectionIntent.putExtra(EXTRA_HERE_TO_EQUIP, false)
                this.startActivity(supportCollectionIntent)
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}