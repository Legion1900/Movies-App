package com.legion1900.moviesapp.view.fragments.mainscreen.di

import androidx.lifecycle.ViewModel
import com.legion1900.moviesapp.view.base.ViewModelFactory
import com.legion1900.moviesapp.view.fragments.mainscreen.PopularMoviesFragment
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Provider

@Module(
    includes = [
        RepoModule::class,
        ViewModelModule::class
    ]
)
object PopularMoviesViewModelFactoryModule {
    @JvmStatic
    @Provides
    @Named(PopularMoviesFragment.QUALIFIER)
    fun providePopularMovieScreenViewModelFactory(
        map: @JvmSuppressWildcards Map<Class<out ViewModel>, Provider<ViewModel>>
    ) = ViewModelFactory(map)
}
