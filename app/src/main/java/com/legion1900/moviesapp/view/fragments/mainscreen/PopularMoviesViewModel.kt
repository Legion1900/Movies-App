package com.legion1900.moviesapp.view.fragments.mainscreen

import androidx.lifecycle.ViewModel
import com.legion1900.moviesapp.data.abs.MoviePager
import javax.inject.Inject

class PopularMoviesViewModel @Inject constructor(
    private val pageProvider: MoviePager
) : ViewModel() {

    val movies = pageProvider.page

    val loadingState = pageProvider.loadingState

    init {
        pageProvider.providePageSource()
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
