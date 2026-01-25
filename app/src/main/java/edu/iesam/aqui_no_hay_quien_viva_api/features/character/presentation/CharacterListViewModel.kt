package edu.iesam.aqui_no_hay_quien_viva_api.features.character.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.iesam.aqui_no_hay_quien_viva_api.core.domain.ErrorApp
import edu.iesam.aqui_no_hay_quien_viva_api.features.character.domain.Character
import edu.iesam.aqui_no_hay_quien_viva_api.features.character.domain.usecases.FetchCharactersUseCase
import edu.iesam.aqui_no_hay_quien_viva_api.features.character.presentation.CharacterWithFavorite
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel


@KoinViewModel
class CharacterListViewModel(
    private val fetchCharactersUseCase: FetchCharactersUseCase,
    @org.koin.core.annotation.Named("favorites") private val favoritesStorage: edu.iesam.aqui_no_hay_quien_viva_api.core.data.local.xml.XmlCacheStorage<edu.iesam.aqui_no_hay_quien_viva_api.features.character.data.local.xml.FavoriteXmlModel>
) : ViewModel() {

    private val _uiState = MutableStateFlow(CharacterListUiState())
    val uiState: StateFlow<CharacterListUiState> = _uiState.asStateFlow()

    private var allCharacters: List<Character> = emptyList()
    private var favoriteIds: Set<String> = emptySet()
    private var showOnlyFavorites: Boolean = false

    init {
        loadCharacters()
    }

    fun loadCharacters() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            // Load favorites first
            favoritesStorage.obtainAll().onSuccess { favorites ->
                favoriteIds = favorites.map { it.favoriteId }.toSet()
            }

            fetchCharactersUseCase().fold(
                onSuccess = { characters ->
                    allCharacters = characters
                    emitCombinedList()
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = error as? ErrorApp
                        )
                    }
                }
            )
        }
    }

    private fun emitCombinedList() {
        val combined = allCharacters.map { character ->
            CharacterWithFavorite(character, favoriteIds.contains(character.id))
        }
        val filtered = if (showOnlyFavorites) {
            combined.filter { it.isFavorite }
        } else {
            combined
        }
        _uiState.update {
            it.copy(
                isLoading = false,
                characters = filtered,
                showOnlyFavorites = showOnlyFavorites
            )
        }
    }

    fun toggleFavorite(character: Character) {
        viewModelScope.launch(Dispatchers.IO) {
            val isFavorite = favoriteIds.contains(character.id)
            if (isFavorite) {
                favoriteIds = favoriteIds - character.id
                favoritesStorage.delete(character.id)
            } else {
                favoriteIds = favoriteIds + character.id
                favoritesStorage.save(edu.iesam.aqui_no_hay_quien_viva_api.features.character.data.local.xml.FavoriteXmlModel(character.id))
            }
            emitCombinedList()
        }
    }

    fun setFavoritesFilter(showFavorites: Boolean) {
        showOnlyFavorites = showFavorites
        emitCombinedList()
    }

    fun retry() {
        loadCharacters()
    }
}
