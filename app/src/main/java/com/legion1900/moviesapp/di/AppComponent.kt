package com.legion1900.moviesapp.di

import com.legion1900.moviesapp.data.abs.MoviesRepository
import com.legion1900.moviesapp.data.impl.serialization.TMDBConfiguration
import com.legion1900.moviesapp.di.modules.ConfigModule
import com.legion1900.moviesapp.di.modules.NetworkModule
import com.legion1900.moviesapp.view.di.FragmentComponent
import dagger.BindsInstance
import dagger.Component
import javax.inject.Named
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        NetworkModule::class,
        ConfigModule::class,
        FragmentComponent.InstallModule::class
    ]
)
interface AppComponent {
    @Component.Builder
    interface Builder {
        fun setPosterSize(
            @BindsInstance
            @Named(MoviesRepository.POSTER_SIZE)
            size: TMDBConfiguration.Images.ImageSize
        ): Builder

        fun setBackdropSize(
            @BindsInstance
            @Named(MoviesRepository.BACKDROP_SIZE)
            size: TMDBConfiguration.Images.ImageSize
        ): Builder

        fun build(): AppComponent
    }

    fun fragmentComponentBuilder(): FragmentComponent.Builder
}
