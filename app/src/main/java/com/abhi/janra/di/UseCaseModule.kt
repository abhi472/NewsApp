package com.abhi.janra.di

import com.abhi.janra.repository.NewsListRepository
import com.abhi.janra.usecase.NewsListRxUseCase
import com.abhi.janra.usecase.NewsListUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object UseCaseModule {

    @Singleton
    @Provides
    fun provideNewsListUseCase(repository: NewsListRepository): NewsListUseCase {
        return NewsListUseCase(repository)
    }

    @Singleton
    @Provides
    fun provideNewsListUseCaseRx(repository: NewsListRepository): NewsListRxUseCase {
        return NewsListRxUseCase(repository)
    }
}