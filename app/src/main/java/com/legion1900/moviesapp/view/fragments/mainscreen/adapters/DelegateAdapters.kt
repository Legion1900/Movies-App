package com.legion1900.moviesapp.view.fragments.mainscreen.adapters

import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.RequestManager
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegate
import com.legion1900.moviesapp.R
import com.legion1900.moviesapp.domain.dto.Movie

const val VIEW_TYPE_MOVIE = 0
const val VIEW_TYPE_LOADING = 1
const val VIEW_TYPE_ERROR = 2

fun buildMovieDelegate(
    glide: RequestManager,
    onMovieClick: (Movie) -> Unit
) = adapterDelegate<Movie, Movie>(
    layout = R.layout.movie_view_holder
) {
    val title = findViewById<TextView>(R.id.title)
    val poster = findViewById<ImageView>(R.id.poster)
    itemView.setOnClickListener { onMovieClick(item) }

    bind {
        title.text = item.title
        glide.load(item.posterPath).into(poster)
    }
}

fun buildLoadingDelegate() =
    adapterDelegate<Movie, Movie>(
        layout = R.layout.loading_view_holder
    ) { /* Nothing to do here. */ }

fun buildErrorDelegate(retry: () -> Unit) =
    adapterDelegate<Movie, Movie>(
        layout = R.layout.error_view_holder
    ) {
        findViewById<Button>(R.id.retry_btn).setOnClickListener { retry() }
    }


fun buildItemDiffCallback() = object : DiffUtil.ItemCallback<Movie>() {
    override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return (oldItem.title == newItem.title) && (oldItem.posterPath == newItem.posterPath)
    }
}
