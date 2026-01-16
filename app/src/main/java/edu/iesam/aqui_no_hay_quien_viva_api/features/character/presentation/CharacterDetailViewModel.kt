package edu.iesam.aqui_no_hay_quien_viva_api.features.character.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.iesam.aqui_no_hay_quien_viva_api.core.domain.ErrorApp
import edu.iesam.aqui_no_hay_quien_viva_api.features.character.domain.usecases.GetCharacterByIdUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel


@KoinViewModel
class CharacterDetailViewModel(
    private val getCharacterByIdUseCase: GetCharacterByIdUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<CharacterDetailUiState>(CharacterDetailUiState.Loading)
    val uiState: StateFlow<CharacterDetailUiState> = _uiState.asStateFlow()

    fun loadCharacter(characterId: String) {
        viewModelScope.launch {
            _uiState.value = CharacterDetailUiState.Loading

            getCharacterByIdUseCase(characterId).fold(
                onSuccess = { character ->
                    _uiState.value = CharacterDetailUiState.Success(character)
                },
                onFailure = { error ->
                    _uiState.value = CharacterDetailUiState.Error(
                        error as? ErrorApp ?: ErrorApp.UnknownError(error.message)
                    )
                }
            )
        }
    }

    fun retry(characterId: String) {
        loadCharacter(characterId)
    }
}
