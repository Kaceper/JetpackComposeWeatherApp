package com.example.weatherapp.data

import android.net.ConnectivityManager
import android.net.Network
import com.example.weatherapp.pages.ConnectivityState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

interface ConnectivityRepository {
    /** * StateFlow - odpowiednik Event z .NET Android, który dodatkowo przechowuje "ostatni znany stan"
     * Dzięki temu UI po uruchomieniu od razu wie, czy jest sieć, nie czekając na nową zmianę
     **/
    val connectivityState: StateFlow<ConnectivityState>
}

/**
 * Nasłuchiwanie NetworkCallback jest wewnątrz repozytorium ponieważ
 * unikamy w ten sposób tworzenia zbędnych klas, odpowiada ona za dostarczania informacji o połączeniu
 */
class DefaultConnectivityRepository(private val connectivityManager: ConnectivityManager) : ConnectivityRepository {
    /** * (Mutable) - tylko to repozytorium może zmieniać stan sieci
     * Chroni to przed sytuacją, w której błąd w UI mógłby "wymusić" fałszywy stan połączenia
     **/
    private val _connectivityState = MutableStateFlow<ConnectivityState>(ConnectivityState.Unavailable)

    private val callback = object  : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: android.net.Network) {
            super.onAvailable(network)
            _connectivityState.value = ConnectivityState.Available
        }

        override fun onLost(network: Network) {
            super.onLost(network)
            _connectivityState.value = ConnectivityState.Unavailable
        }
    }

    /**
     * asStateFlow() - udostępnia strumień jako "tylko do odczytu"
     * iewModel i UI mogą jedynie reagować na zmiany, zachowując spójność danych
     **/
    override val connectivityState: StateFlow<ConnectivityState> = _connectivityState.asStateFlow()

    /**
     * Blok inicjalizujący - odpowiednik konstruktora w C#
     * Rejstruję DefaultConnectivityRepository w momencie stworzenia klasy
     */
    init {
        connectivityManager.registerDefaultNetworkCallback(callback)
    }
}