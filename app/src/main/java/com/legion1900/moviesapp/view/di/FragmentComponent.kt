package com.legion1900.moviesapp.view.di

import com.legion1900.moviesapp.view.base.BaseFragment
import dagger.Module
import dagger.Subcomponent

@Subcomponent(
    modules = [ViewModelModule::class]
)
interface FragmentComponent {
    fun inject(fragment: BaseFragment)

    @Module(subcomponents = [FragmentComponent::class])
    abstract class InstallModule

    @Subcomponent.Builder
    interface Builder {
        fun build(): FragmentComponent
    }
}