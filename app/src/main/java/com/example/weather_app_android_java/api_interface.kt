package com.example.weather_app_android_java

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import weatherapimodel

interface api_interface {

    @GET("weather")
    fun getWeatherByCity(
        @Query("q") cityName: String,
        @Query("appid") apiKey: String,
        @Query("units") units: String
    ): Call<weatherapimodel>

    @GET("weather")
    fun getWeatherByCoordinates(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("appid") apiKey: String,
        @Query("units") units: String
    ): Call<weatherapimodel>
}