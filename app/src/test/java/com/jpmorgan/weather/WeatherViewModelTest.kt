@file:Suppress("DEPRECATION")

package com.jpmorgan.weather

import android.content.SharedPreferences
import com.jpmorgan.weather.api.WeatherRepository
import com.jpmorgan.weather.api.WeatherViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyString
import org.mockito.ArgumentMatchers.eq
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

class WeatherViewModelTest {

    // Mocked dependencies
    @Mock
    private lateinit var repository: WeatherRepository

    @Mock
    private lateinit var myPreference: MyPreference

    @Mock
    private lateinit var sharedPreferences: SharedPreferences

    @Mock
    private lateinit var sharedPrefsEditor: SharedPreferences.Editor

    // Instance of the ViewModel under test
    private lateinit var viewModel: WeatherViewModel

    // Dispatcher for testing coroutines
    private val testDispatcher = TestCoroutineDispatcher()

    // Set up the environment for each test
    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        // Set the main coroutine dispatcher to the test dispatcher
        Dispatchers.setMain(testDispatcher)

        // Initialize mocks
        MockitoAnnotations.openMocks(this)

        // Mock behavior for SharedPreferences and its editor
        `when`(myPreference.prefs).thenReturn(sharedPreferences)
        `when`(sharedPreferences.edit()).thenReturn(sharedPrefsEditor)
        `when`(sharedPrefsEditor.putString(anyString(), anyString())).thenReturn(sharedPrefsEditor)
        `when`(sharedPreferences.getString(eq("LAST_SEARCHED_CITY"), any())).thenReturn("SomeCity")

        // Instantiate the ViewModel
        viewModel = WeatherViewModel(repository, myPreference)
    }

    // Test the fetchWeatherByLocation function in the ViewModel
    @Test
    fun testFetchWeatherByLocation(): Unit = runBlocking {
        val latitude = 50.0
        val longitude = 50.0
        viewModel.fetchWeatherByLocation(latitude, longitude)

        // Verify that the right function in the repository was called with correct arguments
        verify(repository).getWeatherByLocation(latitude, longitude)
    }

    // Test the fetchWeather function in the ViewModel
    @Test
    fun testFetchWeather(): Unit = runBlocking {
        val cityName = "London"
        viewModel.fetchWeather(cityName)

        // Verify that the right function in the repository was called with correct arguments
        verify(repository).getWeatherByCity(cityName)
    }

    // Clean up after each test
    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        // Reset main dispatcher to its original value
        Dispatchers.resetMain()

        // Clean up any remaining coroutine work
        testDispatcher.cleanupTestCoroutines()
    }
}
