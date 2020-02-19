package com.legion1900.moviesapp.di

import com.legion1900.moviesapp.data.abs.MoviesRepository
import com.legion1900.moviesapp.data.impl.serialization.TMDBConfiguration
import com.legion1900.moviesapp.di.modules.ConfigModule
import com.legion1900.moviesapp.di.modules.NetworkModule
import com.legion1900.moviesapp.di.modules.RepoModule
import com.legion1900.moviesapp.view.base.BaseFragment
import com.legion1900.moviesapp.view.di.FragmentComponent
import com.legion1900.moviesapp.view.di.modules.ViewModelModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Named
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        ViewModelModule::class,
        NetworkModule::class,
        RepoModule::class,
        ConfigModule::class,
        FragmentComponent.InstallModule::class
    ]
)
interface AppComponent {
    fun inject(fragment: BaseFragment)

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
