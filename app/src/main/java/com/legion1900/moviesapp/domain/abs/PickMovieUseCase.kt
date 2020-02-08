package com.legion1900.moviesapp.domain.abs

import com.legion1900.moviesapp.domain.abs.dto.Movie

interface PickMovieUseCase {
    fun pick(movie: Movie)
    fun getPicked(): Movie
}