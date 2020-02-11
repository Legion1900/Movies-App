package com.legion1900.moviesapp.domain.impl

import androidx.paging.PageKeyedDataSource
import com.legion1900.moviesapp.data.abs.MoviesRepository
import com.legion1900.moviesapp.domain.abs.dto.Movie
import com.legion1900.moviesapp.domain.abs.dto.MoviePage
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable

class MoviesDataSource(private val repo: MoviesRepository) : PageKeyedDataSource<Int, Movie>() {

    private val disposables = CompositeDisposable()

    private lateinit var load: (MoviePage) -> Unit

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
        load = { callback.onResult(it.movies, page, page + 1) }
        tryLoad()
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {
        page = params.key
        load = {
            val nextPage = if (params.key < totalPages) (params.key + 1) else null
            callback.onResult(it.movies, nextPage)
        }
        tryLoad()
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {
        /*
        * Nothing to do here.
        * */
    }

    fun retryLoad() {
        tryLoad()
    }

    fun dispose() {
        disposables.dispose()
    }

    private fun tryLoad() {
        val disposableLoad = repo.loadMovies(page)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                // TODO: what to do with this disposable??
                onStart()
                disposables.add(it)
            }
            .subscribe(
                {
                    onSuccess()
                    totalPages = it.totalPages
                    load(it)
                },
                onError
            )
        disposables.add(disposableLoad)
    }
}
