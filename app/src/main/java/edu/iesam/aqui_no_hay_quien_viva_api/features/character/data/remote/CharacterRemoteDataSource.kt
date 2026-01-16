package edu.iesam.aqui_no_hay_quien_viva_api.features.character.data.remote

import edu.iesam.aqui_no_hay_quien_viva_api.core.data.remote.api.ApiClient
import edu.iesam.aqui_no_hay_quien_viva_api.core.data.remote.api.apiCall
import edu.iesam.aqui_no_hay_quien_viva_api.features.character.data.remote.api.CharacterApiService
import edu.iesam.aqui_no_hay_quien_viva_api.features.character.data.remote.api.toModel
import edu.iesam.aqui_no_hay_quien_viva_api.features.character.data.remote.api.toModels
import edu.iesam.aqui_no_hay_quien_viva_api.features.character.domain.Character
import org.koin.core.annotation.Single


@Single
class CharacterRemoteDataSource(private val apiClient: ApiClient) {

    private val apiService: CharacterApiService by lazy {
        apiClient.createService(CharacterApiService::class.java)
    }

    suspend fun getCharacters(): Result<List<Character>> {
        return apiCall { apiService.findAll() }
            .map { it.data.toModels() }
    }

    suspend fun getCharacterById(id: String): Result<Character> {
        return apiCall { apiService.findById(id) }
            .map { it.toModel() }
    }
}