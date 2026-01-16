package edu.iesam.aqui_no_hay_quien_viva_api.core

import android.app.Application
import edu.iesam.aqui_no_hay_quien_viva_api.core.di.AppModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.ksp.generated.module


class AquiNoHayQuienVivaApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@AquiNoHayQuienVivaApp)
            modules(AppModule().module)
        }
    }
}
