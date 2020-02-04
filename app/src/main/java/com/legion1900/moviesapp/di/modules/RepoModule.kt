package com.legion1900.moviesapp.di.modules

import com.legion1900.moviesapp.data.abs.MoviesRepository
import com.legion1900.moviesapp.data.impl.TMDBRepo
import dagger.Binds
import dagger.Module

@Module
abstract class RepoModule {
    @Binds
    abstract fun bindMoviesRepo(repo: TMDBRepo): MoviesRepository
}