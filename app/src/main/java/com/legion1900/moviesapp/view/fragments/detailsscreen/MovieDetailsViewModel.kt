package com.legion1900.moviesapp.view.fragments.detailsscreen

import androidx.lifecycle.ViewModel
import com.legion1900.moviesapp.domain.abs.PickMovieUseCase
import javax.inject.Inject

class MovieDetailsViewModel @Inject constructor(
    moviePicker: PickMovieUseCase
) : ViewModel() {
    val title: String
    val originalTitle: String
    val overview: String
    val backdropUrl: String?
    init {
        val movie = moviePicker.getPicked()
        title = movie.title
        originalTitle = movie.originalTitle
        overview = movie.overview
        backdropUrl = movie.backdropPath
    }
}
