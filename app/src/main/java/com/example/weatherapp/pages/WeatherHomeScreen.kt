package com.example.weatherapp.pages

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Compress
import androidx.compose.material.icons.outlined.WaterDrop
import androidx.compose.material.icons.rounded.Air
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.weatherapp.R
import com.example.weatherapp.customuis.AppBackground
import com.example.weatherapp.customuis.CroppedText
import com.example.weatherapp.data.CurrentWeather
import com.example.weatherapp.data.ForecastWeather
import com.example.weatherapp.utils.degree
import com.example.weatherapp.utils.getIconUrl
import androidx.compose.ui.res.stringResource

val temperatury = listOf(10f, 50f, 30f, 80f, 40f)

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
                                Text(text = stringResource(R.string.error_while_loading))
                                if (uiState.errorMessage.isNotEmpty()) {
                                    Text(text = uiState.errorMessage, style = MaterialTheme.typography.bodySmall, textAlign = TextAlign.Center)
                                }
                            }
                        }
                        is WeatherHomeUiState.Loading -> Text(text = stringResource(R.string.loading))
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
        CurrentWeatherSection(weather.currentWeather, modifier = Modifier.weight(1f))
        ForecastWeatherSecion(weather.forecastWeather, modifier = Modifier.weight(1f))
    }
}

@Composable
fun CurrentWeatherSection(currentWeather: CurrentWeather, modifier: Modifier = Modifier) {
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
        Text("${stringResource(R.string.read_feel_temperature)} ${currentWeather.main?.feelsLike?.toInt()}${degree}", style = MaterialTheme.typography.bodySmall)
        Spacer(modifier = Modifier.height(24.dp))
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(start = 25.dp, top = 0.dp, bottom = 0.dp, end = 25.dp)
                .fillMaxWidth()
                .height(70.dp)
                .background(
                    color = Color.White.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(180.dp)
                )
                .border(
                    width = 0.5.dp,
                    color = Color.White.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(180.dp)
                )) {
            Column(modifier = Modifier.weight(0.25f)) { }
            Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Outlined.WaterDrop,
                    contentDescription = stringResource(R.string.humidity),
                    tint = Color.White,
                )
                Text(
                   "${currentWeather.main?.humidity}%",
                    style = MaterialTheme.typography.labelSmall
                )
            }
            Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Outlined.Compress,
                    contentDescription = stringResource(R.string.pressure),
                    tint = Color.White
                )
                Text(
                    "${currentWeather.main?.pressure}hPa",
                    style = MaterialTheme.typography.labelSmall
                )
            }
            Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Rounded.Air,
                    contentDescription = stringResource(R.string.wind_speed),
                    tint = Color.White
                )
                Text(
                    "${currentWeather.wind?.speed?.toInt()}km/h",
                    style = MaterialTheme.typography.labelSmall
                )
            }
            Column(modifier = Modifier.weight(0.25f)) { }
        }
    }
}

@Composable
fun ForecastWeatherSecion(forecastWeather: ForecastWeather, modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        ChartWithLabels(temps = temperatury)
    }
}

@Composable
fun ChartWithLabels(temps: List<Float>) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        VerySimpleChart(
            data = temps,
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .padding(horizontal = 10.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            temps.forEach { temp ->
                Text("${temp.toInt()}°", color = Color.White, style = MaterialTheme.typography.labelSmall)
            }
        }
    }
}
@Composable
fun VerySimpleChart(data: List<Float>, modifier: Modifier) {
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .background(Color.Transparent)
    ) {
        val spacex = size.width / (data.size - 1)

        for (i in 0 until data.size - 1) {
            drawLine(
                color = Color.Cyan,
                start = Offset(x = i * spacex, y = size.height - data[i]),
                end = Offset(x = (i + 1) * spacex, y = size.height - data[i + 1]),
                strokeWidth = 4f
            )
        }
    }
}