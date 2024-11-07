package com.dicoding.asclepius.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Qualifier
import kotlinx.coroutines.Dispatchers

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class IoDispatcher

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class DefaultDispatcher

@Retention
@Qualifier
annotation class MainDispatcher

@Module
@InstallIn(SingletonComponent::class)

object AppModule {
	@Provides
	@IoDispatcher
	fun provideIoDispatcher() = Dispatchers.IO

	@Provides
	@DefaultDispatcher
	fun provideDefaultDispatcher() = Dispatchers.Default

	@Provides
	@MainDispatcher
	fun provideMainDispatcher() = Dispatchers.Main
}