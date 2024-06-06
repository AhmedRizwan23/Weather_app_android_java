package com.example.weather_app_android_java

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import weatherapimodel

interface api_interface {

    @GET("weather")
    fun get_weather(
        @Query("q") city_name: String,
        @Query("appid") api_key: String,
        @Query("units") units: String,

        ): Call<weatherapimodel>
}