package com.dicoding.asclepius.data.network

import com.dicoding.asclepius.BuildConfig
import com.skydoves.sandwich.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

	@GET("top-headlines")
	suspend fun getNews(
		@Query("q") q: String = "cancer",
		@Query("category") category: String = "health",
		@Query("language") language: String = "en",
		@Query("apiKey") apiKey: String = BuildConfig.API_KEY,
	): ApiResponse<GetNewsResponse>
}