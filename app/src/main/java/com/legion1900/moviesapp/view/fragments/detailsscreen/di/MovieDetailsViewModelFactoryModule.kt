package com.legion1900.moviesapp.view.fragments.detailsscreen.di

import androidx.lifecycle.ViewModel
import com.legion1900.moviesapp.view.base.ViewModelFactory
import com.legion1900.moviesapp.view.fragments.detailsscreen.MovieDetailsFragment.Companion.QUALIFIER
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Provider

@Module(
    includes = [
        ViewModelModule::class
    ]
)
object MovieDetailsViewModelFactoryModule {
    @JvmStatic
    @Provides
    @Named(QUALIFIER)
    fun provideMovieDetailsScreenViewModelFactory(
        map: @JvmSuppressWildcards Map<Class<out ViewModel>, Provider<ViewModel>>
    ) = ViewModelFactory(map)
}
