package edu.iesam.aqui_no_hay_quien_viva_api.features.character.data

import edu.iesam.aqui_no_hay_quien_viva_api.features.character.data.local.CharacterLocalDataSource
import edu.iesam.aqui_no_hay_quien_viva_api.features.character.data.remote.CharacterRemoteDataSource
import edu.iesam.aqui_no_hay_quien_viva_api.features.character.domain.Character
import edu.iesam.aqui_no_hay_quien_viva_api.features.character.domain.CharacterRepository
import org.koin.core.annotation.Single

@Single(binds = [CharacterRepository::class])
class CharacterDataRepository(
    private val remoteDataSource: CharacterRemoteDataSource,
    private val localDataSource: CharacterLocalDataSource
) : CharacterRepository {

    override suspend fun findAll(): Result<List<Character>> {
        val localResult = localDataSource.findAll()
        return if (localResult.getOrDefault(emptyList()).isEmpty()) {
            remoteDataSource.getCharacters().onSuccess {
                localDataSource.clear()
                localDataSource.save(it)
            }
        } else {
            localResult
        }
    }

    override suspend fun findById(id: String): Result<Character> {
        localDataSource.find(id).getOrNull()?.let { character ->
            return Result.success(character)
        }
        return remoteDataSource.getCharacterById(id).onSuccess {
            localDataSource.save(it)
        }
    }
}
