package com.legion1900.moviesapp.domain.abs.dto

import android.os.Parcel
import android.os.Parcelable

/*
* Similar to Result, but contains no id and fully prepared poster links.
* */
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
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readFloat(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(title)
        parcel.writeString(originalTitle)
        parcel.writeFloat(avgVote)
        parcel.writeString(posterPath)
        parcel.writeString(backdropPath)
        parcel.writeString(originalLanguage)
        parcel.writeString(overview)
        parcel.writeString(releaseDate)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Movie> {
        override fun createFromParcel(parcel: Parcel): Movie {
            return Movie(parcel)
        }

        override fun newArray(size: Int): Array<Movie?> {
            return arrayOfNulls(size)
        }
    }
}
