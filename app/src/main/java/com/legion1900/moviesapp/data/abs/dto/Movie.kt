package com.legion1900.moviesapp.data.abs.dto

/*
* Similar to Result, but contains no id and fully prepared poster links.
* */
data class Movie(
    val title: String,
    val originalTitle: String,
    val avgVote: Float,
    val posterPath: String?,
    val backdropPath: String?,
    val originalLanguage: String,
    val overview: String,
    val releaseDate: String
)