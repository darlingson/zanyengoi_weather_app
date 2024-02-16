package com.codeshinobi.weather_app.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codeshinobi.weather_app.api.ApiClient
import com.codeshinobi.weather_app.api.Forecast
import kotlinx.coroutines.launch

class ForecastViewModel : ViewModel() {
    val forecast = MutableLiveData<Forecast>()

    fun getForecast() {
        viewModelScope.launch {
            val response = ApiClient.apiService.getForecast(52.52, 13.41, "temperature_2m")
            if (response.isSuccessful) {
                forecast.value = response.body()
            } else {
                // Handle the error here
            }
        }
    }
}
