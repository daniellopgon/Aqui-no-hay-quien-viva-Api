package edu.iesam.aqui_no_hay_quien_viva_api.core.presentation.errors

import android.content.Context
import edu.iesam.aqui_no_hay_quien_viva_api.core.domain.ErrorApp


class ErrorAppFactory(private val context: Context) {

    fun build(errorApp: ErrorApp, onRetry: () -> Unit): ErrorAppUI {
        return when (errorApp) {
            is ErrorApp.InternetConnectionError -> ConnectionErrorAppUI(context, onRetry)
            is ErrorApp.ServerError -> ServerErrorAppUI(context, onRetry)
            is ErrorApp.CacheError -> ServerErrorAppUI(context, onRetry)
            is ErrorApp.NotFoundError -> ServerErrorAppUI(context, onRetry)
            is ErrorApp.UnknownError -> UnknownErrorAppUI(context, onRetry)
        }
    }
}
