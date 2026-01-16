package edu.iesam.aqui_no_hay_quien_viva_api.core.data.remote.api

import com.google.gson.annotations.SerializedName

data class PaginatedResponse<T>(
    @SerializedName("data")
    val data: List<T>
)
