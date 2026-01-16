package edu.iesam.aqui_no_hay_quien_viva_api.features.character.data.remote

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path


interface CharacterApiService {

    @GET("api/characters")
    suspend fun findAll(): Response<List<CharacterApiModel>>

    @GET("api/characters/{id}")
    suspend fun findById(@Path("id") id: String): Response<CharacterApiModel>
}
