package com.legion1900.moviesapp.data

import com.google.gson.annotations.SerializedName

/*
* Root class for response.
* */
data class Response(
    val page: Int,
    @SerializedName("total_results") val totalResults: Int,
    @SerializedName("total_pages") val totalPages: Int,
    @SerializedName("results") val films: ArrayList<Results>
)