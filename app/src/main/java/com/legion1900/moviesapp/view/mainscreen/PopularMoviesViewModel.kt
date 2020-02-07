package com.legion1900.moviesapp.view.mainscreen

import android.view.View
import android.widget.ProgressBar
import androidx.databinding.BindingAdapter
import androidx.lifecycle.*
import androidx.recyclerview.widget.RecyclerView
import com.legion1900.moviesapp.data.abs.MoviesRepository
import com.legion1900.moviesapp.data.abs.dto.Movie
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class PopularMoviesViewModel @Inject constructor(private val repo: MoviesRepository) : ViewModel() {
    private val disposables = CompositeDisposable()

    private val progressBarVisibility = MutableLiveData<Boolean>().also { it.value = true }

    private val recyclerViewVisibility = MutableLiveData<Boolean>().also { it.value = false }

    private val isLoadingError = MutableLiveData<Boolean>().also { it.value = false }

    private val movies =
        MutableLiveData<List<Movie>>().apply { repo.loadMovies { getMovies() } }

    fun getMovies(): LiveData<List<Movie>> = movies

    fun getProgressBarVisibility(): LiveData<Boolean> = progressBarVisibility

    fun getRecyclerViewVisibility(): LiveData<Boolean> = recyclerViewVisibility

    fun isLoadingError(): LiveData<Boolean> = isLoadingError

    fun loadMovies() {
        repo.loadMovies { getMovies() }
    }

    private inline fun MoviesRepository.loadMovies(
        request: MoviesRepository.() -> Single<List<Movie>>
    ) {
        val loadingDisposable = request()
            .doOnSubscribe {
                onStart()
                disposables.add(it)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::onSuccess, ::onError)
        disposables.add(loadingDisposable)
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
        disposables.clear()
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
