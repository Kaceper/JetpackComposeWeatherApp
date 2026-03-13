package com.example.weatherapp

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresPermission
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.core.location.LocationManagerCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weatherapp.pages.ConnectivityState
import com.example.weatherapp.pages.WeatherHomeScreen
import com.example.weatherapp.pages.WeatherHomeUiState
import com.example.weatherapp.pages.WeatherHomeViewModel
import com.example.weatherapp.ui.theme.WeatherAppTheme
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
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
        val fineGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
        val coarseGranted = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false

        permissionGranted = fineGranted || coarseGranted
    }

    // LauchedEffect - to wykona się tylko raz przy starcie ekranu
    // Unit - void - wykona się tylko raz a nie na bazie zmiany jakiegoś prametru jak niżej 'permissionGranted'
    LaunchedEffect(Unit) {
        val isPermissionGranted =
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED

        // Przy braku zgody na uprawnienia pytamy o nie użytkownika
        if (!isPermissionGranted) {
            launcher.launch(arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ))
        } else {
            permissionGranted = true
        }
    }

    LaunchedEffect(permissionGranted) {
        if (permissionGranted) {
            checkLocationAndFetch(context, client, weatherHomeViewModel, true)
        }
    }

    val connectivityState by weatherHomeViewModel.connectivityState.collectAsState()

    WeatherAppTheme() {
        // Przekazanie aktualnego stanu (Ładowanie / Sukces / Błąd) z ViewModelu do ekranu
        WeatherHomeScreen(isConnected = connectivityState == ConnectivityState.Available, onRefresh = {
            checkLocationAndFetch(context, client, weatherHomeViewModel, true)
        }
            , uiState =  weatherHomeViewModel.uiState)
    }
}

/**
 * Funkcja obsługująca wywołanie zapytania API o prognozę pogody jeśli lokaliazcja GPS jest włączona, jest zgoda na uprawenienie do GPSa i współrzędne są prawidłowe
 */
@RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
fun checkLocationAndFetch(context: Context, client: FusedLocationProviderClient, viewModel: WeatherHomeViewModel, permissionGranted: Boolean) {
    viewModel.uiState = WeatherHomeUiState.Loading
    if (!permissionGranted) {
        viewModel.uiState = WeatherHomeUiState.Error(context.getString(R.string.no_location_permission))
        return
    }

    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    if (LocationManagerCompat.isLocationEnabled(locationManager)) {
        client.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                viewModel.updateCoordinates(location.latitude, location.longitude)
                viewModel.getWeatherData()
            } else {
                viewModel.uiState = WeatherHomeUiState.Error(context.getString(R.string.no_location_found))
            }
        }
    } else {
        viewModel.uiState = WeatherHomeUiState.Error(context.getString(R.string.no_location_enabled))
    }
}