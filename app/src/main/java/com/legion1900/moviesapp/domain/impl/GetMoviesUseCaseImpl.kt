package com.legion1900.moviesapp.domain.impl

import com.legion1900.moviesapp.data.abs.MoviesRepository
import com.legion1900.moviesapp.domain.abs.dto.Movie
import com.legion1900.moviesapp.domain.abs.dto.MovieRequest
import com.legion1900.moviesapp.domain.abs.GetMoviesUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class GetMoviesUseCaseImpl @Inject constructor(private val repo: MoviesRepository) : GetMoviesUseCase {

    private var onStart: (() -> Unit)? = null
    private var onSuccess: ((List<Movie>) -> Unit)? = null
    private var onError: ((Throwable) -> Unit)? = null

    private lateinit var request: MovieRequest
    override val hasNextPage: Boolean
        get() = request.page < request.totalPages

    private val disposable = CompositeDisposable()

    override fun subscribe(
        onStart: () -> Unit,
        onSuccess: (List<Movie>) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        this.onStart = onStart
        this.onSuccess = onSuccess
        this.onError = onError
    }

    override fun getMovies(page: Int) {
        val loadingDisposable = repo.loadMovies(page)
            .doOnSubscribe {
                onStart?.invoke()
                disposable.add(it)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::onSuccessWrapper, onError)
        disposable.add(loadingDisposable)
    }

    private fun onSuccessWrapper(movieRequest: MovieRequest) {
        request = movieRequest
        onSuccess?.invoke(request.movies)
    }

    override fun getNextPage() {
        if (hasNextPage)
            getMovies(request.page + 1)
    }

    override fun dispose() {
        disposable.dispose()
    }
}
