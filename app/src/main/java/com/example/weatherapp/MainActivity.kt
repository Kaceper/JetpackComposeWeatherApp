package com.example.weatherapp

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weatherapp.pages.WeatherHomeScreen
import com.example.weatherapp.pages.WeatherHomeViewModel
import com.example.weatherapp.ui.theme.WeatherAppTheme
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Klient do obsługi geolokacji
        var client: FusedLocationProviderClient = LocationServices
            .getFusedLocationProviderClient(this)

        enableEdgeToEdge()
        setContent {
            WeatherApp(client);
        }
    }
}

@Composable
fun WeatherApp(client: FusedLocationProviderClient, modifier: Modifier = Modifier) {
    val weatherHomeViewModel : WeatherHomeViewModel = viewModel()

    val context = LocalContext.current
    // Stan uprawnień:
    // 'mutableStateOf' tworzy obserwowalny obiekt (jeśli wartość się zmieni, UI sam się odświeży).
    // 'remember' sprawia, że wartość nie ginie przy odświeżaniu widoku (rekompozycji).
    // 'by' to delegat pozwalający na bezpośredni odczyt/zapis zmiennej (zamiast .value).
    var permissionGranted by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
    ) { permissions ->
        val fineGranted = permissions[android.Manifest.permission.ACCESS_FINE_LOCATION] ?: false
        val coarseGranted = permissions[android.Manifest.permission.ACCESS_COARSE_LOCATION] ?: false

        permissionGranted = fineGranted || coarseGranted
    }

    // LauchedEffect - to wykona się tylko raz przy starcie ekranu
    LaunchedEffect(Unit) {
        val isPermissionGranted =
            ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED

        // Przy braku zgody na uprawnienia pytamy o nie użytkownika
        if (!isPermissionGranted) {
            launcher.launch(arrayOf(
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ))
        } else {
            permissionGranted = true
        }
    }

    LaunchedEffect(permissionGranted) {
        if (permissionGranted) {
            client.lastLocation.addOnSuccessListener {
                weatherHomeViewModel.updateCoordinates(it.latitude, it.longitude)
                weatherHomeViewModel.getWeatherData()
            }
        }
    }

    WeatherAppTheme() {
        // Przekazanie aktualnego stanu (Ładowanie / Sukces / Błąd) z ViewModelu do ekranu
        WeatherHomeScreen(weatherHomeViewModel.uiState)
    }
}