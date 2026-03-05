package com.example.weatherapp.network

import com.example.weatherapp.data.CurrentWeather
import com.example.weatherapp.data.ForecastWeather
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

private const val BASE_URL = "https://api.openweathermap.org/data/2.5/"

private val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(GsonConverterFactory.create())
    .build()

interface WeatherApiService {
    @GET("weather?lat=18.269183&lon=52.791798&appid=9152e67158d8e2a836aaf71f98a45cc7")
    suspend fun getCurrentWeather(): CurrentWeather

    @GET("forecast?lat=18.269183&lon=52.791798&appid=9152e67158d8e2a836aaf71f98a45cc7")
    suspend fun getForecastWeather(): ForecastWeather
}

object WeatherApi {
    val retrofitService: WeatherApiService by lazy {
        retrofit.create(WeatherApiService::class.java)
    }
}