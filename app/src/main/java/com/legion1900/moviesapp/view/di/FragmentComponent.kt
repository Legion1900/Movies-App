package com.legion1900.moviesapp.view.di

import androidx.fragment.app.Fragment
import com.legion1900.moviesapp.view.di.modules.GlideModule
import com.legion1900.moviesapp.view.fragments.detailsscreen.MovieDetailsFragment
import com.legion1900.moviesapp.view.fragments.detailsscreen.di.MovieDetailsViewModelFactoryModule
import com.legion1900.moviesapp.view.fragments.mainscreen.PopularMoviesFragment
import com.legion1900.moviesapp.view.fragments.mainscreen.di.PopularMoviesViewModelFactoryModule
import dagger.BindsInstance
import dagger.Module
import dagger.Subcomponent

@FragmentScope
@Subcomponent(
    modules = [
        GlideModule::class,
        MovieDetailsViewModelFactoryModule::class,
        PopularMoviesViewModelFactoryModule::class
    ]
)
interface FragmentComponent {
    fun inject(fragment: PopularMoviesFragment)

    fun inject(fragment: MovieDetailsFragment)

    @Module(subcomponents = [FragmentComponent::class])
    abstract class InstallModule

    @Subcomponent.Builder
    interface Builder {
        fun setFragment(@BindsInstance fragment: Fragment): Builder
        fun build(): FragmentComponent
    }
}
