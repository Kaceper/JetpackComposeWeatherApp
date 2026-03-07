package com.example.weatherapp.pages

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.CurrentWeather
import com.example.weatherapp.data.ForecastWeather
import com.example.weatherapp.data.WeatherRepository
import com.example.weatherapp.data.WeatherRepositoryImpl
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlin.coroutines.coroutineContext

class WeatherHomeViewModel : ViewModel() {
    private val weatherRepository: WeatherRepository = WeatherRepositoryImpl()

    // Obserwowalny stan UI (jak INotifyPropertyChanged w C#). Zmiana wartości odświeża ekran
    // Słówko 'by' pozwala pisać uiState zamiast uiState.value. Na start dajemy Loading
    var uiState: WeatherHomeUiState by mutableStateOf(WeatherHomeUiState.Loading)

    // Coroutine - ogólnie "lekki wątek", odpowiednik "Task" z C#. Pozwala na asynchroniczną pracę bez blokowania ekranu
    // Jeśli do ustawienia błędu potrzebujemy tylko samego wyjątku (throwable), zastępujemy go "_", żeby nie śmiecić w pamięci i kodzie
    // exceptionHandler - odpowiednik TaskScheduler.UnobservedTaskException w .NET
    // private val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        uiState = WeatherHomeUiState.Error(throwable.message.toString())
    }

    fun getWeatherData() {
        // "viewModelScope.launch" to swego rodzaju odpowiednij Task.Run w C#
        // "viewModelScope" działa jak wbudowany CancellationToken
        // Jeśli użytkownik zamknie aplikację w trakcie pobierania danych to "viewModelScope" automatycznie anuluje te zapytanie żeby nie marnować zasobów
        viewModelScope.launch(exceptionHandler) {
            uiState = try {
                // async i await to odpowiedniki Task i await z C#

                // Odpalam oba zapytania jednocześnie w tle
                val currentDeferred = async { getCurrentData() }
                val forecastDeferred = async { getForecastData() }

                // Czekam aż oba wrócą z wynikiem
                // byłoby nieoptymalne bo aplikacja czekałaby na wykonanie pierwszeo
                val currentWeather = currentDeferred.await()

                // !! - Non-null Assertion, jeśli wartość null to aplikacja natychmiast się zamyka (Crash)
                // Używane raczej tylko w testach
                val forecastWeather = forecastDeferred.await()
                Log.d("WeatherHomeViewModel", "Current weather: ${currentWeather.main!!.temp}")
                Log.d("WeatherHomeViewModel", "Forecast weather: ${forecastWeather.list!!.size}")

                WeatherHomeUiState.Success(Weather(currentWeather, forecastWeather))
            } catch (e: Exception) {
                WeatherHomeUiState.Error(e.message.toString())
            }
        }
    }

    private suspend fun getCurrentData() : CurrentWeather {
        val endUrl = "weather?lat=52.796761&lon=18.262070&appid=9152e67158d8e2a836aaf71f98a45cc7&units=metric"

        return weatherRepository.getCurrentWeather(endUrl)
    }

    private suspend fun getForecastData() : ForecastWeather {
        val endUrl = "forecast?lat=52.796761&lon=18.262070&appid=9152e67158d8e2a836aaf71f98a45cc7&units=metric"

        return weatherRepository.getForecastWeather(endUrl)
    }
}