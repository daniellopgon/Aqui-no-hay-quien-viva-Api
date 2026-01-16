package edu.iesam.aqui_no_hay_quien_viva_api.features.character.presentation

import edu.iesam.aqui_no_hay_quien_viva_api.core.domain.ErrorApp
import edu.iesam.aqui_no_hay_quien_viva_api.features.character.domain.Character


data class CharacterListUiState(
    val isLoading: Boolean = false,
    val characters: List<Character> = emptyList(),
    val error: ErrorApp? = null
) {
    val showContent: Boolean
        get() = !isLoading && error == null && characters.isNotEmpty()

    val showEmpty: Boolean
        get() = !isLoading && error == null && characters.isEmpty()
}
