package com.legion1900.moviesapp.di

import android.app.Application
import com.legion1900.moviesapp.view.di.FragmentComponent

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.create()
        fragmentComponent = appComponent.fragmentComponentBuilder().build()
    }

    companion object {
        lateinit var appComponent: AppComponent
            private set
        lateinit var fragmentComponent: FragmentComponent
            private set
    }
}
