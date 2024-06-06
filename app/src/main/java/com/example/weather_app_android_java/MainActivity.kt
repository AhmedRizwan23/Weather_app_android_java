package com.example.weather_app_android_java

import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView
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
        fetchweather("Lahore"); // Default city
        val searchview = findViewById<SearchView>(R.id.searchView);
        searchview.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                fetchweather(query.toString())
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

    }

    private fun fetchweather(cityName: String) {
        val retrofit = Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.openweathermap.org/data/2.5/").build()
            .create(api_interface::class.java)

        val response = retrofit.get_weather(cityName, "1af5ffff4e3e1675f012193ef48083de", "metric")

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

                    setanimation(description)
                } else {
                    // Handle the case where the response is not successful or the body is null
                    handleErrorResponse()
                }
            }

            override fun onFailure(call: Call<weatherapimodel>, t: Throwable) {

                TODO("Not yet implemented")
            }

        })
    }

    private fun setanimation(description: String) {
        val animationView = findViewById<LottieAnimationView>(R.id.Animation)
        val background =
            findViewById<androidx.constraintlayout.widget.ConstraintLayout>(R.id.main)

        when (description.toUpperCase()) {

            "CLEAR", "CLEAR SKY", "CLEAR SCATTERED", "CLEAR BROKEN", "SUNNY" -> {

                animationView.setAnimation(R.raw.sun)
                background.setBackgroundResource(R.drawable.sunny_background)
            }

            "HAZE", "PARTLY CLOUDY", "CLOUDS", "OVERCAST", "FEW CLOUDS", "LIGHT CLOUDS", "OVERCAST CLOUDS", "BROKEN CLOUDS" -> {
                animationView.setAnimation(R.raw.cloud)
                background.setBackgroundResource(R.drawable.colud_background)
            }

            "RAIN", "DRIZZLE", "SHOWERS", "lIGHTRAIN", "THUNDERSTORM" -> {
                animationView.setAnimation(R.raw.rain)


                background.setBackgroundResource(R.drawable.rain_background)

            }


            "Snow", "Heavy Snow", "BLIZZARD", "HAIL" -> {
                animationView.setAnimation(R.raw.snow)
                background.setBackgroundResource(R.drawable.snow_background)

            }

            else -> {
                animationView.setAnimation(R.raw.snow) // A default animation in case description does not match any case
            }
        }

        animationView.playAnimation()
    }

    private fun handleErrorResponse() {
        Log.e("AHMED", "Error fetching weather data")
        // Display an error message to the user
        //show toast
        Toast.makeText(
            this,
            "City not found. Please enter a valid city name.",
            Toast.LENGTH_LONG
        ).show()
    }
}

