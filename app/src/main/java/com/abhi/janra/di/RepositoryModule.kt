package com.abhi.janra.di

import com.abhi.janra.repository.NewsListRepository
import com.abhi.janra.repository.NewsListRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {

    @Singleton
    @Provides
    fun provideCountryDataRepository(): NewsListRepository {
        return NewsListRepositoryImpl()
    }
}