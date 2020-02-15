package com.legion1900.moviesapp.view.fragments.mainscreen

import androidx.lifecycle.ViewModel
import com.legion1900.moviesapp.data.abs.MoviePager
import com.legion1900.moviesapp.domain.abs.PickMovieUseCase
import com.legion1900.moviesapp.domain.abs.dto.Movie
import javax.inject.Inject

class PopularMoviesViewModel @Inject constructor(
    private val pageProvider: MoviePager,
    private val moviePicker: PickMovieUseCase
) : ViewModel() {

    val movies = pageProvider.page

    val loadingState = pageProvider.loadingState

    init {
        pageProvider.providePageSource()
    }

    fun pickMovie(movie: Movie) {
        moviePicker.pick(movie)
    }

    fun retryLoad() {
        pageProvider.retryLoad()
    }

    fun invalidateSource() {
        pageProvider.invalidateSource()
    }

    override fun onCleared() {
        super.onCleared()
        pageProvider.dispose()
    }
}
