package edu.iesam.aqui_no_hay_quien_viva_api.core.di

import android.content.Context
import edu.iesam.aqui_no_hay_quien_viva_api.core.data.local.xml.XmlCacheStorage
import edu.iesam.aqui_no_hay_quien_viva_api.features.character.data.local.xml.CharacterXmlModel
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

import edu.iesam.aqui_no_hay_quien_viva_api.core.data.remote.api.ApiClient

@Module
@ComponentScan("edu.iesam.aqui_no_hay_quien_viva_api")
class AppModule {

    @Single
    fun provideCharacterXmlCacheStorage(context: Context): XmlCacheStorage<CharacterXmlModel> {
        return XmlCacheStorage(
            context,
            "character_cache",
            CharacterXmlModel.serializer()
        )
    }

    @Single
    fun provideApiClient(): ApiClient {
        return ApiClient("http://api.anhqv-stats.es/api/")
    }
}
