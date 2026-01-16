package edu.iesam.aqui_no_hay_quien_viva_api.features.character.data.local

import edu.iesam.aqui_no_hay_quien_viva_api.core.data.local.xml.XmlCacheStorage
import edu.iesam.aqui_no_hay_quien_viva_api.core.domain.ErrorApp
import edu.iesam.aqui_no_hay_quien_viva_api.features.character.data.local.xml.CharacterXmlModel
import edu.iesam.aqui_no_hay_quien_viva_api.features.character.data.local.xml.toModel
import edu.iesam.aqui_no_hay_quien_viva_api.features.character.data.local.xml.toXmlModel
import edu.iesam.aqui_no_hay_quien_viva_api.features.character.domain.Character
import org.koin.core.annotation.Single

@Single
class CharacterLocalDataSource(
    private val xmlCacheStorage: XmlCacheStorage<CharacterXmlModel>
) {
    companion object {
        private const val CACHE_EXPIRATION_MS = 60 * 60 * 1000L // 1 hour
    }

    fun findAll(): Result<List<Character>> {
        return xmlCacheStorage.obtainAll().fold(
            onSuccess = { models ->
                val characters = models
                    .filter { isValid(it) }
                    .map { it.toModel() }
                Result.success(characters)
            },
            onFailure = { Result.failure(ErrorApp.CacheError) }
        )
    }

    fun find(characterId: String): Result<Character?> {
        return xmlCacheStorage.obtain(characterId).fold(
            onSuccess = { model ->
                if (model != null && isValid(model)) {
                    Result.success(model.character)
                } else {
                    Result.success(null)
                }
            },
            onFailure = { Result.failure(ErrorApp.CacheError) }
        )
    }

    fun save(characters: List<Character>): Result<Boolean> {
        val createdAt = System.currentTimeMillis()
        return xmlCacheStorage.save(characters.map { it.toXmlModel(createdAt) })
    }

    fun save(character: Character): Result<Boolean> {
        val createdAt = System.currentTimeMillis()
        return xmlCacheStorage.save(character.toXmlModel(createdAt))
    }

    fun clear() = xmlCacheStorage.clear()

    private fun isValid(model: CharacterXmlModel): Boolean {
        val currentTime = System.currentTimeMillis()
        return (currentTime - model.getPersistedTime()) < CACHE_EXPIRATION_MS
    }
}
