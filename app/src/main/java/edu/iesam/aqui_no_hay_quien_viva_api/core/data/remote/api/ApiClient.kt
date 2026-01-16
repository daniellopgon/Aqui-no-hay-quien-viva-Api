package edu.iesam.aqui_no_hay_quien_viva_api.core.data.remote.api

import org.koin.core.annotation.Single
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Singleton that provides configured Retrofit instance.
 * Base URL points to the ANHQV API.
 */
@Single
class ApiClient {
    private val BASE_URL = "http://api.anhqv-stats.es/api/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun <T> createService(typeClass: Class<T>): T {
        return retrofit.create(typeClass)
    }
}
