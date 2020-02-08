package com.legion1900.moviesapp.di

import com.legion1900.moviesapp.di.modules.NetworkModule
import com.legion1900.moviesapp.di.modules.RepoModule
import com.legion1900.moviesapp.di.modules.UseCaseModule
import com.legion1900.moviesapp.view.base.BaseFragment
import com.legion1900.moviesapp.view.di.FragmentComponent
import com.legion1900.moviesapp.view.di.modules.ViewModelModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        ViewModelModule::class,
        NetworkModule::class,
        RepoModule::class,
        UseCaseModule::class,
        FragmentComponent.InstallModule::class
    ]
)
interface AppComponent {
    fun inject(fragment: BaseFragment)
    fun fragmentComponentBuilder(): FragmentComponent.Builder
}
