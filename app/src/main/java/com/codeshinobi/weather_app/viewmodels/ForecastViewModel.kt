package com.codeshinobi.weather_app.viewmodels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codeshinobi.weather_app.api.ApiClient
import com.codeshinobi.weather_app.api.Forecast
import com.codeshinobi.weather_app.api.Hourly
import com.codeshinobi.weather_app.api.HourlyForecast
import kotlinx.coroutines.launch

class ForecastViewModel : ViewModel() {
    private val _forecasts = mutableStateOf<List<HourlyForecast>?>(null)
    val forecasts: State<List<HourlyForecast>?> = _forecasts

    fun getForecasts() {
        viewModelScope.launch {
            val response = ApiClient.apiService.getForecast(35.0, -15.75, "temperature_2m")
            if (response.isSuccessful) {
                val forecastModel = response.body()
                _forecasts.value = mapHourlyToForecasts(forecastModel?.hourly)
            } else {
                // Handle the error here
            }
        }
    }

    private fun mapHourlyToForecasts(hourly: Hourly?): List<HourlyForecast> {
        val forecasts = mutableListOf<HourlyForecast>()
        if (hourly != null) {
            for (i in hourly.time.indices) {
                val time = hourly.time[i]
                val temperature = hourly.temperature2M[i]
                // Create a Forecast object and add it to the list
                val forecast = HourlyForecast(time, temperature)
                forecasts.add(forecast)
            }
        }
        return forecasts
    }
}