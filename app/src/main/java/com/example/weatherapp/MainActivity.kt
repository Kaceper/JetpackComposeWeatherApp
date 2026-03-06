package com.example.weatherapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weatherapp.pages.WeatherHomeScreen
import com.example.weatherapp.pages.WeatherHomeViewModel
import com.example.weatherapp.ui.theme.WeatherAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WeatherApp();
        }
    }
}

@Composable
fun WeatherApp(modifier: Modifier = Modifier) {
    val weatherHomeViewModel : WeatherHomeViewModel = viewModel()
    // LauchedEffect - to wykona się tylko raz przy starcie ekranu
    LaunchedEffect(Unit) {
        weatherHomeViewModel.getWeatherData()
    }

    WeatherAppTheme() {
        // Przekazanie aktualnego stanu (Ładowanie / Sukces / Błąd) z ViewModelu do ekranu
        WeatherHomeScreen(weatherHomeViewModel.uiState)
    }
}