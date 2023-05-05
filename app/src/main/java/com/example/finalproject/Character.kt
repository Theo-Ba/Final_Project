package com.example.finalproject

import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

data class Character(
    var name: String = "",
    var ability1: String = "",
    var ability2: String = "",
    var ability1Damage: Int = 0,
    var ability2Damage: Int = 0,
    var health: Int = 0,
    var archetype: String = "",
    var ability1Description: String = "",
    var ability2Description: String = "",
    var id: Int = 0,
    var ownerId: String =""
) {
    fun getImageAddress() : String {
        var returnValue =""
        val jikanRestService = RetrofitHelper.getInstance().create(JikanRestService::class.java)
        val characterDataCall = jikanRestService.getCharacterData(id.toString())
        characterDataCall.enqueue(object: Callback<CharacterData> {
            override fun onResponse(call: Call<CharacterData>, response: Response<CharacterData>) {
                returnValue = response.body()?.data?.images?.jpg?.image_url.toString()
            }

            override fun onFailure(call: Call<CharacterData>, t: Throwable) {
                Log.d("Character", "onFailure: ${t.message}")
            }
        })
        return returnValue
    }

    fun getAbout() : String {
        var returnValue =""
        val jikanRestService = RetrofitHelper.getInstance().create(JikanRestService::class.java)
        val characterDataCall = jikanRestService.getCharacterData(id.toString())
        characterDataCall.enqueue(object: Callback<CharacterData> {
            override fun onResponse(call: Call<CharacterData>, response: Response<CharacterData>) {
                returnValue = response.body()?.data?.about.toString()
            }

            override fun onFailure(call: Call<CharacterData>, t: Throwable) {
                Log.d("Character", "onFailure: ${t.message}")
            }
        })
        return returnValue
    }

    fun getTitle() : String {
        var returnValue =""
        val jikanRestService = RetrofitHelper.getInstance().create(JikanRestService::class.java)
        val characterDataCall = jikanRestService.getCharacterData(id.toString())
        characterDataCall.enqueue(object: Callback<CharacterData> {
            override fun onResponse(call: Call<CharacterData>, response: Response<CharacterData>) {
                returnValue = response.body()?.data?.anime?.title.toString()
            }

            override fun onFailure(call: Call<CharacterData>, t: Throwable) {
                Log.d("Character", "onFailure: ${t.message}")
            }
        })
        return returnValue
    }
}