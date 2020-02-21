package com.legion1900.moviesapp.data.impl

import com.legion1900.moviesapp.data.abs.MoviesRepository
import com.legion1900.moviesapp.data.impl.serialization.Response
import com.legion1900.moviesapp.data.impl.serialization.TMDBConfiguration
import com.legion1900.moviesapp.data.impl.services.TMDBService
import com.legion1900.moviesapp.domain.dto.MoviePage
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Named

class TMDBRepo @Inject constructor(
    private val service: TMDBService,
    @Named(MoviesRepository.POSTER_SIZE) val posterSize: TMDBConfiguration.Images.ImageSize,
    @Named(MoviesRepository.BACKDROP_SIZE) val backdropSize: TMDBConfiguration.Images.ImageSize
) : MoviesRepository {

    private var apiConfig: TMDBConfiguration? = null

    override fun loadMovies(page: Int): Single<MoviePage> {
        val query = TMDBService.buildFindQuery(page)
        var loading = loadMovies(query)
        if (apiConfig == null)
            loading = Single.zip(loading, loadConfig(), BiFunction { response, config ->
                apiConfig = config
                response
            })
        return loading.convert()
    }

    private fun loadMovies(query: Map<String, String>): Single<Response> {
        return service.loadPopularMovies(query).subscribeOn(Schedulers.io())
    }

    private fun Single<Response>.convert() = map { response ->
        with(apiConfig!!) {
            val currentPage = response.page
            val totalPages = response.totalPages
            val posterSize = imageApi.posterSizes[posterSize]
            val backdropSize = imageApi.posterSizes[backdropSize]
            val baseUrl = imageApi.baseUrl
            val movies = ResultMovieConverter(baseUrl, posterSize, backdropSize)
                .resultsToMovies(response.results)
            MoviePage(currentPage, totalPages, movies)
        }
    }

    private fun loadConfig(): Single<TMDBConfiguration> {
        return service.loadApiConfig().subscribeOn(Schedulers.io())
    }
}
