package com.legion1900.moviesapp.data.impl

import com.legion1900.moviesapp.domain.abs.dto.Movie
import com.legion1900.moviesapp.data.impl.serialization.Result

class ResultMovieConverter(
    private val imgBaseUrl: String,
    private val posterSize: String?,
    private val backdropSize: String?
) {
    private val builder = StringBuilder()

    fun resultsToMovies(results: List<Result>): List<Movie> {
        val movies = mutableListOf<Movie>()
        for (r in results)
            movies += r.toMovie()
        return movies
    }

    private fun Result.toMovie(): Movie = with(this) {
        val poster = posterPath?.properPath(posterSize)
        val backdrop = backdropPath?.properPath(backdropSize)
        Movie(
            title = title,
            originalTitle = originalTitle,
            avgVote = avgVote,
            posterPath = poster,
            backdropPath = backdrop,
            originalLanguage = originalLanguage,
            overview = overview,
            releaseDate = releaseDate
        )
    }

    private fun String.properPath(size: String?): String? {
        return size?.let {
            builder.append(imgBaseUrl)
            builder.append(it)
            builder.append("/")
            builder.append(this)
            builder.toString().apply { builder.clear() }
        }
    }
}