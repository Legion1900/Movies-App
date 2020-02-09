package com.legion1900.moviesapp.di

import android.app.Application
import com.legion1900.moviesapp.data.impl.serialization.TMDBConfiguration

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder()
            .setPosterSize(TMDBConfiguration.Images.ImageSize.MEDIUM)
            .setBackdropSize(TMDBConfiguration.Images.ImageSize.XLARGE)
            .build()
    }

    companion object {
        lateinit var appComponent: AppComponent
            private set
    }
}
