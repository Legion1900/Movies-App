package com.legion1900.moviesapp.di.modules

import androidx.paging.PagedList
import dagger.Module
import dagger.Provides

@Module
object ConfigModule {
    @JvmStatic
    @Provides
    fun provideConfig(): PagedList.Config {
        return PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(20)
            .build()
    }
}
