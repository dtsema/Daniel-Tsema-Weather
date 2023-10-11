package com.jpmorgan.weather.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Singleton object that provides a configured Retrofit instance to communicate with OpenWeatherMap API.
 */
object RetrofitClient {

    // The base URL endpoint for the OpenWeatherMap API.
    private const val BASE_URL = "https://api.openweathermap.org/"

    // Interceptor to log HTTP request and response data.
    private val loggingInterceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // OkHttpClient to add specific interceptors and settings.
    private val okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    // The main Retrofit instance configured with the base URL, converter, and client.
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())  // To convert JSON responses to Kotlin data objects.
        .build()

    // Lazily provides the WeatherService instance from the configured Retrofit.
    val weatherService: WeatherService by lazy {
        retrofit.create(WeatherService::class.java)
    }
}
