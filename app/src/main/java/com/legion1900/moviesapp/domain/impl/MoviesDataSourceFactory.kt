package com.legion1900.moviesapp.domain.impl

import androidx.lifecycle.MutableLiveData
import com.legion1900.moviesapp.data.abs.MoviesRepository
import com.legion1900.moviesapp.domain.abs.dto.Movie
import javax.inject.Inject

class MoviesDataSourceFactory @Inject constructor(
    private val repo: MoviesRepository
) : androidx.paging.DataSource.Factory<Int, Movie>() {

    val sourceLiveData = MutableLiveData<MoviesDataSource>()

    var latestSource: MoviesDataSource? = null

    override fun create(): androidx.paging.DataSource<Int, Movie> {
        latestSource?.run { dispose() }
        latestSource = MoviesDataSource(repo)
        sourceLiveData.value = latestSource
        return latestSource!!
    }
}
