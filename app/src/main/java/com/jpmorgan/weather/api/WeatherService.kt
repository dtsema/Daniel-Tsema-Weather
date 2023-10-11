package com.jpmorgan.weather.api

import retrofit2.http.GET
import retrofit2.http.Query

// Service interface to define endpoints related to weather data fetching.
interface WeatherService {

    /**
     * Fetches the weather data based on the given city name.
     *
     * @param cityName The name of the city to fetch the weather data for.
     * @param units The unit system to be used for temperature. Options are "metric", "imperial", and "standard".
     * @param apiKey The API key used for authentication.
     * @return Weather data for the specified city.
     */
    @GET("data/2.5/weather")
    suspend fun getWeatherByCity(
        @Query("q") cityName: String,
        @Query("units") units: String = "metric",
        @Query("appid") apiKey: String
    ): WeatherResponse

    /**
     * Fetches the weather data based on the given geographic coordinates.
     *
     * @param latitude The latitude of the location.
     * @param longitude The longitude of the location.
     * @param units The unit system to be used for temperature. Options are "metric", "imperial", and "standard".
     * @param apiKey The API key used for authentication.
     * @return Weather data for the specified geographic coordinates.
     */
    @GET("data/2.5/weather")
    suspend fun getWeatherByLocation(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("units") units: String = "metric",
        @Query("appid") apiKey: String
    ): WeatherResponse

}
