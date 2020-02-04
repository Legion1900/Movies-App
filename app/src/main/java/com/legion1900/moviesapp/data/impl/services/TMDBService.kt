package com.legion1900.moviesapp.data.impl.services

import com.legion1900.moviesapp.data.impl.serialization.Response
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.QueryMap


interface TMDBService {
    @GET("discover/movie")
    fun loadPopularMovies(@QueryMap query: Map<String, String>): Single<Response>

    companion object {
        private const val PARAM_SORT_BY = "sort_by"
        private const val PARAM_API_KEY = "api_key"
        private const val PARAM_PAGE = "page"

        private const val DEFAULT_SORT_BY = "popularity.desc"
        private const val DEFAULT_PAGE = 1

        fun buildFindQuery(
            apiKey: String,
            page: Int = DEFAULT_PAGE,
            sortBy: String = DEFAULT_SORT_BY
        ): Map<String, String> = mapOf(
            PARAM_SORT_BY to sortBy,
            PARAM_API_KEY to apiKey,
            PARAM_PAGE to page.toString())
    }
}