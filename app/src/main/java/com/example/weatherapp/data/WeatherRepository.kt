package com.example.weatherapp.data

import com.example.weatherapp.network.WeatherApiService
import javax.inject.Inject

/**
 * Repozytorium odpowiedzialne za pobieranie danych z API
 * To posednik, któego zadaniem ejst ukrycie przed ViewModelem, UI tego ską i jak pobieramy dane
 */
interface WeatherRepository {
    /**
     * suspend - odpowiednik async/Task z C#, metoda asynchroniczna
     */
    suspend fun getCurrentWeather(endUrl: String): CurrentWeather
    suspend fun getForecastWeather(endUrl: String): ForecastWeather
}

/**
 * Implementacja repozytorium, używa w tym przypadku biblioteki Retrofit aby pobrać dane z API
 */
class WeatherRepositoryImpl @Inject constructor(private val weatherApi: WeatherApiService) : WeatherRepository {
    override suspend fun getCurrentWeather(endUrl: String): CurrentWeather {
        return weatherApi.getCurrentWeather(endUrl)
    }

    override suspend fun getForecastWeather(endUrl: String): ForecastWeather {
        return weatherApi.getForecastWeather(endUrl)
    }
}