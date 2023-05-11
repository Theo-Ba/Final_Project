package com.example.finalproject

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface JikanRestService {

    @GET("characters/{id}/full")
    fun getCharacterData(
        @Path("id") id: String
    ) : Call<CharacterData>

    @GET("random/characters")
    fun getRandomCharacterID(
    ) : Call<RandomCharacterID>
}