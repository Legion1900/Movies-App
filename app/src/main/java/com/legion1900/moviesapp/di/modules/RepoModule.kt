package com.legion1900.moviesapp.di.modules

import com.legion1900.moviesapp.data.abs.MoviePager
import com.legion1900.moviesapp.data.abs.MoviesRepository
import com.legion1900.moviesapp.data.impl.RxMoviePager
import com.legion1900.moviesapp.data.impl.TMDBRepo
import dagger.Binds
import dagger.Module

@Module
interface RepoModule {
    @Binds
    fun bindMoviesRepo(repo: TMDBRepo): MoviesRepository

    @Binds
    fun bindMoviePager(pager: RxMoviePager): MoviePager
}
