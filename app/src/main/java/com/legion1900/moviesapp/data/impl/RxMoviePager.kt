package com.legion1900.moviesapp.data.impl

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import androidx.paging.RxPagedListBuilder
import com.legion1900.moviesapp.data.abs.MoviePager
import com.legion1900.moviesapp.data.impl.paging.MoviesDataSourceFactory
import com.legion1900.moviesapp.domain.dto.Movie
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

/*
* Implements MoviePager using RxPagedListBuilder.
* */
class RxMoviePager @Inject constructor(
    private val factory: MoviesDataSourceFactory,
    private val config: PagedList.Config

) : MoviePager {

    private val disposables = CompositeDisposable()

    override val loadingState: LiveData<MoviePager.LoadingState> = factory.loadingState

    private val _page = MutableLiveData<PagedList<Movie>>()
    override val page: LiveData<PagedList<Movie>> = _page

    override fun providePageSource() {
        val observable = RxPagedListBuilder<Int, Movie>(factory, config).buildObservable()
        val d = observable.subscribe { _page.value = it }
        disposables.add(d)
    }

    override fun retryLoad() {
        factory.latestSource?.retryLoad()
    }

    override fun invalidateSource() {
        disposables.clear()
        factory.latestSource?.invalidate()
    }

    override fun dispose() {
        disposables.dispose()
    }
}
