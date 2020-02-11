package com.legion1900.moviesapp.view.fragments.mainscreen.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.legion1900.moviesapp.R
import com.legion1900.moviesapp.domain.abs.dto.Movie

class MoviesPagedAdapter(
    private val glide: RequestManager,
    private val onMovieClick: (View) -> Unit
) : PagedListAdapter<Movie, MoviesPagedAdapter.MovieHolder>(DiffCallback) {

    fun getMovie(position: Int) = getItem(position)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.film_view_holder, parent, false)
            .apply { setOnClickListener(onMovieClick) }
        return MovieHolder(view)
    }

    override fun onBindViewHolder(holder: MovieHolder, position: Int) {
        val movie = getItem(position)
        glide.load(movie?.posterPath).into(holder.poster)
        holder.title.text = movie?.title
    }

    class MovieHolder(item: View) : RecyclerView.ViewHolder(item) {
        val poster: ImageView = item.findViewById(R.id.poster)
        val title: TextView = item.findViewById(R.id.title)
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return (oldItem.title == newItem.title) && (oldItem.posterPath == newItem.posterPath)
        }
    }
}
