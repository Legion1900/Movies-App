package com.legion1900.moviesapp.view.mainscreen

import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.legion1900.moviesapp.data.abs.MoviesRepository
import com.legion1900.moviesapp.data.abs.dto.Movie
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class PopularMoviesViewModel @Inject constructor(private val repo: MoviesRepository) : ViewModel() {
    private val disposable = CompositeDisposable()

    private val movies =
        MutableLiveData<List<Movie>>().apply { repo.loadMovies { getMovies() } }

    private val isProgressBarVisible = MutableLiveData<Boolean>().also { it.value = true }

    private val isRecyclerViewVisible = MutableLiveData<Boolean>().also { it.value = false }

    fun getMovies(): LiveData<List<Movie>> = movies

    fun isProgressBarVisible(): LiveData<Boolean> = isProgressBarVisible

    fun isRecyclerViewVisible(): LiveData<Boolean> = isRecyclerViewVisible

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
        isRecyclerViewVisible.value = true
        isProgressBarVisible.value = false
    }

    private fun onError(t: Throwable) {
        // TODO: display alert msg
        isProgressBarVisible.value = false
        Log.e("test", t.stackTrace.toString())
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
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
