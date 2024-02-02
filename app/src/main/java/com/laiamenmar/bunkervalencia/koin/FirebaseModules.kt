package com.laiamenmar.bunkervalencia.koin

import com.laiamenmar.bunkervalencia.utils.AnalyticsManager
import com.laiamenmar.bunkervalencia.utils.AuthManager
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val FirebaseModules = module {
    singleOf(::AnalyticsManager)
    singleOf(::AuthManager)
}

// mail soy.moddy@gmail.com
// pass meEncantaAyudarXD.