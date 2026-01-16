package edu.iesam.aqui_no_hay_quien_viva_api.features.character.data.remote.api

import edu.iesam.aqui_no_hay_quien_viva_api.features.character.domain.Character

fun CharacterApiModel.toModel(): Character {
    return Character(
        id = this.id.toString(),
        name = this.name,
        surname = this.surname,
        secondSurname = this.secondSurname,
        shortname = this.shortname,
        nickname = this.nickname,
        imageUrl = this.imageUrl,
        imageAlt = this.imageAlt,
        slug = this.slug
    )
}


fun List<CharacterApiModel>.toModels(): List<Character> = map { it.toModel() }