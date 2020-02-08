package com.legion1900.moviesapp.view.fragments.mainscreen

import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.databinding.BindingAdapter
import androidx.lifecycle.*
import androidx.recyclerview.widget.RecyclerView
import com.legion1900.moviesapp.domain.abs.GetMoviesUseCase
import com.legion1900.moviesapp.domain.abs.PickMovieUseCase
import com.legion1900.moviesapp.domain.abs.dto.Movie
import javax.inject.Inject

class PopularMoviesViewModel @Inject constructor(
    private val moviesProvider: GetMoviesUseCase,
    private val moviePicker: PickMovieUseCase
) : ViewModel() {

    private val progressBarVisibility = MutableLiveData<Boolean>()

    private val recyclerViewVisibility = MutableLiveData<Boolean>()

    private val isLoadingError = MutableLiveData<Boolean>()

    private val movies = MutableLiveData<List<Movie>>()

    init {
        progressBarVisibility.value = true
        recyclerViewVisibility.value = false
        isLoadingError.value = false
        moviesProvider.subscribe(::onStart, ::onSuccess, ::onError)
    }

    fun getMovies(): LiveData<List<Movie>> = movies

    fun getProgressBarVisibility(): LiveData<Boolean> = progressBarVisibility

    fun getRecyclerViewVisibility(): LiveData<Boolean> = recyclerViewVisibility

    fun isLoadingError(): LiveData<Boolean> = isLoadingError

    fun loadMovies(page: Int = 1) {
        moviesProvider.getMovies(page)
    }

    fun pickMovie(movie: Movie) {
        moviePicker.pick(movie)
    }

    private fun onStart() {
        isLoadingError.value = false
        recyclerViewVisibility.value = false
        progressBarVisibility.value = true
    }

    private fun onSuccess(movies: List<Movie>) {
        this.movies.value = movies
        recyclerViewVisibility.value = true
        progressBarVisibility.value = false
    }

    private fun onError(t: Throwable) {
        progressBarVisibility.value = false
        isLoadingError.value = true
    }

    override fun onCleared() {
        super.onCleared()
        moviesProvider.dispose()
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
