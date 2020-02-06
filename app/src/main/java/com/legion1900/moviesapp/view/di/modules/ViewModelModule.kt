package com.legion1900.moviesapp.view.di.modules

import androidx.lifecycle.ViewModel
import com.legion1900.moviesapp.di.ViewModelKey
import com.legion1900.moviesapp.view.mainscreen.PopularMoviesViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(PopularMoviesViewModel::class)
    abstract fun bindPopularFilmsViewModel(vm: PopularMoviesViewModel): ViewModel
}