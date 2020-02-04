package com.legion1900.moviesapp.view.mainscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.legion1900.moviesapp.R
import com.legion1900.moviesapp.view.base.BaseFragment


class PopularFilmsFragment : BaseFragment() {

    private val viewModel: PopularFilmsViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[PopularFilmsViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.popular_films_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

}
