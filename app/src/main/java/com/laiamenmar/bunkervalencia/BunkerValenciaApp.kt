package com.laiamenmar.bunkervalencia

import android.app.Application
import com.laiamenmar.bunkervalencia.koin.FirebaseModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class BunkerValenciaApp:Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@BunkerValenciaApp)
            androidLogger()
            modules(FirebaseModules)
        }
    }
}