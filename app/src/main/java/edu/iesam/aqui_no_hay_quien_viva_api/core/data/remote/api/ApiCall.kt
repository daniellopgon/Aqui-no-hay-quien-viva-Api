package edu.iesam.aqui_no_hay_quien_viva_api.core.data.remote.api

import edu.iesam.aqui_no_hay_quien_viva_api.core.domain.ErrorApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.io.IOException


suspend fun <T> apiCall(
    call: suspend () -> Response<T>
): Result<T> {
    return withContext(Dispatchers.IO) {
        try {
            val response = call()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(ErrorApp.ServerError)
            }
        } catch (e: IOException) {
            Result.failure(ErrorApp.InternetConnectionError)
        } catch (e: Exception) {
            Result.failure(ErrorApp.ServerError)
        }
    }
}