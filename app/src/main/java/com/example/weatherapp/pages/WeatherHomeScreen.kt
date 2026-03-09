package com.example.weatherapp.pages

import android.graphics.Paint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.weatherapp.R
import com.example.weatherapp.customuis.AppBackground
import com.example.weatherapp.customuis.CroppedText
import com.example.weatherapp.data.CurrentWeather
import com.example.weatherapp.data.ForecastWeather
import com.example.weatherapp.utils.degree
import com.example.weatherapp.utils.dot
import com.example.weatherapp.utils.getFormattedDate
import com.example.weatherapp.utils.getIconUrl

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherHomeScreen(uiState: WeatherHomeUiState, modifier: Modifier = Modifier) {
    // Box - odpowiednik Grid bez wierszy i kolumn z XAMLa
    // układa elementy jedne na drugich
    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        AppBackground(photoId = R.drawable.weather_app_background)
        // Scaffold - odpowiednik View w XML (gotowy "szablon" strony)
        Scaffold(
            /*topBar = {
                TopAppBar(
                    title = { Text(text = "Pogoda", style = MaterialTheme.typography.titleMedium) },
                    colors = TopAppBarDefaults.topAppBarColors( containerColor = Color.Transparent, actionIconContentColor = Color.White)
                )
            },*/
            containerColor = Color.Transparent
        ) {
            // Surface - odpowiednik "Panel" lub "Border" w XAML
            Surface(
               color = Color.Transparent,
                modifier = Modifier
                    .padding(it)
                    .padding(10.dp)
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center)
            ) {
                // Odpowiednik StackPanel (Orientation = "Vertical"), ułoży elementy jednej pod drugim
                Column() {
                    when (uiState) {
                        is WeatherHomeUiState.Error -> {
                            Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Błąd podczas pobierania")
                                if (uiState.errorMessage.isNotEmpty()) {
                                    Text(text = uiState.errorMessage, style = MaterialTheme.typography.bodySmall, textAlign = TextAlign.Center)
                                }
                            }
                        }
                        is WeatherHomeUiState.Loading -> Text("Wczytywanie...")
                        is WeatherHomeUiState.Success -> WeatherSection(weather = uiState.weather)
                    }
                }
            }
        }
    }
}

@Composable
fun WeatherSection(weather: Weather, modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(8.dp)) {
        CurrentWeatherSecion(weather.currentWeather, modifier = Modifier.weight(1f))
        ForecastWeatherSecion(weather.forecastWeather, modifier = Modifier.weight(1f))
    }
}

@Composable
fun CurrentWeatherSecion(currentWeather: CurrentWeather, modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        Text("${currentWeather.name}, ${currentWeather.sys?.country}", style = MaterialTheme.typography.titleSmall)
        (Spacer(modifier = Modifier.height(10.dp)))
        Row(verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(model = ImageRequest.Builder(LocalContext.current)
                .data(getIconUrl(currentWeather.weather?.get(0)!!.icon!!))
                .crossfade(true)
                .build(),
                contentDescription = null,
                modifier = Modifier.size(50.dp))
            Text("${currentWeather.weather?.get(0)!!.description}", style = MaterialTheme.typography.bodyMedium, textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.width(16.dp))
        }
        CroppedText(
            text = "${currentWeather.main?.temp?.toInt()}$degree",
            topCrop = 30.dp,
            bottomCrop = 0.dp,
            MaterialTheme.typography.displayLarge
        )
        Text("Odczuwalna ${currentWeather.main?.feelsLike?.toInt()}${degree}", style = MaterialTheme.typography.bodySmall)
        (Spacer(modifier = Modifier.height(8.dp)))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Maks. ${currentWeather.main?.tempMax?.toInt()}${degree} $dot ", style = MaterialTheme.typography.bodySmall)
            Text("Min. ${currentWeather.main?.tempMin?.toInt()}${degree}", style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
fun ForecastWeatherSecion(forecastWeather: ForecastWeather, modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {

    }
}