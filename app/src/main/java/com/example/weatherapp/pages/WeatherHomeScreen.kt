package com.example.weatherapp.pages

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.weatherapp.R
import com.example.weatherapp.customuis.AppBackground

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun WeatherHomeScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        AppBackground(photoId = R.drawable.weather_app_background)
        Scaffold(
            /*topBar = {
                TopAppBar(
                    title = { Text(text = "Pogoda", style = MaterialTheme.typography.titleMedium) },
                    colors = TopAppBarDefaults.topAppBarColors( containerColor = Color.Transparent, actionIconContentColor = Color.White)
                )
            },*/
            containerColor = Color.Transparent
        ) {
            Surface(
               color = Color.Transparent,
                modifier = Modifier
                    .padding(it)
                    .padding(0.dp)
                    .fillMaxSize()
                    .wrapContentSize()
            ) {
                Column {
                    Text("Pogoda - ekran główny", style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}