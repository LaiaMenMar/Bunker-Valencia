/*package com.laiamenmar.bunkervalencia.core

import android.content.Context
import com.laiamenmar.bunkervalencia.utils.AnalyticsManager
import com.laiamenmar.bunkervalencia.utils.AuthManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    fun provideAnalyticsManager(context: Context): AnalyticsManager {
        return AnalyticsManager(context)
    }

    @Singleton

    @Provides
    fun provideAuthManager(context: Context): AuthManager {
        return AuthManager(context)
    }
}*/