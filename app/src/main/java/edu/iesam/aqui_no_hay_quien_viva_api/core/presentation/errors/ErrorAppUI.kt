package edu.iesam.aqui_no_hay_quien_viva_api.core.presentation.errors

import android.content.Context
import edu.iesam.aqui_no_hay_quien_viva_api.R


interface ErrorAppUI {
    fun getImageError(): Int
    fun getTitleError(): String
    fun getDescriptionError(): String
    fun getActionRetry()
}


class ConnectionErrorAppUI(
    private val context: Context,
    private val onClick: (() -> Unit)?
) : ErrorAppUI {

    override fun getImageError(): Int = R.drawable.img_network_error

    override fun getTitleError(): String =
        context.getString(R.string.title_error_connection)

    override fun getDescriptionError(): String =
        context.getString(R.string.description_error_connection)

    override fun getActionRetry() {
        onClick?.invoke()
    }
}


class ServerErrorAppUI(
    private val context: Context,
    private val onClick: (() -> Unit)?
) : ErrorAppUI {

    override fun getImageError(): Int = R.drawable.img_unknown_error

    override fun getTitleError(): String =
        context.getString(R.string.title_error_server)

    override fun getDescriptionError(): String =
        context.getString(R.string.description_error_server)

    override fun getActionRetry() {
        onClick?.invoke()
    }
}


class UnknownErrorAppUI(
    private val context: Context,
    private val onClick: (() -> Unit)?
) : ErrorAppUI {

    override fun getImageError(): Int = R.drawable.img_unknown_error

    override fun getTitleError(): String =
        context.getString(R.string.title_error_unknown)

    override fun getDescriptionError(): String =
        context.getString(R.string.description_error_unknown)

    override fun getActionRetry() {
        onClick?.invoke()
    }
}
