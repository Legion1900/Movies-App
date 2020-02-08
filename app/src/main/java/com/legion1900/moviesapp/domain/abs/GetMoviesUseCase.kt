package com.legion1900.moviesapp.domain.abs

import com.legion1900.moviesapp.domain.abs.dto.Movie

interface GetMoviesUseCase {
    val hasNextPage: Boolean

    fun subscribe(
        onStart: () -> Unit,
        onSuccess: (List<Movie>) -> Unit,
        onError: (Throwable) -> Unit
    )

    fun getMovies(page: Int = 1)

    fun getNextPage()

    fun dispose()
}
