package com.dicoding.asclepius.di

import android.content.Context
import androidx.room.Room
import com.dicoding.asclepius.data.local.AnalyzeResultDao
import com.dicoding.asclepius.data.local.AsclepiusDatabase
import com.skydoves.sandwich.retrofit.adapters.ApiResponseCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {

	@Provides
	@Singleton
	fun provideOkHttp() = OkHttpClient
		.Builder()
		.connectTimeout(25, TimeUnit.SECONDS)
		.addInterceptor(
			HttpLoggingInterceptor().apply {
				level = HttpLoggingInterceptor.Level.BODY
			}
		)
		.build()

	@Provides
	@Singleton
	fun provideRetrofit(okHttp: OkHttpClient): Retrofit =
		Retrofit
			.Builder()
			.baseUrl("https://event-api.dicoding.dev")
			.addConverterFactory(
				Json.asConverterFactory("application/json; charset=UTF8".toMediaType())
			)
			.addCallAdapterFactory(ApiResponseCallAdapterFactory.create())
			.client(okHttp)
			.build()

	@Provides
	@Singleton
	fun provideAppDatabase(@ApplicationContext appContext: Context): AsclepiusDatabase {
		return Room.databaseBuilder(
			appContext,
			AsclepiusDatabase::class.java,
			"__asclepius.db"
		).build()
	}

	@Provides
	@Singleton
	fun provideAnalyzeResultDao(appDatabase: AsclepiusDatabase)
			: AnalyzeResultDao = appDatabase.analyzeResultDao()
}