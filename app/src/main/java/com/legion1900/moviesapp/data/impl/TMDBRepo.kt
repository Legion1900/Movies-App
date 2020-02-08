package com.legion1900.moviesapp.data.impl

import com.legion1900.moviesapp.data.abs.MoviesRepository
import com.legion1900.moviesapp.domain.abs.dto.MovieRequest
import com.legion1900.moviesapp.data.impl.serialization.TMDBConfiguration
import com.legion1900.moviesapp.data.impl.services.TMDBService
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Named

class TMDBRepo @Inject constructor(
    @Named(MoviesRepository.API_KEY) private val apiKey: String,
    private val service: TMDBService
) : MoviesRepository {

    private var apiConfig: TMDBConfiguration? = null

    override fun loadMovies(page: Int): Single<MovieRequest> {
        val query = TMDBService.buildFindQuery(apiKey, page)
        val movies = loadMovies(query)
        return if (apiConfig == null) movies.delaySubscription(loadConfig()) else movies
    }

    private fun loadMovies(query: Map<String, String>): Single<MovieRequest> {
        return service.loadPopularMovies(query).subscribeOn(Schedulers.io()).map { response ->
            with(apiConfig!!) {
                val currentPage = response.page
                val totalPages = response.totalPages
                val posterSize = imageApi.posterSizes[TMDBConfiguration.Images.ImageSize.MEDIUM]
                val backdropSize = imageApi.posterSizes[TMDBConfiguration.Images.ImageSize.LARGE]
                val baseUrl = imageApi.baseUrl
                val movies = ResultMovieConverter(baseUrl, posterSize, backdropSize)
                    .resultsToMovies(response.results)
                MovieRequest(currentPage, totalPages, movies)
            }
        }
    }

    private fun loadConfig(): Single<TMDBConfiguration> {
        return service.loadApiConfig(apiKey).subscribeOn(Schedulers.io()).doOnSuccess {
            apiConfig = it
        }
    }
}
