package com.example.weatherapp.pages

import com.example.weatherapp.data.CurrentWeather
import com.example.weatherapp.data.ForecastWeather

// "data class" to odpowiednik "record" z C# 9.0+.
// automatycznie generuje: equals(), hashCode(), toString() oraz copy().
data class Weather(val currentWeather: CurrentWeather, val forecastWeather: ForecastWeather)

// Określenie stanów dla ekranu, dzięki temu nie wyświetlimy kółka ładowania i danych jednocześnie
// Swego rodzaju ENUM
sealed interface WeatherHomeUiState {
    // Udało się pobrać dane, "data class" ponieważ zawiera dane - "weather: Weather"
    data class Success(val weather : Weather) : WeatherHomeUiState

    // Stan, gdy coś poszło nie tak (błąd), używamy "data object" ponieważ nie zawiera danych
    data object Error : WeatherHomeUiState

    // Stan, gdy czekam na pobranie danych, używamy "data object" ponieważ nie zawiera danych
    data object Loading : WeatherHomeUiState
}