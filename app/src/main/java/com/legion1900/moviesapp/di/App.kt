package com.legion1900.moviesapp.di

import android.app.Application

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.create()
    }

    companion object {
        lateinit var appComponent: AppComponent
            private set
    }
}
