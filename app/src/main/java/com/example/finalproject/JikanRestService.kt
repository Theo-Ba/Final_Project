package com.example.finalproject

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface JikanRestService {

    @GET("characters/{id}/full")
    fun getCharacterData(
        @Path("id") id: String
    ) : Call<CharacterData>
}