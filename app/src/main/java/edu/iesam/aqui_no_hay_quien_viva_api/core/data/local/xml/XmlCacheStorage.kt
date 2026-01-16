package edu.iesam.aqui_no_hay_quien_viva_api.core.data.local.xml

import android.content.Context
import androidx.core.content.edit
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json


class XmlCacheStorage<T : XmlModel>(
    private val context: Context,
    private val nameXml: String,
    private val dataSerializer: KSerializer<T>
) {
    private val prefs = context.getSharedPreferences(nameXml, Context.MODE_PRIVATE)

    private val json = Json {
        ignoreUnknownKeys = true
        prettyPrint = true
        isLenient = true
    }

    fun save(model: T): Result<Boolean> {
        return runCatching {
            val jsonString = json.encodeToString(dataSerializer, model)
            prefs.edit { putString(model.getId(), jsonString) }
            true
        }
    }

    fun save(models: List<T>): Result<Boolean> {
        models.forEach { model ->
            save(model).onFailure { return Result.failure(it) }
        }
        return Result.success(true)
    }

    fun obtainAll(): Result<List<T>> {
        return runCatching {
            prefs.all.map { entry ->
                json.decodeFromString(dataSerializer, entry.value as String)
            }
        }
    }

    fun obtain(id: String): Result<T?> {
        return runCatching {
            prefs.getString(id, null)?.let {
                json.decodeFromString(dataSerializer, it)
            }
        }
    }

    fun delete(id: String): Result<Boolean> {
        return runCatching {
            prefs.edit { remove(id) }
            true
        }
    }

    fun clear() {
        prefs.edit { clear() }
    }
}
