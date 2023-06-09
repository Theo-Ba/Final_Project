package com.example.finalproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.backendless.Backendless
import com.backendless.async.callback.AsyncCallback
import com.backendless.exceptions.BackendlessFault
import com.backendless.persistence.DataQueryBuilder
import com.example.finalproject.LoginActivity.Companion.EXTRA_USERID
import com.example.finalproject.databinding.ActivityCombatBinding
import com.squareup.picasso.Picasso

class CombatActivity : AppCompatActivity() {

    companion object {
        val TAG = "CombatActivity"
    }

    private lateinit var binding: ActivityCombatBinding
    private var character1: Character? = null
    private var character1TempHealth = 0
    private var character2: Character? = null
    private var character2TempHealth = 0
    private var character3: Character? = null
    private var character3TempHealth = 0
    private lateinit var enemy1: Enemy
    private var enemy1TempHealth = 0
    private lateinit var enemy2: Enemy
    private var enemy2TempHealth = 0
    private lateinit var enemy3: Enemy
    private var enemy3TempHealth = 0
    private var turn1 = false
    private var turn2 = false
    private var turn3 = false
    var boost1 = 0
    var boost2 = 0
    var boost3 = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCombatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.groupCombatSlot1.isGone = true
        binding.groupCombatSlot2.isGone = true
        binding.groupCombatSlot3.isGone = true

