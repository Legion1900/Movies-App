package com.legion1900.moviesapp.view.fragments.detailsscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.RequestManager
import com.legion1900.moviesapp.R
import com.legion1900.moviesapp.databinding.MovieDetailsFragmentBinding
import com.legion1900.moviesapp.di.App
import com.legion1900.moviesapp.view.base.ViewModelFactory
import javax.inject.Inject
import javax.inject.Named

class MovieDetailsFragment : Fragment() {
    @Inject
    @Named(QUALIFIER)
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var glide: RequestManager

    private lateinit var binding: MovieDetailsFragmentBinding

    private val args by navArgs<MovieDetailsFragmentArgs>()

    private val viewModel: MovieDetailsViewModel by lazy {
        val vm = ViewModelProvider(this, viewModelFactory)[MovieDetailsViewModel::class.java]
        vm.apply { movie = args.selectedMovie }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.appComponent.fragmentComponentBuilder().setFragment(this).build().inject(this)
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
        const val QUALIFIER = "Movie details"

        @JvmStatic
        fun newInstance() = MovieDetailsFragment()
    }
}
