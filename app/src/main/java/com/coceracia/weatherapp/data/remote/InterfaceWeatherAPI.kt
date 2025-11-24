package com.coceracia.weatherapp.data.remote

import com.coceracia.weatherapp.data.model.Forecast5Response
import com.coceracia.weatherapp.data.model.WeatherData
import retrofit2.http.GET
import retrofit2.http.Query

interface InterfaceWeatherAPI {
    @GET("data/2.5/weather")
    suspend fun getWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric",
        @Query("lang") lang: String = "en"
    ): WeatherData

    @GET("data/2.5/forecast")
    suspend fun getForecast5(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric"
    ): Forecast5Response
}