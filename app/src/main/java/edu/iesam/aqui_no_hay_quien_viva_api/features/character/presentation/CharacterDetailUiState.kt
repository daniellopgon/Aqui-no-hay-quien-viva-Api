package edu.iesam.aqui_no_hay_quien_viva_api.features.character.presentation

import edu.iesam.aqui_no_hay_quien_viva_api.core.domain.ErrorApp
import edu.iesam.aqui_no_hay_quien_viva_api.features.character.domain.Character


sealed class CharacterDetailUiState {
    data object Loading : CharacterDetailUiState()
    data class Success(val character: Character) : CharacterDetailUiState()
    data class Error(val error: ErrorApp) : CharacterDetailUiState()
}
