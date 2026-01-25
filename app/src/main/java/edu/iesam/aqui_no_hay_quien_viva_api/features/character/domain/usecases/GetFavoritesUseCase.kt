package edu.iesam.aqui_no_hay_quien_viva_api.features.character.domain.usecases

import edu.iesam.aqui_no_hay_quien_viva_api.features.character.data.local.dao.FavoriteDao
import edu.iesam.aqui_no_hay_quien_viva_api.features.character.data.local.entity.FavoriteEntity
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Single

@Single
class GetFavoritesUseCase(
    private val favoriteDao: FavoriteDao
) {
    operator fun invoke(): Flow<List<FavoriteEntity>> {
        return favoriteDao.getAllFavorites()
    }
}
