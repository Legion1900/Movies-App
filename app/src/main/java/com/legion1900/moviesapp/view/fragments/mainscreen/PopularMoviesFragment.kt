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
import com.legion1900.moviesapp.R
import com.legion1900.moviesapp.databinding.PopularFilmsFragmentBinding
import com.legion1900.moviesapp.di.App
import com.legion1900.moviesapp.view.base.BaseFragment
import com.legion1900.moviesapp.view.dialogs.HostUnreachableDialogFragment
import com.legion1900.moviesapp.view.fragments.mainscreen.adapters.MoviesAdapter
import javax.inject.Inject

class PopularMoviesFragment : BaseFragment() {

    @Inject
    lateinit var glide: RequestManager

    private lateinit var binding: PopularFilmsFragmentBinding

    private val viewModel: PopularMoviesViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[PopularMoviesViewModel::class.java]
    }

    private val errorCallback = object : HostUnreachableDialogFragment.PositiveCallback() {
        override fun onPositiveClick(dialog: DialogInterface, which: Int) {
            errorDialog.dismiss()
            viewModel.loadMovies()
        }
    }

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
        initDataBinding()
        initLoadingErrorDialog()

        return binding.root
    }

    private fun initDataBinding() {
        viewModel.loadMovies()
        binding.run {
            viewModel = this@PopularMoviesFragment.viewModel
            lifecycleOwner = this@PopularMoviesFragment
            movieList.adapter = MoviesAdapter(::onMovieClick, glide)
            movieList.layoutManager = GridLayoutManager(context, 2)
        }
    }

    private fun initLoadingErrorDialog() {
        viewModel.isLoadingError().observe(viewLifecycleOwner, Observer {
            val isDialogPresent = childFragmentManager.findFragmentByTag(DIALOG_ERR_TAG) != null
            if (it && !isDialogPresent)
                errorDialog.show(childFragmentManager, DIALOG_ERR_TAG)
            else if (isDialogPresent) errorDialog.dismiss()
        })
    }

    private fun onMovieClick(v: View) {
        TODO("open another fragment")
    }

    companion object {
        const val DIALOG_ERR_TAG = "host_unreachable"
    }
}
