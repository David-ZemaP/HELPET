package com.ucb.helpet.di

import com.google.firebase.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.remoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import com.ucb.helpet.R
import org.koin.dsl.module

val remoteConfigModule = module {
    single<FirebaseRemoteConfig> {
        val remoteConfig = Firebase.remoteConfig
        val configSettings = remoteConfigSettings {
            // Set to 0 for development to fetch frequently
            minimumFetchIntervalInSeconds = 0
        }
        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)
        remoteConfig
    }
}
