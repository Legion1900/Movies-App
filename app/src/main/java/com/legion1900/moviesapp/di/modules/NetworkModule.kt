package com.legion1900.moviesapp.di.modules

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.legion1900.moviesapp.BuildConfig
import com.legion1900.moviesapp.data.abs.MoviesRepository
import com.legion1900.moviesapp.data.impl.serialization.TMDBConfiguration
import com.legion1900.moviesapp.data.impl.services.ConfigurationDeserializer
import com.legion1900.moviesapp.data.impl.services.TMDBService
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
class NetworkModule {

    @Singleton
    @Provides
    fun provideGson() = GsonBuilder()
        .setLenient()
        .registerTypeAdapter(TMDBConfiguration::class.java, ConfigurationDeserializer())
        .create()

    @Singleton
    @Provides
    fun provideRetrofit(gson: Gson): Retrofit {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
        return Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .client(client)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Singleton
    @Provides
    fun provideTMDBService(retrofit: Retrofit): TMDBService {
        return retrofit.create(TMDBService::class.java)
    }

    @Named(MoviesRepository.API_KEY)
    @Provides
    fun provideTMDBKey() = BuildConfig.API_KEY
}
