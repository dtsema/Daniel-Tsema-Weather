package com.jpmorgan.weather.api

import javax.inject.Inject
import javax.inject.Singleton

/**
 * A repository that serves as an intermediary between the data source (API)
 * and the rest of the application.
 *
 * @property key The API key used for authentication.
 */
@Singleton
class WeatherRepository @Inject constructor() {

    private val key = "16a2aa0ae7f1820b4a679484b2346e5b"
    private val api = RetrofitClient.weatherService

    /**
     * Fetches weather data for the given city.
     *
     * @param cityName The name of the city to fetch the weather data for.
     * @return Weather data for the specified city.
     */
    suspend fun getWeatherByCity(cityName: String): WeatherResponse {
        return api.getWeatherByCity(cityName, "imperial", key)
    }

    /**
     * Fetches weather data based on the given geographic coordinates.
     *
     * @param latitude The latitude of the location.
     * @param longitude The longitude of the location.
     * @return Weather data for the specified geographic coordinates.
     */
    suspend fun getWeatherByLocation(latitude: Double, longitude: Double): WeatherResponse {
        return api.getWeatherByLocation(latitude, longitude, "imperial", key)
    }
}
