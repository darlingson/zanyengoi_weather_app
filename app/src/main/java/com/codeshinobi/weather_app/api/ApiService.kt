package com.codeshinobi.weather_app.api

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("posts/{id}")
    fun getPostById(@Path("id") postId: Int): Call<Forecast>

    @GET("v1/forecast")
    suspend fun getForecast(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("hourly") hourly: String,
        @Query("temperature_2m") temperature2m: Boolean = true,
        @Query("relative_humidity_2m") relativeHumidity2m: Boolean = true,
        @Query("precipitation_probability") precipitationProbability: Boolean = true,
        @Query("precipitation") precipitation: Boolean = true,
        @Query("rain") rain: Boolean = true,
        @Query("cloud_cover") cloudCover: Boolean = true,
        @Query("visibility") visibility: Boolean = true,
        @Query("wind_speed_10m") windSpeed10m: Boolean = true
    ): Response<Forecast>
}
