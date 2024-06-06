package com.example.weather_app_android_java

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import weatherapimodel

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        fetchweather()
    }

    private fun fetchweather() {
        val retrofit = Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.openweathermap.org/data/2.5/").build()
            .create(api_interface::class.java)

        val response = retrofit.get_weather("London", "1af5ffff4e3e1675f012193ef48083de", "metric")

        response.enqueue(object : Callback<weatherapimodel> {
            override fun onResponse(
                call: Call<weatherapimodel>,
                response: Response<weatherapimodel>
            ) {
                val responsebody = response.body();
                if (response.isSuccessful) {
                    val temperature = responsebody!!.main.temp.toString()
                    val maxtemp = responsebody!!.main.temp_max.toString()
                    val mintemp = responsebody!!.main.temp_min.toString()
                    val humidity = responsebody!!.main.humidity.toString()
                    val condition = responsebody!!.weather.get(0).main.toString()
                    val pressure = responsebody!!.main.pressure.toString()
                    val windspeed = responsebody!!.wind.speed.toString()
                    val winddirection = responsebody!!.wind.deg.toString()
                    val description = responsebody!!.weather.get(0).description.toString()
                    val icon = responsebody!!.weather.get(0).icon.toString()
                    val main = responsebody!!.weather.get(0).main.toString()
                    val name = responsebody!!.name.toString()
                    val country = responsebody!!.sys.country.toString()
                    val sunrise = responsebody!!.sys.sunrise.toString()
                    val sunset = responsebody!!.sys.sunset.toString()
                    val timezone = responsebody!!.timezone.toString()
                    val visibility = responsebody!!.visibility.toString()
                    val id = responsebody!!.id.toString()
                    val cod = responsebody!!.cod.toString()

                    Log.d("AHMED", "onResponse: $temperature")
                    Log.d("AHMED", "_humidity: $humidity")

                    val temp = findViewById<TextView>(R.id.temperatureTextView)
                    val humidityy = findViewById<TextView>(R.id.humidityTextView)
                    val conditonss = findViewById<TextView>(R.id.weatherConditionTextView)
                    val pressurey = findViewById<TextView>(R.id.seaLevelTextView)
                    val maxtemprature = findViewById<TextView>(R.id.maxTempTextView)
                    val mintemprature = findViewById<TextView>(R.id.minTempTextView)
                    val description_ = findViewById<TextView>(R.id.bigweatherConditionTextView)
                    val cityname = findViewById<TextView>(R.id.locationTextView)
//                    val windspeedy = findViewById<TextView>(R.id.windspeedTextView)
//                    val winddirectiony = findViewById<TextView>(R.id.winddirectionTextView)

                    temp.text = "$temperature°C"
                    humidityy.text = "$humidity %"
                    conditonss.text = "$condition"
                    pressurey.text = "$pressure hPa"
                    maxtemprature.text = "Max $maxtemp°C"
                    mintemprature.text = "Min $mintemp°C"
                    description_.text = "$description"
                    cityname.text = "$name"
//                    windspeedy.text = " $windspeed"
//                    winddirectiony.text = "$winddirection"
                }
            }

            override fun onFailure(call: Call<weatherapimodel>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }
}