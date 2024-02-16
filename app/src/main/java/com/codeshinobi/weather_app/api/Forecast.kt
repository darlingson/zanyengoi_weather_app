package com.codeshinobi.weather_app.api

data class Forecast (
    val latitude: Long,
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
    val temperature2M: List<Double>
)

data class HourlyUnits (
    val time: String,
    val temperature2M: String
)