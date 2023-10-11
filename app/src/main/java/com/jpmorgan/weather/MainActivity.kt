package com.jpmorgan.weather

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.jpmorgan.weather.ui.screens.WeatherScreen
import com.jpmorgan.weather.ui.theme.WeatherTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * The main entry point of the application which displays the WeatherScreen.
 * Annotated with @AndroidEntryPoint to enable field injection with Hilt.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set the UI content for this activity.
        setContent {
            // Use the app's custom theme.
            WeatherTheme {
                // A surface container using the background color from the theme.
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Displays the main WeatherScreen.
                    WeatherScreen()
                }
            }
        }
    }
}
