package com.legion1900.moviesapp.view.fragments.mainscreen.di

import androidx.lifecycle.ViewModel
import com.legion1900.moviesapp.di.ViewModelKey
import com.legion1900.moviesapp.view.fragments.mainscreen.PopularMoviesViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(PopularMoviesViewModel::class)
    fun bindPopularMoviesViewModel(vm: PopularMoviesViewModel): ViewModel
}
