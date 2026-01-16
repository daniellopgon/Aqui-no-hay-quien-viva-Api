package edu.iesam.aqui_no_hay_quien_viva_api.features.character.domain

import kotlinx.serialization.Serializable


@Serializable
data class Character(
    val id: String,
    val name: String,
    val surname: String,
    val secondSurname: String,
    val shortname: String,
    val nickname: String,
    val imageUrl: String,
    val imageAlt: String,
    val slug: String
)
