package edu.iesam.aqui_no_hay_quien_viva_api.features.character.data.remote.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path


import edu.iesam.aqui_no_hay_quien_viva_api.core.data.remote.api.PaginatedResponse

interface CharacterApiService {

    @GET("characters")
    suspend fun findAll(): Response<PaginatedResponse<CharacterApiModel>>

    @GET("characters/{id}")
    suspend fun findById(@Path("id") id: String): Response<CharacterApiModel>
}