package edu.iesam.aqui_no_hay_quien_viva_api.features.character.data.remote

import com.google.gson.annotations.SerializedName


data class CharacterApiModel(
    val id: Int,
    val name: String,
    val surname: String,

    @SerializedName("second_surname")
    val secondSurname: String,

    val shortname: String,
    val nickname: String,

    @SerializedName("image_url")
    val imageUrl: String,

    @SerializedName("image_alt")
    val imageAlt: String,

    val slug: String
)
