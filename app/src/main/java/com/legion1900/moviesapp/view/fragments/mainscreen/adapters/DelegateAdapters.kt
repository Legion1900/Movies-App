package com.legion1900.moviesapp.view.fragments.mainscreen.adapters

import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.RequestManager
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegate
import com.legion1900.moviesapp.R
import com.legion1900.moviesapp.domain.abs.dto.Movie

fun buildMovieAdapter(
    glide: RequestManager,
    onMovieClick: (Movie) -> Unit
) = adapterDelegate<Movie, Movie>(
    layout = R.layout.film_view_holder,
    on = { _, data, position -> position < data.size - 1 }
) {
    val title = findViewById<TextView>(R.id.title)
    val poster = findViewById<ImageView>(R.id.poster)
    itemView.setOnClickListener { onMovieClick(item) }

    bind {
        title.text = item.title
        glide.load(item.posterPath).into(poster)
    }
}

fun buildItemDiffCallback() = object : DiffUtil.ItemCallback<Movie>() {
    override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return (oldItem.title == newItem.title) && (oldItem.posterPath == newItem.posterPath)
    }
}
