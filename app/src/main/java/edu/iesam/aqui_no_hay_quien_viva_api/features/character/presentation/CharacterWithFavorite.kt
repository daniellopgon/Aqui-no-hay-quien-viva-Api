package edu.iesam.aqui_no_hay_quien_viva_api.features.character.presentation

import edu.iesam.aqui_no_hay_quien_viva_api.features.character.domain.Character

data class CharacterWithFavorite(
    val character: Character,
    val isFavorite: Boolean
)
