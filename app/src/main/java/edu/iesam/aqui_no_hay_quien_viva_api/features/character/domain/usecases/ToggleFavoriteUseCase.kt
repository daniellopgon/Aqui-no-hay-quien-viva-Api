package edu.iesam.aqui_no_hay_quien_viva_api.features.character.domain.usecases

import edu.iesam.aqui_no_hay_quien_viva_api.features.character.data.local.dao.FavoriteDao
import edu.iesam.aqui_no_hay_quien_viva_api.features.character.data.local.entity.FavoriteEntity
import org.koin.core.annotation.Single

@Single
class ToggleFavoriteUseCase(
    private val favoriteDao: FavoriteDao
) {
    suspend operator fun invoke(id: String, isFavorite: Boolean) {
        if (isFavorite) {
            favoriteDao.removeFavorite(FavoriteEntity(id))
        } else {
            favoriteDao.addFavorite(FavoriteEntity(id))
        }
    }
}
