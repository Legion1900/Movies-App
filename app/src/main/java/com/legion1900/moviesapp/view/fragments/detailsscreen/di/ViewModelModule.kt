package com.legion1900.moviesapp.view.fragments.detailsscreen.di

import androidx.lifecycle.ViewModel
import com.legion1900.moviesapp.di.ViewModelKey
import com.legion1900.moviesapp.view.fragments.detailsscreen.MovieDetailsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(MovieDetailsViewModel::class)
    fun bindMovieDetailsViewModel(vm: MovieDetailsViewModel): ViewModel
}
