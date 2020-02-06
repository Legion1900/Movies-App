package com.legion1900.moviesapp.data.impl.serialization

import com.google.gson.annotations.SerializedName

/*
* Represents one result from array of results
* */
data class Result(
    val id: Int,
    @SerializedName("poster_path") val posterPath: String?,
    @SerializedName("backdrop_path") val backdropPath: String?,
    @SerializedName("original_language") val originalLanguage: String,
    @SerializedName("original_title") val originalTitle: String,
    val title: String,
    @SerializedName("vote_average") val avgVote: Float,
    val overview: String,
    @SerializedName("release_date") val releaseDate: String
)
