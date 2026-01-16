package edu.iesam.aqui_no_hay_quien_viva_api.features.character.domain.usecases

import org.koin.core.annotation.Single
import edu.iesam.aqui_no_hay_quien_viva_api.features.character.domain.Character
import edu.iesam.aqui_no_hay_quien_viva_api.features.character.domain.CharacterRepository


@Single
class GetCharacterByIdUseCase(
    private val repository: CharacterRepository
) {
    suspend operator fun invoke(id: String): Result<Character> {
        return repository.findById(id)
    }
}
