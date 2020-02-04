package com.legion1900.moviesapp.view.di.modules

import androidx.lifecycle.ViewModel
import com.legion1900.moviesapp.di.ViewModelKey
import com.legion1900.moviesapp.view.mainscreen.PopularFilmsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import javax.inject.Provider

@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(PopularFilmsViewModel::class)
    abstract fun bindPopularFilmsViewModel(vm: PopularFilmsViewModel): ViewModel
}