package com.legion1900.moviesapp.domain.impl

import com.legion1900.moviesapp.domain.abs.PickMovieUseCase
import com.legion1900.moviesapp.domain.abs.dto.Movie
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PickMovieUseCaseImpl @Inject constructor() : PickMovieUseCase {

    private var movie: Movie? = null

    override fun pick(movie: Movie) {
        this.movie = movie
    }

    override fun getPicked(): Movie {
        return movie ?: throw IllegalStateException("pick(Movie) must be called before getPicked()")
    }
}
