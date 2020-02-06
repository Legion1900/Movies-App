package com.legion1900.moviesapp.view.di

import androidx.fragment.app.Fragment
import com.legion1900.moviesapp.view.di.modules.GlideModule
import com.legion1900.moviesapp.view.mainscreen.PopularMoviesFragment
import dagger.BindsInstance
import dagger.Module
import dagger.Subcomponent

@FragmentScope
@Subcomponent(modules = [GlideModule::class])
interface FragmentComponent {
    fun inject(fragment: PopularMoviesFragment)

    @Module(subcomponents = [FragmentComponent::class])
    abstract class InstallModule

    @Subcomponent.Builder
    interface Builder {
        fun setFragment(@BindsInstance fragment: Fragment): Builder
        fun build(): FragmentComponent
    }
}