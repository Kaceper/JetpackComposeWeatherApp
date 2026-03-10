package com.example.weatherapp.data

data class DailyForecast(
    val dayName: Any,
    val tempMax: String,
    val tempMin: String,
    val icon: String,
    val date: String
)