package com.legion1900.moviesapp.domain.abs.dto

data class MoviePage(
    val page: Int,
    val totalPages: Int,
    val movies: List<Movie>
)
