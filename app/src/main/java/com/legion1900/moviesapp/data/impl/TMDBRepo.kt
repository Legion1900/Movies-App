package com.legion1900.moviesapp.data.impl

import com.legion1900.moviesapp.data.abs.MoviesRepository
import com.legion1900.moviesapp.data.impl.serialization.Movie
import com.legion1900.moviesapp.data.impl.services.TMDBService
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Named

class TMDBRepo @Inject constructor(
    @Named(MoviesRepository.API_KEY)
    private val apiKey: String,
    private val service: TMDBService
) : MoviesRepository {

    override val hasNextPage: Boolean
        get() = totalPages > currentPage

    private var totalPages: Int = 0

    private var currentPage = 1

    override fun getMovies(page: Int): Single<List<Movie>> {
        val query = TMDBService.buildFindQuery(apiKey, page)
        return service.loadPopularMovies(query).subscribeOn(Schedulers.io()).map {
            totalPages = it.totalPages
            currentPage = it.page
            it.movies
        }
    }

    // TODO: see what kind of exception will be thrown when page is out of bounds
    override fun getNextPage(): Single<List<Movie>> = getMovies(++currentPage)
}