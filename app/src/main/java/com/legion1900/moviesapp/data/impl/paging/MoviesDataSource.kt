package com.legion1900.moviesapp.data.impl.paging

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.legion1900.moviesapp.data.abs.MoviePager
import com.legion1900.moviesapp.data.abs.MoviesRepository
import com.legion1900.moviesapp.domain.dto.Movie
import com.legion1900.moviesapp.domain.dto.MoviePage
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable

class MoviesDataSource(
    private val repo: MoviesRepository,
    private val loadingState: MutableLiveData<MoviePager.LoadingState>
) : PageKeyedDataSource<Int, Movie>() {

    private val disposables = CompositeDisposable()

    private lateinit var load: (MoviePage) -> Unit

    private var page = 1
    private var totalPages = 0

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
                loadingState.postValue(MoviePager.LoadingState.LOADING)
                disposables.add(it)
            }
            .subscribe(
                { page ->
                    loadingState.postValue(MoviePager.LoadingState.IDLE)
                    totalPages = page.totalPages
                    load(page)
                },
                { e ->
                    Log.e("Error", "Error while loading movies", e)
                    loadingState.postValue(MoviePager.LoadingState.ERROR)
                }
            )
        disposables.add(disposableLoad)
    }
}
