package com.legion1900.moviesapp.view.fragments.mainscreen

import android.view.View
import android.widget.ProgressBar
import androidx.databinding.BindingAdapter
import androidx.lifecycle.*
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import androidx.recyclerview.widget.RecyclerView
import com.legion1900.moviesapp.domain.abs.PickMovieUseCase
import com.legion1900.moviesapp.domain.abs.dto.Movie
import com.legion1900.moviesapp.domain.impl.MoviesDataSourceFactory
import javax.inject.Inject

class PopularMoviesViewModel @Inject constructor(
    private val moviesSourceFactory: MoviesDataSourceFactory,
    private val moviePicker: PickMovieUseCase
) : ViewModel() {

    val movies: LiveData<PagedList<Movie>>

    private val progressBarVisibility = MutableLiveData<Boolean>()

    private val recyclerViewVisibility = MutableLiveData<Boolean>()

    private val isLoadingError = MutableLiveData<Boolean>()

    init {
        progressBarVisibility.value = true
        recyclerViewVisibility.value = false
        isLoadingError.value = false
        moviesSourceFactory.sourceLiveData.observeForever {
            it.subscribe(::onStart, ::onSuccess, ::onError)
        }

        movies = LivePagedListBuilder<Int, Movie>(moviesSourceFactory, 20).setFetchExecutor {
            it.run()
        }.build()
    }

    fun getProgressBarVisibility(): LiveData<Boolean> = progressBarVisibility

    fun getRecyclerViewVisibility(): LiveData<Boolean> = recyclerViewVisibility

    fun isLoadingError(): LiveData<Boolean> = isLoadingError

    fun pickMovie(movie: Movie) {
        moviePicker.pick(movie)
    }

    fun retryLoad() {
        moviesSourceFactory.latestSource?.retryLoad()
    }

    fun invalidateSource() {
        moviesSourceFactory.latestSource?.invalidate()
    }

    private fun onStart() {
        isLoadingError.value = false
        recyclerViewVisibility.value = false
        progressBarVisibility.value = true
    }

    private fun onSuccess() {
        recyclerViewVisibility.value = true
        progressBarVisibility.value = false
    }

    private fun onError(t: Throwable) {
        progressBarVisibility.value = false
        isLoadingError.value = true
    }

    override fun onCleared() {
        super.onCleared()
        moviesSourceFactory.latestSource?.dispose()
    }

    companion object {
        @JvmStatic
        @BindingAdapter("visibility")
        fun setProgressBarVisibility(pb: ProgressBar, visibilitySource: LiveData<Boolean>) {
            setVisibility(pb, visibilitySource)
        }

        @JvmStatic
        @BindingAdapter("visibility")
        fun setRecyclerViewVisibility(rv: RecyclerView, visibilitySource: LiveData<Boolean>) {
            setVisibility(rv, visibilitySource)
        }

        @JvmStatic
        private fun setVisibility(view: View, visibilitySource: LiveData<Boolean>) {
            visibilitySource.observe(view.context as LifecycleOwner, Observer {
                view.visibility = if (it) View.VISIBLE else View.GONE
            })
        }
    }
}
