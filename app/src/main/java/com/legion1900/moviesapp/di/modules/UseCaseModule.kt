package com.legion1900.moviesapp.di.modules

import com.legion1900.moviesapp.domain.abs.GetMoviesUseCase
import com.legion1900.moviesapp.domain.impl.PagedGetMovies
import dagger.Binds
import dagger.Module

@Module
abstract class UseCaseModule {
    @Binds
    abstract fun bindGetMoviesUseCase(useCase: PagedGetMovies): GetMoviesUseCase
}