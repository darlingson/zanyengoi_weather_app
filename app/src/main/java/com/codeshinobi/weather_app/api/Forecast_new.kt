package com.codeshinobi.weather_app.api

data class Forecast_new (
    val latitude: Double? = null,
    val longitude: Double? = null,
    val generationtimeMS: Double? = null,
    val utcOffsetSeconds: Long? = null,
    val timezone: String? = null,
    val timezoneAbbreviation: String? = null,
    val elevation: Long? = null,
    val hourlyUnits: HourlyUnits? = null,
    val hourly: Hourly? = null
)

data class Hourly_new (
    val time: List<String>? = null,
    val temperature_2m: List<Double>? = null,
    val relative_humidity_2m: List<Long>? = null,
    val precipitation_probability: List<Long>? = null,
    val precipitation: List<Double>? = null,
    val rain: List<Double>? = null,
    val cloud_cover: List<Long>? = null,
    val visibility: List<Long>? = null,
    val wind_speed_10m: List<Double>? = null
)

data class HourlyUnits_new (
    val time: String? = null,
    val temperature_2m: String? = null,
    val relative_humidity_2m: String? = null,
    val precipitation_probability: String? = null,
    val precipitation: String? = null,
    val rain: String? = null,
    val cloud_cover: String? = null,
    val visibility: String? = null,
    val wind_speed_10m: String? = null
)