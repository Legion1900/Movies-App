package com.legion1900.moviesapp.data.impl.paging

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.legion1900.moviesapp.data.abs.MoviePager
import com.legion1900.moviesapp.data.abs.MoviesRepository
import com.legion1900.moviesapp.domain.abs.dto.Movie
import javax.inject.Inject

class MoviesDataSourceFactory @Inject constructor(
    private val repo: MoviesRepository
) : androidx.paging.DataSource.Factory<Int, Movie>() {

    private val _loadingState =
        MutableLiveData<MoviePager.LoadingState>().apply { postValue(MoviePager.LoadingState.IDLE) }
    val loadingState: LiveData<MoviePager.LoadingState> = _loadingState

    val sourceLiveData = MutableLiveData<MoviesDataSource>()

    var latestSource: MoviesDataSource? = null

    override fun create(): androidx.paging.DataSource<Int, Movie> {
        latestSource?.run { dispose() }
        latestSource =
            MoviesDataSource(repo, _loadingState)
        sourceLiveData.postValue(latestSource)
        return latestSource!!
    }
}
