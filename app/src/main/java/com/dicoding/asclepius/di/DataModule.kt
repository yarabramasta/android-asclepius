package com.dicoding.asclepius.di

import com.dicoding.asclepius.data.repositories.AnalyzeResultsRepositoryImpl
import com.dicoding.asclepius.domain.repositories.AnalyzeResultsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

	@Binds
	@Singleton
	abstract fun bindAnalyzeResultsRepository(
		analyzeResultsRepositoryImpl: AnalyzeResultsRepositoryImpl,
	): AnalyzeResultsRepository
}