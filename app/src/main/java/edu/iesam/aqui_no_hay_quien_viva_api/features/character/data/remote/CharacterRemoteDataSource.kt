package edu.iesam.aqui_no_hay_quien_viva_api.features.character.data.remote

import edu.iesam.aqui_no_hay_quien_viva_api.core.data.remote.api.ApiClient
import edu.iesam.aqui_no_hay_quien_viva_api.core.data.remote.api.apiCall
import org.koin.core.annotation.Single


@Single
class CharacterRemoteDataSource(private val apiClient: ApiClient) {

    private val apiService: CharacterApiService by lazy {
        apiClient.createService(CharacterApiService::class.java)
    }

    suspend fun getCharacters(): Result<List<CharacterApiModel>> {
        return apiCall { apiService.findAll() }
    }

    suspend fun getCharacterById(id: String): Result<CharacterApiModel> {
        return apiCall { apiService.findById(id) }
    }
}
