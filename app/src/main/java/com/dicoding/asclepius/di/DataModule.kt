package com.dicoding.asclepius.di

import com.dicoding.asclepius.data.repositories.HistoriesRepositoryImpl
import com.dicoding.asclepius.data.repositories.NewsRepositoryImpl
import com.dicoding.asclepius.domain.repositories.HistoriesRepository
import com.dicoding.asclepius.domain.repositories.NewsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

	@Binds
	@Singleton
	fun bindHistoriesRepository(historiesRepositoryImpl: HistoriesRepositoryImpl)
			: HistoriesRepository

	@Binds
	@Singleton
	fun bindNewsRepository(newsRepositoryImpl: NewsRepositoryImpl)
			: NewsRepository
}