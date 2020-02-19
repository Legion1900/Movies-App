package com.legion1900.moviesapp.view.fragments.detailsscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.RequestManager
import com.legion1900.moviesapp.R
import com.legion1900.moviesapp.databinding.MovieDetailsFragmentBinding
import com.legion1900.moviesapp.di.App
import com.legion1900.moviesapp.domain.abs.dto.Movie
import com.legion1900.moviesapp.view.base.BaseFragment
import javax.inject.Inject

class MovieDetailsFragment : BaseFragment() {
    @Inject
    lateinit var glide: RequestManager

    private lateinit var binding: MovieDetailsFragmentBinding

    private val viewModel: MovieDetailsViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[MovieDetailsViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.appComponent.fragmentComponentBuilder().setFragment(this).build().inject(this)
        if (viewModel.movie == null) {
            viewModel.movie = arguments?.getParcelable(ARGS_MOVIE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.movie_details_fragment, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        glide.load(viewModel.movie!!.backdropPath).into(binding.backdropImage)
        return binding.root
    }

    companion object {
        const val TAG = "movie_details"
        const val ARGS_MOVIE = "selected_movie"

        @JvmStatic
        fun newInstance(movie: Movie) = MovieDetailsFragment().apply {
            arguments = Bundle().apply {
                putParcelable(ARGS_MOVIE, movie)
            }
        }
    }
}
