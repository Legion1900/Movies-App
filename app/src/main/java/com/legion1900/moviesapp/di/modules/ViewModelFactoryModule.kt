package com.legion1900.moviesapp.di.modules

import androidx.lifecycle.ViewModelProvider
import com.legion1900.moviesapp.di.ViewModelFactory
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class ViewModelFactoryModule {
    @Singleton
    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}