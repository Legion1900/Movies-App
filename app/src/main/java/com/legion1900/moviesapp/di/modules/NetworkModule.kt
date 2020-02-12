package com.legion1900.moviesapp.di.modules

import com.google.gson.GsonBuilder
import com.legion1900.moviesapp.BuildConfig
import com.legion1900.moviesapp.data.impl.serialization.TMDBConfiguration
import com.legion1900.moviesapp.data.impl.services.ConfigurationDeserializer
import com.legion1900.moviesapp.data.impl.services.TMDBService
import com.legion1900.moviesapp.di.InterceptorKey
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap
import dagger.multibindings.IntoSet
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.CallAdapter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
import kotlin.reflect.KClass

@Module(includes = [NetworkModule.TypeAdapterModule::class])
class NetworkModule {

    @Provides
    fun provideRxAdapterCallFactory(): CallAdapter.Factory = RxJava2CallAdapterFactory.create()

    @Provides
    fun provideConverterFactory(
        map: Map<KClass<*>, @JvmSuppressWildcards Any>
    ): GsonConverterFactory {
        val gson = GsonBuilder()
            .setLenient()
            .apply {
                for ((klass, deserializer) in map) {
                    registerTypeAdapter(klass.java, deserializer)
                }
            }
            .create()
        return GsonConverterFactory.create(gson)
    }

    @Singleton
    @Provides
    fun provideRetrofit(
        gaf: GsonConverterFactory,
        caf: CallAdapter.Factory,
        interceptors: Set<@JvmSuppressWildcards Interceptor>
    ): Retrofit {
        val client = OkHttpClient.Builder()
            .apply { interceptors.forEach { addInterceptor(it) } }
            .build()
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(client)
            .addCallAdapterFactory(caf)
            .addConverterFactory(gaf)
            .build()
    }

    @Singleton
    @Provides
    fun provideTMDBService(retrofit: Retrofit): TMDBService {
        return retrofit.create(TMDBService::class.java)
    }

    @Provides
    @IntoSet
    fun provideLoggingInterceptor(): Interceptor =
        HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

    @Provides
    @IntoSet
    fun provideApiKeyInterceptor(): Interceptor {
        return object : Interceptor {
            override fun intercept(chain: Interceptor.Chain): Response {
                val original = chain.request()
                val originalUrl = original.url
                val url = originalUrl.newBuilder()
                    .addQueryParameter(TMDBService.PARAM_API_KEY, BuildConfig.API_KEY)
                    .build()
                val request = original.newBuilder().url(url).build()
                return chain.proceed(request)
            }
        }
    }




    @Module
    abstract class TypeAdapterModule {
        @Binds
        @IntoMap
        @InterceptorKey(TMDBConfiguration::class)
        abstract fun bindServiceConfigAdapter(adapter: ConfigurationDeserializer): Any

        @Binds
        abstract fun provideConfigurationDeserializer(impl: ConfigurationDeserializer): ConfigurationDeserializer
    }
}

//@Module
//abstract class NetworkModule {
//
//
//    @Binds
//    @IntoMap
//    @InterceptorKey(TMDBConfiguration::class)
//    abstract fun bindServiceConfigAdapter(adapter: ConfigurationDeserializer): Any
//
//    @Module
//    companion object {
//        @Provides
//        fun provideRxAdapterCallFactory(): CallAdapter.Factory = RxJava2CallAdapterFactory.create()
//
//        @Provides
//        fun provideConverterFactory(
//            map: Map<KClass<*>, @JvmSuppressWildcards Any>
//        ): GsonConverterFactory {
//            val gson = GsonBuilder()
//                .setLenient()
//                .apply {
//                    for ((klass, deserializer) in map) {
//                        registerTypeAdapter(klass.java, deserializer)
//                    }
//                }
//                .create()
//            return GsonConverterFactory.create(gson)
//        }
//
//        @Singleton
//        @Provides
//        fun provideRetrofit(
//            gaf: GsonConverterFactory,
//            caf: CallAdapter.Factory,
//            interceptors: Set<@JvmSuppressWildcards Interceptor>
//        ): Retrofit {
//            val client = OkHttpClient.Builder()
//                .apply { interceptors.forEach { addInterceptor(it) } }
//                .build()
//            return Retrofit.Builder()
//                .baseUrl(BuildConfig.BASE_URL)
//                .client(client)
//                .addCallAdapterFactory(caf)
//                .addConverterFactory(gaf)
//                .build()
//        }
//
//        @Singleton
//        @Provides
//        fun provideTMDBService(retrofit: Retrofit): TMDBService {
//            return retrofit.create(TMDBService::class.java)
//        }
//
//        @Provides
//        @IntoSet
//        fun provideLoggingInterceptor(): Interceptor =
//            HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
//
//        @Provides
//        @IntoSet
//        fun provideApiKeyInterceptor(): Interceptor {
//            return object : Interceptor {
//                override fun intercept(chain: Interceptor.Chain): Response {
//                    val original = chain.request()
//                    val originalUrl = original.url
//                    val url = originalUrl.newBuilder()
//                        .addQueryParameter(TMDBService.PARAM_API_KEY, BuildConfig.API_KEY)
//                        .build()
//                    val request = original.newBuilder().url(url).build()
//                    return chain.proceed(request)
//                }
//            }
//        }
//    }
//}
