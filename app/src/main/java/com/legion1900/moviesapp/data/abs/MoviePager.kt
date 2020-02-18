package com.legion1900.moviesapp.data.abs

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.legion1900.moviesapp.domain.abs.dto.Movie

/*
* Manages some kind of PagedList builder and provides data about loading state.
* */
interface MoviePager {
    enum class LoadingState {
        IDLE, LOADING, ERROR
    }

    val loadingState: LiveData<LoadingState>

    val page: LiveData<PagedList<Movie>>

    fun providePageSource()

    fun retryLoad()

    fun invalidateSource()

    fun dispose()
}
