package edu.iesam.aqui_no_hay_quien_viva_api.features.character.data.local.xml

import edu.iesam.aqui_no_hay_quien_viva_api.core.data.local.xml.XmlModel
import kotlinx.serialization.Serializable

@Serializable
class FavoriteXmlModel(
    val favoriteId: String,
    val createdAt: Long = System.currentTimeMillis()
) : XmlModel {
    override fun getId(): String = favoriteId
    override fun getPersistedTime(): Long = createdAt
}
