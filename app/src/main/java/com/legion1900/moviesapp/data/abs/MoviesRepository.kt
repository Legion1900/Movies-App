package com.legion1900.moviesapp.data.abs

import com.legion1900.moviesapp.domain.abs.dto.MovieRequest
import io.reactivex.Single

interface MoviesRepository {
    fun loadMovies(page: Int): Single<MovieRequest>

    companion object {
        const val API_KEY = "api_key"
        const val POSTER_SIZE = "poster_size"
        const val BACKDROP_SIZE = "backdrop_size"
    }
}
