package com.legion1900.moviesapp.view.fragments.mainscreen.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.legion1900.moviesapp.R
import com.legion1900.moviesapp.domain.abs.dto.Movie

class MoviesAdapter(
    private val onMovieClick: (View) -> Unit,
    private val glide: RequestManager
) : RecyclerView.Adapter<MoviesAdapter.MovieHolder>() {

    var movies = listOf<Movie>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.film_view_holder, parent, false)
                .apply { setOnClickListener(onMovieClick) }
        return MovieHolder(view)
    }

    override fun getItemCount(): Int = movies.size

    override fun onBindViewHolder(holder: MovieHolder, position: Int) {
        val movie = movies[position]
        glide
            .setDefaultRequestOptions(glideOptions)
            .load(movie.posterPath)
            .into(holder.poster)
        holder.title.text = movie.title
    }

    class MovieHolder(item: View) : RecyclerView.ViewHolder(item) {
        val poster: ImageView = item.findViewById(R.id.poster)
        val title: TextView = item.findViewById(R.id.title)
    }

    companion object {
        @JvmStatic
        @BindingAdapter("setDataSource")
        fun setDataSource(view: RecyclerView, dataSource: LiveData<List<Movie>>) {
            val adapter = view.adapter as MoviesAdapter
            dataSource.observe(view.context as LifecycleOwner, Observer {
                adapter.movies = it
            })
        }

        private val glideOptions = RequestOptions().apply {
            placeholder(R.drawable.img_preview)
            error(R.drawable.img_error)
        }
    }
}
