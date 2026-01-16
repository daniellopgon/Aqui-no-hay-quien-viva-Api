package edu.iesam.aqui_no_hay_quien_viva_api.features.character.data.local.xml

import edu.iesam.aqui_no_hay_quien_viva_api.features.character.domain.Character

fun Character.toXmlModel(createdAt: Long): CharacterXmlModel {
    return CharacterXmlModel(this, createdAt)
}

fun CharacterXmlModel.toModel(): Character {
    return this.character
}
