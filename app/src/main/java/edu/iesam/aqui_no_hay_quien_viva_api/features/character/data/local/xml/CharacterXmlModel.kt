package edu.iesam.aqui_no_hay_quien_viva_api.features.character.data.local.xml

import edu.iesam.aqui_no_hay_quien_viva_api.core.data.local.xml.XmlModel
import edu.iesam.aqui_no_hay_quien_viva_api.features.character.domain.Character
import kotlinx.serialization.Serializable


@Serializable
class CharacterXmlModel(
    val character: Character,
    val createdAt: Long
) : XmlModel {
    override fun getId(): String = character.id
    override fun getPersistedTime(): Long = createdAt
}
