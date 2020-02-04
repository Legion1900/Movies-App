package com.legion1900.moviesapp.view.base

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.legion1900.moviesapp.di.App
import com.legion1900.moviesapp.di.ViewModelFactory
import javax.inject.Inject

abstract class BaseFragment : Fragment(){
    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        App.appComponent.inject(this)
        super.onCreate(savedInstanceState)
    }
}