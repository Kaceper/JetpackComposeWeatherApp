package com.example.weatherapp

import android.content.Context
import android.net.ConnectivityManager
import com.example.weatherapp.data.ConnectivityRepository
import com.example.weatherapp.data.DefaultConnectivityRepository
import com.example.weatherapp.data.WeatherRepository
import com.example.weatherapp.data.WeatherRepositoryImpl
import com.example.weatherapp.network.WeatherApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module /** Definiuje ten obiekt jako moduł DI dostarczający instrukcje tworzenia zależności **/
@InstallIn(SingletonComponent::class) /** Określa zakres (scope) modułu na cykl życia całej aplikacji (SingletonComponent) **/
/**
 * Wstrzykiwanie zależności pozwala klasom (np. ViewModelom) otrzymywać gotowe komponenty (obiekty) z zewnątrz,
 *  * zamiast tworzyć je samodzielnie. Gwarantuje to luźne powiązanie kodu (klasy wiedzą o sobi jak najmniej),
 *  co znacznie ułatwia testowanie, utrzymanie i skalowanie aplikacji
 */
object RepositoryModule {
    private const val BASE_URL = "https://api.openweathermap.org/data/2.5/"

    @Provides /** Rejestruje tę metodę jako dostawcę (provider) typu Retrofit w grafie zależności **/
    fun provideRetrofitClient(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    fun providerWeatherApiService(retrofit: Retrofit): WeatherApiService {
        return retrofit.create(WeatherApiService::class.java)
    }

    @Provides
    fun provideConnectivityManager(@ApplicationContext context: Context): ConnectivityManager {
        return context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    @Provides
    fun provideConnectivityRepository(connectivityManager: ConnectivityManager): ConnectivityRepository {
        return DefaultConnectivityRepository(connectivityManager)
    }

    @Provides
    fun provideWeatherRepository(weatherApiService: WeatherApiService): WeatherRepository {
        return WeatherRepositoryImpl(weatherApiService)
    }
}