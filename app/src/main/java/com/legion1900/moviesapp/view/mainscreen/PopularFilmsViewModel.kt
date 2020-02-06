package com.legion1900.moviesapp.view.mainscreen

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.legion1900.moviesapp.data.abs.MoviesRepository
import com.legion1900.moviesapp.data.abs.dto.Movie
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class PopularFilmsViewModel @Inject constructor(private val repo: MoviesRepository) : ViewModel() {
    private val disposable = CompositeDisposable()

    private val movies =
        MutableLiveData<List<Movie>>().apply { repo.loadMovies { getMovies() } }

    fun getMovies(): LiveData<List<Movie>> = movies

    private inline fun MoviesRepository.loadMovies(
        request: MoviesRepository.() -> Single<List<Movie>>
    ) {
        disposable.addAll(
            request().observeOn(AndroidSchedulers.mainThread())
                .subscribe(::onSuccess, ::onError)
        )
    }

    private fun onSuccess(movies: List<Movie>) {
        this.movies.value = movies
    }

    private fun onError(t: Throwable) {
        // TODO: display alert msg
        Log.e("test", t.stackTrace.toString())
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}
