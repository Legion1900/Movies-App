package com.legion1900.moviesapp.view.di

import dagger.Module
import dagger.Subcomponent

@Subcomponent
interface FragmentComponent {
    @Module(subcomponents = [FragmentComponent::class])
    abstract class InstallModule

    @Subcomponent.Builder
    interface Builder {
        // TODO: add Context setter for Glide
        fun build(): FragmentComponent
    }
}