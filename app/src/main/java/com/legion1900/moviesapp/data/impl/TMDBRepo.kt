package com.legion1900.moviesapp.data.impl

import com.legion1900.moviesapp.data.abs.MoviesRepository
import com.legion1900.moviesapp.data.abs.dto.Movie
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

    override val hasNextPage: Boolean
        get() = totalPages > currentPage

    private var apiConfig: TMDBConfiguration? = null

    private var totalPages: Int = 0

    private var currentPage = 0

    override fun getMovies(page: Int): Single<List<Movie>> {
        val query = TMDBService.buildFindQuery(apiKey, page)
        val movies = loadMovies(query)
        return if (apiConfig == null) movies.delaySubscription(loadConfig()) else movies
    }

    private fun loadMovies(query: Map<String, String>): Single<List<Movie>> {
        return service.loadPopularMovies(query).subscribeOn(Schedulers.io()).map { response ->
            with(apiConfig!!) {
                totalPages = response.totalPages
                currentPage = response.page
                val posterSize = imageApi.posterSizes[TMDBConfiguration.Images.ImageSize.MEDIUM]
                val backdropSize = imageApi.posterSizes[TMDBConfiguration.Images.ImageSize.LARGE]
                val baseUrl = imageApi.baseUrl
                ResultMovieConverter(baseUrl, posterSize, backdropSize)
                    .resultsToMovies(response.results)
            }
        }
    }

    private fun loadConfig(): Single<TMDBConfiguration> {
        return service.loadApiConfig(apiKey).subscribeOn(Schedulers.io()).doOnSuccess {
            apiConfig = it
        }
    }

    override fun getNextPage(): Single<List<Movie>> = getMovies(++currentPage)
}
