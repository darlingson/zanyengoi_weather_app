package com.codeshinobi.weather_app.viewmodels

import android.util.Log
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
            try {
                val response = ApiClient.apiService.getForecast(-15.76, 34.98, "temperature_2m,relative_humidity_2m,precipitation_probability,precipitation,rain,cloud_cover,visibility,wind_speed_10m")
                if (response.isSuccessful) {
                    val forecastModel = response.body()
                    _forecasts.value = mapHourlyToForecasts(forecastModel?.hourly)

                    // Print the successful response in the logcat
                    Log.d("ForecastViewModel", "Successful response: $forecastModel")
                    Log.d("request", "request: ${response.raw()}")
                    Log.d("hourly", "hourly: ${forecastModel?.hourly}")
                } else {
                    // Handle the error here
                    Log.e("ForecastViewModel", "Error: ${response.code()} - ${response.message()}")
                }
            } catch (e: Exception) {
                // Handle exceptions here
                Log.e("ForecastViewModel", "Exception: ${e.message}", e)
            }
        }
    }

    private fun mapHourlyToForecasts(hourly: Hourly?): List<HourlyForecast> {
        val forecasts = mutableListOf<HourlyForecast>()
        if (hourly != null && hourly.time != null && hourly.temperature_2m != null) {
            for (i in hourly.time.indices) {
                val time = hourly.time.getOrNull(i) ?: continue
                val temperature = hourly.temperature_2m.getOrNull(i) ?: continue
                val relativeHumidity = hourly.relative_humidity_2m?.getOrNull(i) ?: continue
                val precipitationProbability = hourly.precipitation_probability?.getOrNull(i) ?: continue
                val precipitation = hourly.precipitation?.getOrNull(i) ?: continue
                val rain = hourly.rain?.getOrNull(i) ?: continue
                val cloudCover = hourly.cloud_cover?.getOrNull(i) ?: continue
                val visibility = hourly.visibility?.getOrNull(i) ?: continue
                val windSpeed = hourly.wind_speed_10m?.getOrNull(i) ?: continue

                val forecast = HourlyForecast(
                    time = time,
                    temperature = temperature,
                    relative_humidity_2m = relativeHumidity.toString(),
                    precipitation_probability = precipitationProbability.toString(),
                    precipitation = precipitation.toString(),
                    rain = rain.toString(),
                    cloud_cover = cloudCover.toString(),
                    visibility = visibility.toString(),
                    wind_speed_10m = windSpeed.toString()
                )
                forecasts.add(forecast)
            }
        }
        return forecasts
    }

}