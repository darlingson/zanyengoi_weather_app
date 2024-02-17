package com.codeshinobi.weather_app.api

data class Forecast (
    val latitude: Double,
    val longitude: Double,
    val generationtimeMS: Double,
    val utcOffsetSeconds: Long,
    val timezone: String,
    val timezoneAbbreviation: String,
    val elevation: Long,
    val hourlyUnits: HourlyUnits,
    val hourly: Hourly
)

data class Hourly (
    val time: List<String>,
    val temperature_2m: List<Double>,
    val relative_humidity_2m: List<Long>? = null,
    val precipitation_probability: List<Long>? = null,
    val precipitation: List<Double>? = null,
    val rain: List<Double>? = null,
    val cloud_cover: List<Long>? = null,
    val visibility: List<Long>? = null,
    val wind_speed_10m: List<Double>? = null
)

data class HourlyUnits (
    val time: String,
    val temperature_2m: String
)

data class HourlyForecast(
    val time: String,
    val temperature: Double,
    val relative_humidity_2m: String? = null,
    val precipitation_probability: String? = null,
    val precipitation: String? = null,
    val rain: String? = null,
    val cloud_cover: String? = null,
    val visibility: String? = null,
    val wind_speed_10m: String? = null
)