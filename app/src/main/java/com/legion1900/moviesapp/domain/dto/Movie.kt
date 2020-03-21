package com.legion1900.moviesapp.domain.dto

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/*
* Similar to Result, but contains no id and has fully prepared poster links.
* */
@Parcelize
data class Movie(
    val id: Int,
    val title: String,
    val originalTitle: String,
    val avgVote: Float,
    val posterPath: String?,
    val backdropPath: String?,
    val originalLanguage: String,
    val overview: String,
    val releaseDate: String?
) : Parcelable