        initializeCharacters()
    }

    fun initializeCharacters() {
        val whereClause = "ownerId = '${intent.getStringExtra(EXTRA_USERID)}'"
        val queryBuilder = DataQueryBuilder.create()
        queryBuilder.whereClause = whereClause
        Backendless.Data.of(Character::class.java).find(queryBuilder, object :
            AsyncCallback<MutableList<Character?>?> {
            override fun handleResponse(response: MutableList<Character?>?) {
                var x = 0
                if(response != null) {
                    while(x < response.size) {
                        if(response[x]!!.equipped == "slot1") {
                            character1 = response[x]!!
                        }
                        if(response[x]!!.equipped == "slot2") {
                            character2 = response[x]!!
                        }
                        if(response[x]!!.equipped == "slot3") {
                            character3 = response[x]!!
                        }
                        x++
                    }
                    if(character1 != null) {
                        character1TempHealth = character1!!.health
                        binding.groupCombatSlot1.isVisible = true
                        binding.textViewCombatName1.text = character1!!.name
                        binding.textViewCombatHealth1.text = "${character1TempHealth}/${character1!!.health}"
                        binding.buttonCombatAbility11.text = character1!!.ability1
                        binding.buttonCombatAbility12.text = character1!!.ability2
                    }
                    if(character2 != null) {
                        character2TempHealth = character2!!.health
                        binding.groupCombatSlot2.isVisible = true
                        binding.textViewCombatName2.text = character2!!.name
                        binding.textViewCombatHealth2.text = "${character2!!.health}/${character2!!.health}"
                        binding.buttonCombatAbility21.text = character2!!.ability1
                        binding.buttonCombatAbility22.text = character2!!.ability2
                    }
                    if(character3 != null) {
                        character3TempHealth = character3!!.health
                        binding.groupCombatSlot3.isVisible = true
                        binding.textViewCombatName3.text = character3!!.name
                        binding.textViewCombatHealth3.text = "${character3!!.health}/${character3!!.health}"
                        binding.buttonCombatAbility31.text = character3!!.ability1
                        binding.buttonCombatAbility32.text = character3!!.ability2
                    }
                    initializeEnemies()
                }
            }

            override fun handleFault(fault: BackendlessFault?) {
                Log.d(TAG, "handleFault: ${fault!!.message}")
            }

        })
    }

    fun initializeEnemies() {
        Backendless.Data.of(Enemy::class.java).find(object :
            AsyncCallback<MutableList<Enemy?>?> {
            override fun handleResponse(response: MutableList<Enemy?>?) {
                var randomEnemy = ((Math.random()*response!!.size)).toInt()
                enemy1 = response[randomEnemy]!!
                enemy1TempHealth = enemy1.health
                randomEnemy = ((Math.random()*response.size)).toInt()
                enemy2 = response[randomEnemy]!!
                enemy2TempHealth = enemy2.health
                randomEnemy = ((Math.random()*response.size)).toInt()
                enemy3 = response[randomEnemy]!!
                enemy3TempHealth = enemy3.health

                binding.textViewCombatEnemy1.text = enemy1.name
                binding.textViewCombatHealth4.text = "${enemy1TempHealth}/${enemy1.health}"
                Picasso.with(this@CombatActivity).load(enemy1.imageAddress).into(binding.imageViewCombatImage4)
                binding.textViewCombatEnemy2.text = enemy2.name
                binding.textViewCombatHealth5.text = "${enemy2TempHealth}/${enemy2.health}"
                Picasso.with(this@CombatActivity).load(enemy2.imageAddress).into(binding.imageViewCombatImage5)
                binding.textViewCombatEnemy3.text = enemy3.name
                binding.textViewCombatHealth6.text = "${enemy3TempHealth}/${enemy3.health}"
                Picasso.with(this@CombatActivity).load(enemy3.imageAddress).into(binding.imageViewCombatImage6)

                //combat()
                turn1()
            }

            override fun handleFault(fault: BackendlessFault?) {
                Log.d(TAG, "handleFault: ${fault!!.message}")
            }
        })
    }

    private fun turn1() {
        if(character1 != null && character1TempHealth > 0) {
            binding.textViewCombatTurn.text = "It's ${character1!!.name}'s turn"
            turn1 = true
            binding.buttonCombatAbility11.setOnClickListener {
                if(turn1) {
                    var attacking = true
                    var abilityDamage = character1!!.ability1Damage
                    if(character1!!.equippedSupportsPower != null) {
                        abilityDamage += (character1!!.equippedSupportsPower)/100
                    }
                    if(boost1 > 0) {
                        abilityDamage += boost1
                        boost1 = 0
                    }
                    if(character1!!.ability1DamageType == "support") {
                        binding.textViewCombatName2.setOnClickListener {
                            boost2 = abilityDamage
                            turn2()
                        }
                        binding.textViewCombatName3.setOnClickListener {
                            boost3 = abilityDamage
                            turn2()
                        }
                    }
                    else {
                        binding.imageViewCombatImage4.setOnClickListener {
                            if (attacking) {
                                enemy1TempHealth -= abilityDamage
                                binding.textViewCombatHealth4.text =
                                    "${enemy1TempHealth}/${enemy1.health}"
                                if (enemy1TempHealth <= 0) {
                                    //binding.groupCombatEnemy1.isGone = true
                                }
                                attacking = false
                                turn1 = false
                                turn2()
                            }
                        }
                        binding.imageViewCombatImage5.setOnClickListener {
                            if (attacking) {
                                enemy2TempHealth -= abilityDamage
                                binding.textViewCombatHealth5.text =
                                    "${enemy2TempHealth}/${enemy2.health}"
                                if (enemy2TempHealth <= 0) {
                                    //binding.groupCombatEnemy2.isGone = true
                                }
                                attacking = false
                                turn1 = false
                                turn2()
                            }
                        }
                        binding.imageViewCombatImage6.setOnClickListener {
                            if (attacking) {
                                enemy3TempHealth -= abilityDamage
                                binding.textViewCombatHealth6.text =
                                    "${enemy3TempHealth}/${enemy3.health}"
                                if (enemy3TempHealth <= 0) {
                                    //binding.groupCombatEnemy1.isGone = true
                                }
                                attacking = false
                                turn1 = false
                                turn2()
                            }
                        }
                    }
                }
            }
            binding.buttonCombatAbility12.setOnClickListener {
                if(turn1) {
                    var attacking = true
                    var abilityDamage = character1!!.ability2Damage
                    if(character1!!.equippedSupportsPower != null) {
                        abilityDamage += (character1!!.equippedSupportsPower)/100
                    }
                    if(boost1 > 0) {
                        abilityDamage += boost1
                        boost1 = 0
                    }
                    if(character1!!.ability2DamageType == "support") {
                        binding.textViewCombatName2.setOnClickListener {
                            boost2 = abilityDamage
                            turn2()
                        }
                        binding.textViewCombatName3.setOnClickListener {
                            boost3 = abilityDamage
                            turn2()
                        }
                    }
                    else {
                        binding.imageViewCombatImage4.setOnClickListener {
                            if (attacking) {
                                enemy1TempHealth -= abilityDamage
                                binding.textViewCombatHealth4.text =
                                    "${enemy1TempHealth}/${enemy1.health}"
                                if (enemy1TempHealth <= 0) {
                                    //binding.groupCombatEnemy1.isGone = true
                                }
                                attacking = false
                                turn1 = false
                                turn2()
                            }
                        }
                        binding.imageViewCombatImage5.setOnClickListener {
                            if (attacking) {
                                enemy2TempHealth -= abilityDamage
                                binding.textViewCombatHealth5.text =
                                    "${enemy2TempHealth}/${enemy2.health}"
                                if (enemy2TempHealth <= 0) {
                                    //binding.groupCombatEnemy2.isGone = true
                                }
                                attacking = false
                                turn1 = false
                                turn2()
                            }
                        }
                        binding.imageViewCombatImage6.setOnClickListener {
                            if (attacking) {
                                enemy3TempHealth -= abilityDamage
                                binding.textViewCombatHealth6.text =
                                    "${enemy3TempHealth}/${enemy3.health}"
                                if (enemy3TempHealth <= 0) {
                                    //binding.groupCombatEnemy1.isGone = true
                                }
                                attacking = false
                                turn1 = false
                                turn2()
                            }
                        }
                    }
                }
            }
        }
        if((character1TempHealth > 0 || character2TempHealth > 0 || character3TempHealth > 0) &&
            (enemy1TempHealth <= 0 && enemy2TempHealth <= 0 && enemy3TempHealth <= 0)) {
            val whereClause = "ownerId = '${intent.getStringExtra(EXTRA_USERID)}'"
            val queryBuilder = DataQueryBuilder.create()
            queryBuilder.whereClause = whereClause
            Backendless.Data.of(Ticket::class.java).find(queryBuilder, object :
                AsyncCallback<MutableList<Ticket?>?> {
                override fun handleResponse(response: MutableList<Ticket?>?) {
                    response!![0]!!.amount += 1
                    Backendless.Data.of(Ticket::class.java).save(response!![0], object:
                        AsyncCallback<Ticket> {
                        override fun handleResponse(response: Ticket?) {
                            Toast.makeText(this@CombatActivity,
                                "You Won! 1 ticket received", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                        override fun handleFault(fault: BackendlessFault?) {
                            Log.d(RollActivity.TAG, "handleFault: ${fault!!.message}")
                        }
                    })
                }
                override fun handleFault(fault: BackendlessFault?) {
                    Log.d(RollActivity.TAG, "handleFault: ${fault!!.message}")
                }
            })
        }
        if((character1TempHealth <= 0 && character2TempHealth <= 0 && character3TempHealth <= 0) &&
            (enemy1TempHealth > 0 || enemy2TempHealth > 0 || enemy3TempHealth > 0)) {
            Toast.makeText(this, "You Lost", Toast.LENGTH_SHORT).show()
            finish()
        }
        if(character1 == null) {
            turn2()
        }
    }

    private fun turn2() {
        if(character2 != null && character2TempHealth > 0) {
            binding.textViewCombatTurn.text = "It's ${character2!!.name}'s turn"
            turn2 = true
            binding.buttonCombatAbility21.setOnClickListener {
                if(turn2) {
                    var attacking = true
                    var abilityDamage = character2!!.ability1Damage
                    if(character2!!.equippedSupportsPower != null) {
                        abilityDamage += (character2!!.equippedSupportsPower)/100
                    }
                    if(boost2 > 0) {
                        abilityDamage += boost2
                        boost2 = 0
                    }
                    if(character2!!.ability1DamageType == "support") {
                        binding.textViewCombatName1.setOnClickListener {
                            boost1 = abilityDamage
                            turn3()
                        }
                        binding.textViewCombatName3.setOnClickListener {
                            boost3 = abilityDamage
                            turn3()
                        }
                    }
                    else {
                        binding.imageViewCombatImage4.setOnClickListener {
                            if (attacking) {
                                enemy1TempHealth -= abilityDamage
                                binding.textViewCombatHealth4.text =
                                    "${enemy1TempHealth}/${enemy1.health}"
                                if (enemy1TempHealth <= 0) {
                                    //binding.groupCombatEnemy1.isGone = true
                                }
                                attacking = false
                                turn2 = false
                                turn3()
                            }
                        }
                        binding.imageViewCombatImage5.setOnClickListener {
                            if (attacking) {
                                enemy2TempHealth -= abilityDamage
                                binding.textViewCombatHealth5.text =
                                    "${enemy2TempHealth}/${enemy2.health}"
                                if (enemy2TempHealth <= 0) {
                                    //binding.groupCombatEnemy2.isGone = true
                                }
                                attacking = false
                                turn2 = false
                                turn3()
                            }
                        }
                        binding.imageViewCombatImage6.setOnClickListener {
                            if (attacking) {
                                enemy3TempHealth -= abilityDamage
                                binding.textViewCombatHealth6.text =
                                    "${enemy3TempHealth}/${enemy3.health}"
                                if (enemy3TempHealth <= 0) {
                                    //binding.groupCombatEnemy1.isGone = true
                                }
                                attacking = false
                                turn2 = false
                                turn3()
                            }
                        }
                    }
                }
            }
            binding.buttonCombatAbility22.setOnClickListener {
                if(turn2) {
                    var attacking = true
                    var abilityDamage = character2!!.ability2Damage
                    if(character2!!.equippedSupportsPower != null) {
                        abilityDamage += (character2!!.equippedSupportsPower)/100
                    }
                    if(boost2 > 0) {
                        abilityDamage += boost2
                        boost2 = 0
                    }
                    if(character2!!.ability2DamageType == "support") {
                        binding.textViewCombatName1.setOnClickListener {
                            boost1 = abilityDamage
                            turn3()
                        }
                        binding.textViewCombatName3.setOnClickListener {
                            boost3 = abilityDamage
                            turn3()
                        }
                    }
                    else {
                        binding.imageViewCombatImage4.setOnClickListener {
                            if (attacking) {
                                enemy1TempHealth -= abilityDamage
                                binding.textViewCombatHealth4.text =
                                    "${enemy1TempHealth}/${enemy1.health}"
                                if (enemy1TempHealth <= 0) {
                                    //binding.groupCombatEnemy1.isGone = true
                                }
                                attacking = false
                                turn2 = false
                                turn3()
                            }
                        }
                        binding.imageViewCombatImage5.setOnClickListener {
                            if (attacking) {
                                enemy2TempHealth -= abilityDamage
                                binding.textViewCombatHealth5.text =
                                    "${enemy2TempHealth}/${enemy2.health}"
                                if (enemy2TempHealth <= 0) {
                                    //binding.groupCombatEnemy2.isGone = true
                                }
                                attacking = false
                                turn2 = false
                                turn3()
                            }
                        }
                        binding.imageViewCombatImage6.setOnClickListener {
                            if (attacking) {
                                enemy3TempHealth -= abilityDamage
                                binding.textViewCombatHealth6.text =
                                    "${enemy3TempHealth}/${enemy3.health}"
                                if (enemy3TempHealth <= 0) {
                                    //binding.groupCombatEnemy1.isGone = true
                                }
                                attacking = false
                                turn2 = false
                                turn3()
                            }
                        }
                    }
                }
            }
        }
        if((character1TempHealth > 0 || character2TempHealth > 0 || character3TempHealth > 0) &&
            (enemy1TempHealth <= 0 && enemy2TempHealth <= 0 && enemy3TempHealth <= 0)) {
            val whereClause = "ownerId = '${intent.getStringExtra(EXTRA_USERID)}'"
            val queryBuilder = DataQueryBuilder.create()
            queryBuilder.whereClause = whereClause
            Backendless.Data.of(Ticket::class.java).find(queryBuilder, object :
                AsyncCallback<MutableList<Ticket?>?> {
                override fun handleResponse(response: MutableList<Ticket?>?) {
                    response!![0]!!.amount += 1
                    Backendless.Data.of(Ticket::class.java).save(response!![0], object:
                        AsyncCallback<Ticket> {
                        override fun handleResponse(response: Ticket?) {
                            Toast.makeText(this@CombatActivity,
                                "You Won! 1 ticket received", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                        override fun handleFault(fault: BackendlessFault?) {
                            Log.d(RollActivity.TAG, "handleFault: ${fault!!.message}")
                        }
                    })
                }
                override fun handleFault(fault: BackendlessFault?) {
                    Log.d(RollActivity.TAG, "handleFault: ${fault!!.message}")
                }
            })
        }
        if((character1TempHealth <= 0 && character2TempHealth <= 0 && character3TempHealth <= 0) &&
            (enemy1TempHealth > 0 || enemy2TempHealth > 0 || enemy3TempHealth > 0)) {
            Toast.makeText(this, "You Lost", Toast.LENGTH_SHORT).show()
            finish()
        }
        if(character2 == null) {
            turn3()
        }
    }

    private fun turn3() {
        if(character3 != null && character3TempHealth > 0) {
            binding.textViewCombatTurn.text = "It's ${character3!!.name}'s turn"
            turn3 = true
            binding.buttonCombatAbility31.setOnClickListener {
                if(turn3) {
                    var attacking = true
                    var abilityDamage = character3!!.ability1Damage
                    if(character3!!.equippedSupportsPower != null) {
                        abilityDamage += (character2!!.equippedSupportsPower)/100
                    }
                    if(boost3 > 0) {
                        abilityDamage += boost3
                        boost3 = 0
                    }
                    if(character3!!.ability1DamageType == "support") {
                        binding.textViewCombatName1.setOnClickListener {
                            boost1 = abilityDamage
                            turn4()
                        }
                        binding.textViewCombatName2.setOnClickListener {
                            boost2 = abilityDamage
                            turn4()
                        }
                    }
                    else {
                        binding.imageViewCombatImage4.setOnClickListener {
                            if (attacking) {
                                enemy1TempHealth -= abilityDamage
                                binding.textViewCombatHealth4.text =
                                    "${enemy1TempHealth}/${enemy1.health}"
                                if (enemy1TempHealth <= 0) {
                                    //binding.groupCombatEnemy1.isGone = true
                                }
                                attacking = false
                                turn3 = false
                                turn4()
                            }
                        }
                        binding.imageViewCombatImage5.setOnClickListener {
                            if (attacking) {
                                enemy2TempHealth -= abilityDamage
                                binding.textViewCombatHealth5.text =
                                    "${enemy2TempHealth}/${enemy2.health}"
                                if (enemy2TempHealth <= 0) {
                                    //binding.groupCombatEnemy2.isGone = true
                                }
                                attacking = false
                                turn3 = false
                                turn4()
                            }
                        }
                        binding.imageViewCombatImage6.setOnClickListener {
                            if (attacking) {
                                enemy3TempHealth -= abilityDamage
                                binding.textViewCombatHealth6.text =
                                    "${enemy3TempHealth}/${enemy3.health}"
                                if (enemy3TempHealth <= 0) {
                                    //binding.groupCombatEnemy1.isGone = true
                                }
                                attacking = false
                                turn3 = false
                                turn4()
                            }
                        }
                    }
                }
            }
            binding.buttonCombatAbility32.setOnClickListener {
                if(turn3) {
                    var attacking = true
                    var abilityDamage = character3!!.ability2Damage
                    if(character3!!.equippedSupportsPower != null) {
                        abilityDamage += (character2!!.equippedSupportsPower)/100
                    }
                    if(boost3 > 0) {
                        abilityDamage += boost3
                        boost3 = 0
                    }
                    if(character3!!.ability2DamageType == "support") {
                        binding.textViewCombatName1.setOnClickListener {
                            boost1 = abilityDamage
                            turn4()
                        }
                        binding.textViewCombatName2.setOnClickListener {
                            boost2 = abilityDamage
                            turn4()
                        }
                    }
                    else {
                        binding.imageViewCombatImage4.setOnClickListener {
                            if (attacking) {
                                enemy1TempHealth -= abilityDamage
                                binding.textViewCombatHealth4.text =
                                    "${enemy1TempHealth}/${enemy1.health}"
                                if (enemy1TempHealth <= 0) {
                                    //binding.groupCombatEnemy1.isGone = true
                                }
                                attacking = false
                                turn3 = false
                                turn4()
                            }
                        }
                        binding.imageViewCombatImage5.setOnClickListener {
                            if (attacking) {
                                enemy2TempHealth -= abilityDamage
                                binding.textViewCombatHealth5.text =
                                    "${enemy2TempHealth}/${enemy2.health}"
                                if (enemy2TempHealth <= 0) {
                                    //binding.groupCombatEnemy2.isGone = true
                                }
                                attacking = false
                                turn3 = false
                                turn4()
                            }
                        }
                        binding.imageViewCombatImage6.setOnClickListener {
                            if (attacking) {
                                enemy3TempHealth -= abilityDamage
                                binding.textViewCombatHealth6.text =
                                    "${enemy3TempHealth}/${enemy3.health}"
                                if (enemy3TempHealth <= 0) {
                                    //binding.groupCombatEnemy1.isGone = true
                                }
                                attacking = false
                                turn3 = false
                                turn4()
                            }
                        }
                    }
                }
            }
        }
        if(character3 == null) {
            turn4()
        }
    }

    private fun turn4() {
        if(enemy1TempHealth > 0) {
            binding.textViewCombatTurn.text = "It's ${enemy1!!.name}'s turn"
            var whoToAttack = ((Math.random()* 3)+1).toInt()
            var turnActive = true
            while(turnActive) {
                if(whoToAttack == 1 && character1 != null) {
                    character1TempHealth -= enemy1.damage
                    binding.textViewCombatHealth1.text = "${character1TempHealth}/${character1!!.health}"
                    turn5()
                    turnActive = false
                }
                else if(whoToAttack == 1 && character1 == null)
                    whoToAttack = ((Math.random()* 3)+1).toInt()
                if(whoToAttack == 2 && character2 != null) {
                    character2TempHealth -= enemy1.damage
                    binding.textViewCombatHealth2.text = "${character2TempHealth}/${character2!!.health}"
                    turn5()
                    turnActive = false
                }
                else if(whoToAttack == 2 && character2 == null)
                    whoToAttack = ((Math.random()* 3)+1).toInt()
                if(whoToAttack == 3 && character3 != null) {
                    character3TempHealth -= enemy1.damage
                    binding.textViewCombatHealth3.text = "${character3TempHealth}/${character3!!.health}"
                    turn5()
                    turnActive = false
                }
                else if(whoToAttack == 3 && character3 == null)
                    whoToAttack = ((Math.random()* 3)+1).toInt()
            }
        }
        if(enemy1TempHealth <= 0) {
            turn5()
        }
    }

    private fun turn5() {
        if(enemy2TempHealth > 0) {
            binding.textViewCombatTurn.text = "It's ${enemy2!!.name}'s turn"
            var whoToAttack = ((Math.random()* 3)+1).toInt()
            var turnActive = true
            while(turnActive) {
                if(whoToAttack == 1 && character1 != null) {
                    character1TempHealth -= enemy2.damage
                    binding.textViewCombatHealth1.text = "${character1TempHealth}/${character1!!.health}"
                    turn6()
                    turnActive = false
                }
                else if(whoToAttack == 1 && character1 == null)
                    whoToAttack = ((Math.random()* 3)+1).toInt()
                if(whoToAttack == 2 && character2 != null) {
                    character2TempHealth -= enemy2.damage
                    binding.textViewCombatHealth2.text = "${character2TempHealth}/${character2!!.health}"
                    turn6()
                    turnActive = false
                }
                else if(whoToAttack == 2 && character2 == null)
                    whoToAttack = ((Math.random()* 3)+1).toInt()
                if(whoToAttack == 3 && character3 != null) {
                    character3TempHealth -= enemy2.damage
                    binding.textViewCombatHealth3.text = "${character3TempHealth}/${character3!!.health}"
                    turn6()
                    turnActive = false
                }
                else if(whoToAttack == 3 && character3 == null)
                    whoToAttack = ((Math.random()* 3)+1).toInt()
            }
        }
        if(enemy2TempHealth <= 0) {
            turn6()
        }
    }

    private fun turn6() {
        if(enemy3TempHealth > 0) {
            binding.textViewCombatTurn.text = "It's ${enemy3!!.name}'s turn"
            var whoToAttack = ((Math.random()* 3)+1).toInt()
            var turnActive = true
            while(turnActive) {
                if(whoToAttack == 1 && character1 != null) {
                    character1TempHealth -= enemy3.damage
                    binding.textViewCombatHealth1.text = "${character1TempHealth}/${character1!!.health}"
                    turn1()
                    turnActive = false
                }
                else if(whoToAttack == 1 && character1 == null)
                    whoToAttack = ((Math.random()* 3)+1).toInt()
                if(whoToAttack == 2 && character2 != null) {
                    character2TempHealth -= enemy3.damage
                    binding.textViewCombatHealth2.text = "${character2TempHealth}/${character2!!.health}"
                    turn1()
                    turnActive = false
                }
                else if(whoToAttack == 2 && character2 == null)
                    whoToAttack = ((Math.random()* 3)+1).toInt()
                if(whoToAttack == 3 && character3 != null) {
                    character3TempHealth -= enemy3.damage
                    binding.textViewCombatHealth3.text = "${character3TempHealth}/${character3!!.health}"
                    turn1()
                    turnActive = false
                }
                else if(whoToAttack == 3 && character3 == null)
                    whoToAttack = ((Math.random()* 3)+1).toInt()
            }
        }
        if(enemy3TempHealth <= 0) {
            turn1()
        }
    }
}