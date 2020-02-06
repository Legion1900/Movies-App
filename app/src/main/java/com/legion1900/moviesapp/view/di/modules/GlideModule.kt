package com.legion1900.moviesapp.view.di.modules

import android.content.Context
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.legion1900.moviesapp.view.di.FragmentScope
import dagger.Module
import dagger.Provides

@Module
class GlideModule {
    @FragmentScope
    @Provides
    fun provideGlide(fragment: Fragment): RequestManager = Glide.with(fragment)
}