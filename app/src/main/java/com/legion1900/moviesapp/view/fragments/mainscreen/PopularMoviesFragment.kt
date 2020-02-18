package com.legion1900.moviesapp.view.fragments.mainscreen

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.RequestManager
import com.hannesdorfmann.adapterdelegates4.AdapterDelegatesManager
import com.legion1900.moviesapp.R
import com.legion1900.moviesapp.data.abs.MoviePager
import com.legion1900.moviesapp.databinding.PopularFilmsFragmentBinding
import com.legion1900.moviesapp.di.App
import com.legion1900.moviesapp.domain.abs.dto.Movie
import com.legion1900.moviesapp.view.base.BaseFragment
import com.legion1900.moviesapp.view.dialogs.HostUnreachableDialogFragment
import com.legion1900.moviesapp.view.fragments.detailsscreen.MovieDetailsFragment
import com.legion1900.moviesapp.view.fragments.mainscreen.adapters.*
import javax.inject.Inject

class PopularMoviesFragment : BaseFragment() {

    @Inject
    lateinit var glide: RequestManager

    private lateinit var binding: PopularFilmsFragmentBinding

    private lateinit var adapter: BottomNotifierMovieAdapter

    private val viewModel: PopularMoviesViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[PopularMoviesViewModel::class.java]
    }

    private val errorCallback = object : HostUnreachableDialogFragment.PositiveCallback() {
        override fun onPositiveClick(dialog: DialogInterface, which: Int) {
            errorDialog.dismiss()
            viewModel.retryLoad()
        }
    }

    private val isDialogPresent
        get() = childFragmentManager.findFragmentByTag(DIALOG_ERR_TAG) != null

    private val errorDialog: HostUnreachableDialogFragment = HostUnreachableDialogFragment.create(
        R.string.host_unreachable_msg,
        R.string.try_again,
        errorCallback
    )

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
            val manager = AdapterDelegatesManager<List<Movie>>()
            manager.addDelegate(VIEW_TYPE_MOVIE, buildMovieDelegate(glide, ::onMovieClick))
                .addDelegate(
                    VIEW_TYPE_LOADING,
                    buildLoadingDelegate()
                )
                .addDelegate(
                    VIEW_TYPE_ERROR,
                    buildErrorDelegate()
                )
            adapter = BottomNotifierMovieAdapter(
                buildItemDiffCallback(),
                MoviePager.LoadingState.LOADING,
                manager
            )
            movieList.adapter = adapter
            val layoutManager = GridLayoutManager(context, 2)
            layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    val isLoading = viewModel.loadingState.value == MoviePager.LoadingState.LOADING
                    val isError = viewModel.loadingState.value == MoviePager.LoadingState.ERROR
                    return if ((isLoading || isError) && position >= adapter.itemCount) 2 else 1
                }

            }
            movieList.layoutManager = GridLayoutManager(context, 2)
        }
        viewModel.movies.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })
    }

    private fun initStateHandling() {
        viewModel.loadingState.observe(viewLifecycleOwner, Observer {
            adapter.currentState = it
//            it?.also {
//                when (it) {
//                    MoviePager.LoadingState.IDLE -> onSuccess()
//                    MoviePager.LoadingState.LOADING -> onLoading()
//                    MoviePager.LoadingState.ERROR -> onError()
//                }
//            }
        })
    }

    private fun onSuccess() {
        with(binding) {
            if (isDialogPresent) errorDialog.dismiss()
            loadingAnimation.visibility = View.GONE
        }
    }

    private fun onLoading() {
        if (isDialogPresent) errorDialog.dismiss()
        binding.loadingAnimation.visibility = View.VISIBLE
    }

    private fun onError() {
        binding.loadingAnimation.visibility = View.GONE
        if (!isDialogPresent) {
            errorDialog.show(childFragmentManager, DIALOG_ERR_TAG)
        }
    }

    private fun onMovieClick(movie: Movie) {
        viewModel.pickMovie(movie)
//        TODO: add simple transition animation
        activity?.supportFragmentManager?.beginTransaction()?.apply {
            replace(
                R.id.fragment_container,
                MovieDetailsFragment.newInstance(),
                MovieDetailsFragment.TAG
            )
            addToBackStack(null)
            commit()
        }
    }

    companion object {
        const val DIALOG_ERR_TAG = "host_unreachable"
    }
}
