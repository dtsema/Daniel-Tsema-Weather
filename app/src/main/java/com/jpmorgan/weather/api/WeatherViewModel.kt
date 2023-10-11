package com.jpmorgan.weather.api

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.LocationServices
import com.jpmorgan.weather.MyPreference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repository: WeatherRepository,
    private val myPreference: MyPreference,
) : ViewModel() {


    // LiveData for current location's weather data
    private val _currentLocationWeatherData = MutableLiveData<WeatherResponse>()
    val currentLocationWeatherData: LiveData<WeatherResponse> get() = _currentLocationWeatherData

    // LiveData for the last searched city's weather data
    private val _lastSearchedWeatherData = MutableLiveData<WeatherResponse>()
    val lastSearchedWeatherData: LiveData<WeatherResponse> get() = _lastSearchedWeatherData

    // Initialization block to fetch the last searched city's weather data if available
    init {
        getLastSearchedCity()?.let {
            if (it.isNotEmpty()) fetchWeather(it)
        }
    }

    // Fetches weather data by geographic coordinates
    fun fetchWeatherByLocation(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            _currentLocationWeatherData.value = repository.getWeatherByLocation(latitude, longitude)
        }
    }

    // Fetches weather data for the provided city name
    fun fetchWeather(cityName: String) {
        viewModelScope.launch {
            _lastSearchedWeatherData.value = repository.getWeatherByCity(cityName)
        }
    }

    // Saves the provided city name as the last searched city
    fun saveLastSearchedCity(cityName: String) {
        with(myPreference.prefs.edit()) {
            putString("LAST_SEARCHED_CITY", cityName)
            apply()
        }
    }

    // Retrieves the last searched city's name
    fun getLastSearchedCity(): String? {
        return myPreference.prefs.getString("LAST_SEARCHED_CITY", null)
    }

    // Obtains the user's location if permissions are granted and executes the provided onSuccess callback
    @SuppressLint("MissingPermission")
    fun getUserLocation(context: Context, onSuccess: (Location) -> Unit) {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                location?.let { onSuccess(it) }
            }
    }
}
