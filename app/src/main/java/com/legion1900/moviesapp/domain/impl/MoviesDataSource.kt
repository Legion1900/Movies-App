package com.legion1900.moviesapp.domain.impl

import androidx.paging.PageKeyedDataSource
import com.legion1900.moviesapp.data.abs.MoviesRepository
import com.legion1900.moviesapp.domain.abs.dto.Movie
import com.legion1900.moviesapp.domain.abs.dto.MovieRequest
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable

class MoviesDataSource(private val repo: MoviesRepository) : PageKeyedDataSource<Int, Movie>() {

    private val disposables = CompositeDisposable()

    private lateinit var onStart: () -> Unit
    private lateinit var onError: (Throwable) -> Unit
    private lateinit var onSuccess: () -> Unit

    private var page = 1

    private var totalPages = 0

    fun subscribe(onStart: () -> Unit, onSuccess: () -> Unit, onError: (Throwable) -> Unit) {
        this.onStart = onStart
        this.onSuccess = onSuccess
        this.onError = onError
    }

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Movie>
    ) {
        subscribeToMovies(page) {
            callback.onResult(it.movies, page, ++page)
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {
        subscribeToMovies(page) {
            /*
            * Next page (if any at all).
            * */
            val nextPage = if (page < totalPages) ++page else null
            callback.onResult(it.movies, nextPage)
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {
        /*
        * Nothing to do here.
        * */
    }

    fun dispose() {
        disposables.dispose()
    }

    private inline fun subscribeToMovies(
        page: Int,
        crossinline routeToCallback: (MovieRequest) -> Unit
    ) {
        val moviesDisposable = repo.loadMovies(page)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                //                TODO: what to do with this disposable??
                onStart()
                disposables.add(it)
            }
            .subscribe(
                {
                    onSuccess()
                    totalPages = it.totalPages
                    routeToCallback(it)
                },
                onError
            )
        disposables.add(moviesDisposable)
    }
}
