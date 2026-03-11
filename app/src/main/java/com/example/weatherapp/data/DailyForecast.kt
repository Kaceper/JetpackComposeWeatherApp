package com.example.weatherapp.data

/**
 * Klasa opisująca pogodę na dany dzień
 */
data class DailyForecast(
    val dayName: Any,
    val tempMax: String,
    val tempMin: String,
    val icon: String,
    val date: String
)