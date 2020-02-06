package com.legion1900.moviesapp.data.impl.serialization

import com.google.gson.annotations.SerializedName

data class TMDBConfiguration(
    @SerializedName("images") val imageApi: Images
) {
    data class Images(
        @SerializedName("secure_base_url") val baseUrl: String,
        @SerializedName("backdrop_sizes") val backdropSizes: Map<ImageSize, String>,
        @SerializedName("poster_sizes") val posterSizes: Map<ImageSize, String>
    ) {
        enum class ImageSize {
            SMALL, MEDIUM, LARGE, XLARGE
        }
    }
}