package com.legion1900.moviesapp.di.modules

import com.legion1900.moviesapp.domain.abs.GetMoviesUseCase
import com.legion1900.moviesapp.domain.abs.PickMovieUseCase
import com.legion1900.moviesapp.domain.impl.GetMoviesUseCaseImpl
import com.legion1900.moviesapp.domain.impl.PickMovieUseCaseImpl
import dagger.Binds
import dagger.Module

@Module
abstract class UseCaseModule {
    @Binds
    abstract fun bindGetMoviesUseCaseImpl(useCase: GetMoviesUseCaseImpl): GetMoviesUseCase

    @Binds
    abstract fun bindPickMovieUseCaseImpl(useCase: PickMovieUseCaseImpl): PickMovieUseCase
}