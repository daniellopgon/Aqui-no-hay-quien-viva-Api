package edu.iesam.aqui_no_hay_quien_viva_api.features.character.domain


interface CharacterRepository {
    suspend fun findAll(): Result<List<Character>>
    suspend fun findById(id: String): Result<Character>
}
