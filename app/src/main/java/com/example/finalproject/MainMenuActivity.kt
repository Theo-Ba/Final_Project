package com.example.finalproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.example.finalproject.LoginActivity.Companion.EXTRA_USERID
import com.example.finalproject.databinding.ActivityLoginBinding
import com.example.finalproject.databinding.ActivityMainMenuBinding

class MainMenuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonMainCollection.setOnClickListener {
            val userId = intent.getStringExtra(LoginActivity.EXTRA_USERID)
            val collectionIntent = Intent(this, CollectionActivity::class.java)
            collectionIntent.putExtra(EXTRA_USERID, userId)
            startActivity(collectionIntent)
        }

        binding.buttonMainRoll.setOnClickListener {
            val userId = intent.getStringExtra(LoginActivity.EXTRA_USERID)
            val rollIntent = Intent(this,RollActivity::class.java)
            rollIntent.putExtra(EXTRA_USERID, userId)
            startActivity(rollIntent)
        }

        binding.imageViewMainFightButton.setOnClickListener {
            val userId = intent.getStringExtra(EXTRA_USERID)
            val combatIntent = Intent(this, CombatActivity::class.java)
            combatIntent.putExtra(EXTRA_USERID, userId)
            startActivity(combatIntent)
        }
    }

//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        val inflater: MenuInflater = menuInflater
//        inflater.inflate(R.menu.main_menu_menu, menu)
//        return true
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        return when (item.itemId) {
//            R.id.profile -> {
//                true
//            }
//            else -> super.onOptionsItemSelected(item)
//        }
//    }
}