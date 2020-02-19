package com.legion1900.moviesapp.view.fragments.detailsscreen

import androidx.lifecycle.ViewModel
import com.legion1900.moviesapp.domain.dto.Movie
import javax.inject.Inject

class MovieDetailsViewModel @Inject constructor() : ViewModel() {
    var movie: Movie? = null
}
