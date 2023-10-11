package com.jpmorgan.weather.api

/**
 * Represents the main response from the Weather API.
 */
data class WeatherResponse(
    val coord: Coord,
    val weather: List<Weather>,
    val base: String,
    val main: Main,
    val visibility: Int,
    val wind: Wind,
    val rain: Rain?,
    val clouds: Clouds,
    val dt: Long,
    val sys: Sys,
    val timezone: Int,
    val id: Int,
    val name: String,
    val cod: Int
)

/**
 * Represents geographic coordinates.
 */
data class Coord(
    val lon: Double,  // Longitude
    val lat: Double   // Latitude
)

/**
 * Holds information about the weather conditions.
 */
data class Weather(
    val id: Int,
    val main: String,           // Weather condition, e.g. "Rain"
    val description: String,    // Detailed description of the condition
    val icon: String            // Icon identifier
)

/**
 * Contains details about main weather metrics.
 */
data class Main(
    val temp: Double,
    val feels_like: Double,
    val temp_min: Double,
    val temp_max: Double,
    val pressure: Int,
    val humidity: Int,
    val sea_level: Int,
    val grnd_level: Int
)

/**
 * Represents wind attributes.
 */
data class Wind(
    val speed: Double,  // Wind speed
    val deg: Int,       // Wind direction, in degrees
    val gust: Double    // Wind gust
)

/**
 * Contains precipitation details for the last hour.
 */
data class Rain(
    val `1h`: Double    // Rain volume for the last hour, in mm
)

/**
 * Represents cloudiness.
 */
data class Clouds(
    val all: Int    // Cloudiness percentage
)

/**
 * Contains system-related data.
 */
data class Sys(
    val type: Int,
    val id: Int,
    val country: String,   // Country code
    val sunrise: Long,     // Sunrise time, Unix, UTC
    val sunset: Long       // Sunset time, Unix, UTC
)
