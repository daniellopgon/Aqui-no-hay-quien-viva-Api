package edu.iesam.aqui_no_hay_quien_viva_api.core.domain


sealed class ErrorApp : Throwable() {
    data object InternetConnectionError : ErrorApp()
    data object ServerError : ErrorApp()
    data object CacheError : ErrorApp()
    data object NotFoundError : ErrorApp()
    data class UnknownError(override val message: String? = null) : ErrorApp()
}
