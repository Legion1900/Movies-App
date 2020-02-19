package com.legion1900.moviesapp.view.di.modules

import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.legion1900.moviesapp.R
import com.legion1900.moviesapp.view.di.FragmentScope
import dagger.Module
import dagger.Provides

@Module
object GlideModule {
    @JvmStatic
    @FragmentScope
    @Provides
    fun provideGlide(fragment: Fragment, options: RequestOptions): RequestManager =
        Glide.with(fragment).setDefaultRequestOptions(options)

    @JvmStatic
    @FragmentScope
    @Provides
    fun provideRequestOptions() = RequestOptions().apply {
        placeholder(R.drawable.img_preview)
        error(R.drawable.img_error)
    }
}
