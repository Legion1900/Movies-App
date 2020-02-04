package com.legion1900.moviesapp.data.abs

import com.legion1900.moviesapp.data.impl.serialization.Movie
import io.reactivex.Single

interface MoviesRepository {
    val hasNextPage: Boolean
    fun getMovies(page: Int = 1): Single<List<Movie>>
    fun getNextPage(): Single<List<Movie>>

    companion object {
        const val API_KEY = "api_key"
    }
}
