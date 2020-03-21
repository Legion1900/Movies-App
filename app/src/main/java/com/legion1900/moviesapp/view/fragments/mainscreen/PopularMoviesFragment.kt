package com.legion1900.moviesapp.view.fragments.mainscreen

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.RequestManager
import com.hannesdorfmann.adapterdelegates4.AdapterDelegatesManager
import com.legion1900.moviesapp.R
import com.legion1900.moviesapp.data.abs.MoviePager
import com.legion1900.moviesapp.databinding.PopularFilmsFragmentBinding
import com.legion1900.moviesapp.di.App
import com.legion1900.moviesapp.domain.dto.Movie
import com.legion1900.moviesapp.view.base.ViewModelFactory
import com.legion1900.moviesapp.view.fragments.mainscreen.adapters.*
import javax.inject.Inject
import javax.inject.Named

class PopularMoviesFragment : Fragment() {

    @Inject
    @Named(QUALIFIER)
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var glide: RequestManager

    private lateinit var binding: PopularFilmsFragmentBinding

    private val adapter: BottomNotifierMovieAdapter by lazy {
        val manager = AdapterDelegatesManager<List<Movie>>()
            .addDelegate(VIEW_TYPE_MOVIE, buildMovieDelegate(glide, ::onMovieClick))
            .addDelegate(VIEW_TYPE_LOADING, buildLoadingDelegate())
            .addDelegate(VIEW_TYPE_ERROR, buildErrorDelegate(viewModel::retryLoad))
        BottomNotifierMovieAdapter(
            buildItemDiffCallback(),
            MoviePager.LoadingState.LOADING,
            manager
        )
    }

    private val navigator: NavController by lazy { findNavController() }

    private val viewModel: PopularMoviesViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[PopularMoviesViewModel::class.java]
    }

    private val spanCount: Int
        get() {
            val orientation = resources.configuration.orientation
            return if (orientation == Configuration.ORIENTATION_PORTRAIT) PORTRAIT_SPAN_CNT
            else LANDSCAPE_SPAN_CNT
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.appComponent.fragmentComponentBuilder().setFragment(this).build().inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.popular_films_fragment, container, false)

        initRecyclerView()
        initStateHandling()

        return binding.root
    }

    private fun initRecyclerView() {
        binding.run {
            movieList.adapter = adapter
            movieList.layoutManager = buildFooterGridLayoutManager()
        }
        viewModel.movies.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })
    }

    private fun buildFooterGridLayoutManager() = GridLayoutManager(context, spanCount).apply {
        spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val isLoading = viewModel.loadingState.value == MoviePager.LoadingState.LOADING
                val isError = viewModel.loadingState.value == MoviePager.LoadingState.ERROR
                return if ((isLoading || isError) && position == adapter.itemCount - 1) spanCount
                else 1
            }
        }
    }

    private fun initStateHandling() {
        initEmptyScreenError()
        viewModel.loadingState.observe(viewLifecycleOwner, Observer {
            adapter.currentState = it
            if (viewModel.movies.value?.size == 0) {
                when (it) {
                    MoviePager.LoadingState.LOADING -> onLoading()
                    MoviePager.LoadingState.IDLE -> onSuccess()
                    MoviePager.LoadingState.ERROR -> onError()
                    else -> Unit
                }
            }
        })
    }

    private fun initEmptyScreenError() {
        binding.run {
            errorMsg.findViewById<Button>(R.id.retry_btn)
                .setOnClickListener { viewModel.retryLoad() }
            loadingAnimation.visibility =
                if (viewModel.movies.value?.size ?: 0 == 0) View.VISIBLE else View.GONE
        }
    }

    private fun onSuccess() {
        with(binding) {
            loadingAnimation.visibility = View.GONE
            errorMsg.visibility = View.GONE
        }
    }

    private fun onLoading() {
        binding.run {
            errorMsg.visibility = View.GONE
            loadingAnimation.visibility = View.VISIBLE
        }
    }

    private fun onError() {
        binding.run {
            loadingAnimation.visibility = View.GONE
            errorMsg.visibility = View.VISIBLE
        }
    }

    private fun onMovieClick(movie: Movie) {
        val action = PopularMoviesFragmentDirections.showMovieDetails(movie)
        navigator.navigate(action)
    }

    companion object {
        const val QUALIFIER = "Popular movies"

        private const val PORTRAIT_SPAN_CNT = 2
        private const val LANDSCAPE_SPAN_CNT = 4
    }
}
