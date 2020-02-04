package com.legion1900.moviesapp.di.modules

import com.legion1900.moviesapp.BuildConfig
import com.legion1900.moviesapp.data.abs.MoviesRepository
import com.legion1900.moviesapp.data.impl.services.TMDBService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
class NetworkModule {
    @Singleton
    @Provides
    fun provideTMDBService(): TMDBService {
        return Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TMDBService::class.java)
    }

    @Named(MoviesRepository.API_KEY)
    @Provides
    fun provideTMDBKey() = BuildConfig.API_KEY
}
