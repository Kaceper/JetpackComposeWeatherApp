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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.outlined.Compress
import androidx.compose.material.icons.outlined.WaterDrop
import androidx.compose.material.icons.rounded.Air
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.weatherapp.R
import com.example.weatherapp.customuis.AppBackground
import com.example.weatherapp.customuis.CroppedText
import com.example.weatherapp.data.CurrentWeather
import com.example.weatherapp.data.DailyForecast
import com.example.weatherapp.data.HourlyForecast
import com.example.weatherapp.utils.degree
import com.example.weatherapp.utils.getIconUrl

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherHomeScreen(isConnected: Boolean, onRefresh: () -> Unit, uiState: WeatherHomeUiState, modifier: Modifier = Modifier) {
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
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center)
            ) {
                // Odpowiednik StackPanel (Orientation = "Vertical"), ułoży elementy jednej pod drugim
                Column() {
                    if (!isConnected) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = stringResource(R.string.no_internet_connection),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }  else {
                        when (uiState) {
                            is WeatherHomeUiState.Error -> {
                                ErrorSection(message = uiState.errorMessage.ifEmpty { stringResource(R.string.error_while_loading) }, onRefresh = onRefresh)
                            }

                            is WeatherHomeUiState.Loading -> Text(text = stringResource(R.string.loading))
                            is WeatherHomeUiState.Success -> WeatherSection(weather = uiState.weather)
                        }
                    }
                }
            }
        }
    }
}

/**
 * Unit - odpowiednik Void z C#
 */
@Composable
fun ErrorSection(message: String, onRefresh: () -> Unit, modifier: Modifier = Modifier) {
    Column {
        Text(message)
        Spacer(modifier = Modifier.height(8.dp))
        IconButton(
            onClick = onRefresh,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Icon(Icons.Default.Refresh, contentDescription = null, tint = Color.White)
        }
    }
}

@Composable
fun WeatherSection(weather: Weather, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        CurrentWeatherSection(weather.currentWeather, modifier = Modifier.weight(1f).padding(12.dp))
        ForecastWeatherSection(weather.hourlyForecast, weather.dailyForecast, modifier = Modifier.weight(1f))
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
fun ForecastWeatherSection(hourlyForecast: List<HourlyForecast>, dailyForecast: List<DailyForecast>, modifier: Modifier = Modifier) {
    Column (modifier = modifier.fillMaxSize()) {
        Spacer(modifier = Modifier.weight(0.1f))
        ChartWithLabels(temps = hourlyForecast)
        Column(modifier.weight(1f).padding(horizontal = 10.dp)) {
            Spacer(modifier = Modifier.weight(0.15f))
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                modifier = Modifier.weight(0.66f)
            ) {
                items(dailyForecast.size) { index ->
                    DailyForecastWeatherItem(dailyForecast[index])
                }
            }
            Spacer(modifier = Modifier.weight(0.2f))
        }
    }
}

/**
 * Funkcja wyświetlająca kontener z wykresem temperatur per godzinę z podpięciem do niego etykiet
 * @param temps lista temperatur na daną godzinę
 */
@Composable
fun ChartWithLabels(temps: List<HourlyForecast>) {
    val floatList = temps.map { it.temp.toFloat() }
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        VerySimpleChart(
            data = floatList,
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            temps.forEach { temp ->
                Text(temp.hour, color = Color.White, style = MaterialTheme.typography.labelSmall)
            }
        }
    }
}

/**
 * Funckja rysująca wykres liniowy
 * @param data lista temperatur na daną godzinę
 * @param modifier modyfikator
 */
@Composable
fun VerySimpleChart(data: List<Float>, modifier: Modifier = Modifier) {
    // Narzędzie do pisania na wykresie (Paint)
    // Dzięki "remember" obiekt Paint nie jest tworzony na nowo  gdy coś zmieni się na ekranie (rekompozycja)
    val textPaint = remember {
        android.graphics.Paint().apply {
            color = android.graphics.Color.WHITE
            textSize = 32f
            textAlign = android.graphics.Paint.Align.CENTER
        }
    }

    Canvas(modifier = modifier.fillMaxWidth().height(120.dp)) {
        val marginPx = 60f
        val spacex = (size.width - 2 * marginPx) / (data.size - 1)

        // ajpierw narysuj wszystkie linie (pod spodem)
        for (i in 0 until data.size - 1) {
            val x1 = marginPx + (i * spacex)
            val x2 = marginPx + ((i + 1) * spacex)
            val y1 = size.height - (data[i] * 8f) - 40f
            val y2 = size.height - (data[i + 1] * 8f) - 40f

            // Główna linia wykresu
            drawLine(Color.White, Offset(x1, y1), Offset(x2, y2), strokeWidth = 6f)

            // Linia wchodząca z lewej strony (tylko dla pierwszego punktu) z alpha 0.5f
            if (i == 0) drawLine(Color.White.copy(0.5f), Offset(0f, y1), Offset(x1, y1), 6f)

            // Linia wychodząca w prawo (tylko dla ostatniego punktu) z alpha 0.5f
            if (i == data.size - 2) drawLine(Color.White.copy(0.5f), Offset(x2, y2), Offset(size.width, y2), 6f)
        }

        // Kropki na linii i tekst z temperaturą
        data.forEachIndexed { i, temp ->
            val x = marginPx + (i * spacex)
            val y = size.height - (temp * 8f) - 40f

            drawCircle(Color.White, radius = 6f, center = Offset(x, y))
            drawContext.canvas.nativeCanvas.drawText("${temp.toInt()}°", x, y - 25f, textPaint)
        }
    }
}

/**
 * Funkcja wyświetlająca pojedynczy element horyzontalnej listy z prognozą na kolejne dni
 * @param item prognoza na dany dzień
 * @param modifier modyfikator
 */
@Composable
fun DailyForecastWeatherItem(item: DailyForecast, modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .width(80.dp)
            .background(
                color = Color.White.copy(alpha = 0.15f),
                shape = RoundedCornerShape(25.dp)
            )
            .border(
                width = 0.5.dp,
                color = Color.White.copy(alpha = 0.5f),
                shape = RoundedCornerShape(25.dp)
            )
    ) {
        val displayDay = when (val name = item.dayName) {
            is Int -> stringResource(name)
            else -> name.toString()
        }

        Text(
            text = displayDay,
            style = MaterialTheme.typography.labelSmall,
            textAlign = TextAlign.Center
        )
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(getIconUrl(item.icon))
                .crossfade(true)
                .build(),
            contentDescription = null,
            modifier = Modifier.size(50.dp)
        )
        Text(
            item.tempMax,
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center,
        )
        Text(
            item.tempMin,
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center,
            color = Color.LightGray
        )
    }
}