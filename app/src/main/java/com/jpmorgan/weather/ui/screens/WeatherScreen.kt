package com.jpmorgan.weather.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.jpmorgan.weather.R
import com.jpmorgan.weather.api.WeatherResponse
import com.jpmorgan.weather.api.WeatherViewModel
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun WeatherScreen() {
    val viewModel: WeatherViewModel = hiltViewModel()

    val context = LocalContext.current

    RequestLocationPermission(onPermissionGranted = {
        viewModel.getUserLocation(context) { location ->
            viewModel.fetchWeatherByLocation(location.latitude, location.longitude)
        }
    })

    // Observing the weather data
    val lastSearchedWeatherData by viewModel.lastSearchedWeatherData.observeAsState()
    val currentLocationWeatherData by viewModel.currentLocationWeatherData.observeAsState()

    // UI to input city name and fetch data
    var cityName by remember { mutableStateOf(viewModel.getLastSearchedCity() ?: "") }

    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(text = "Weather App", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                value = cityName,
                onValueChange = { cityName = it },
                label = { Text("Enter city name") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    viewModel.saveLastSearchedCity(cityName)
                    viewModel.fetchWeather(cityName)
                },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Get Weather")
            }

            Spacer(modifier = Modifier.height(30.dp))

            // Display the current location weather data
            currentLocationWeatherData?.let { data ->
                WeatherInfoCard(data, title = "Current Location")
            }

            Spacer(modifier = Modifier.height(30.dp))

            // Display the last searched location weather data
            lastSearchedWeatherData?.let { data ->
                WeatherInfoCard(data, title = "Last Searched")
            }
        }
    }
}

@Composable
fun WeatherInfoCard(data: WeatherResponse, title: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            DisplayWeatherIcon(iconId = data.weather[0].icon)

            Spacer(modifier = Modifier.width(24.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "${data.name}, ${data.sys.country}",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
                Text(
                    text = data.weather[0].description.replaceFirstChar {
                        if (it.isLowerCase()) it.titlecase(
                            Locale.getDefault()
                        ) else it.toString()
                    },
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Temperature: ${data.main.temp}°",
                    fontWeight = FontWeight.Medium,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Feels like: ${data.main.feels_like}°",
                    style = MaterialTheme.typography.bodySmall
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Icon(painter = painterResource(R.drawable.baseline_air_24),
                         contentDescription = null, modifier = Modifier.size(18.dp))
                    Text(text = "Wind: ${data.wind.speed} m/s", style = MaterialTheme.typography.bodySmall)
                    Icon(painter = painterResource(R.drawable.baseline_water_drop_24), contentDescription = null, modifier = Modifier.size(18.dp))
                    Text(text = "Humidity: ${data.main.humidity}%", style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}



@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequestLocationPermission(onPermissionGranted: () -> Unit) {
    val locationPermissionState = rememberPermissionState(android.Manifest.permission.ACCESS_FINE_LOCATION)

    LaunchedEffect(true) {
        locationPermissionState.launchPermissionRequest()
    }

    SideEffect {
        if (locationPermissionState.status.isGranted) {
            onPermissionGranted()
        }
    }
}


@OptIn(ExperimentalCoilApi::class)
@Composable
fun DisplayWeatherIcon(iconId: String) {
    val imageUrl = "https://openweathermap.org/img/wn/$iconId@2x.png"
    Image(
        painter = rememberImagePainter(data = imageUrl),
        contentDescription = null,
        modifier = Modifier.size(50.dp)
    )
}


