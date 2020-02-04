package com.legion1900.moviesapp.di

import com.legion1900.moviesapp.view.di.FragmentComponent
import com.legion1900.moviesapp.view.di.ViewModelModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ViewModelModule::class, FragmentComponent.InstallModule::class])
interface AppComponent {
    fun fragmentComponentBuilder(): FragmentComponent.Builder
}
