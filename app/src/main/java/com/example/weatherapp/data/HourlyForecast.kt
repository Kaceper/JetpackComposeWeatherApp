package com.example.weatherapp.data

/**
 * Klasa opisująca temperaturę na daną godzinę
 */
data class HourlyForecast (
    val temp: Double,
    val hour: String
)