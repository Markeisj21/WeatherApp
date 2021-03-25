package com.mj.weatherapp

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mj.weatherapp.api.CurrentWeather
import com.mj.weatherapp.api.DailyForecast
import com.mj.weatherapp.api.WeeklyForecast
import com.mj.weatherapp.api.createOpenWeatherMapService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.random.Random

class ForecastRepository {

    private val _currentWeather = MutableLiveData<CurrentWeather>()
    val currentWeather: LiveData<CurrentWeather> = _currentWeather


    private val _weeklyForecast = MutableLiveData<WeeklyForecast>()
    val weeklyForecast : LiveData<WeeklyForecast> = _weeklyForecast

    fun loadWeeklyForecast(zipcode: String){
    val call = createOpenWeatherMapService().currentWeather(zipcode,"imperial",BuildConfig.OPEN_WEATHER_MAP_API_KEY)
        call.enqueue(object: Callback<CurrentWeather>{
            override fun onResponse(
                call: Call<CurrentWeather>,
                response: Response<CurrentWeather>, )
            {
                val weatherResponse = response.body()
                if(weatherResponse != null){
                    //load 7 days
                   val forecastCall= createOpenWeatherMapService().sevenDayForecast(
                       lat = weatherResponse.coord.lat,
                       lon = weatherResponse.coord.lon,
                       exclude = "current,minutely, hourly",
                       units = "imperial",
                       apiKey = BuildConfig.OPEN_WEATHER_MAP_API_KEY)
                    forecastCall.enqueue(object: Callback<WeeklyForecast>{
                        override fun onResponse(
                            call: Call<WeeklyForecast>,
                            response: Response<WeeklyForecast>, ) {
                            val weeklyForecastResponse = response.body()
                            if(weeklyForecastResponse != null){
                                _weeklyForecast.value = weeklyForecastResponse
                            }

                        }

                        override fun onFailure(call: Call<WeeklyForecast>, t: Throwable) {
                            Log.e(ForecastRepository::class.java.simpleName,"error loading weekly forecast", t)
                        }

                    } )


                }
            }

            override fun onFailure(call: Call<CurrentWeather>, t: Throwable) {
                Log.e(ForecastRepository::class.java.simpleName, "error loading location for weekly forecast" , t)

            }

        })

    }

    fun loadCurrentForecast(city: String){
//        val randomTemp = Random.nextFloat().rem(100)* 100
//        val forecast = DailyForecast(Date(), randomTemp, getTempDesciption(randomTemp))
//        _currentForecast.value = forecast
        val call = createOpenWeatherMapService().currentWeather(city,"imperial",  BuildConfig.OPEN_WEATHER_MAP_API_KEY)
        call.enqueue(object: Callback<CurrentWeather>{
            override fun onResponse(
                call: Call<CurrentWeather>,
                response: Response<CurrentWeather>, )
            {
                val weatherResponse = response.body()
                if(weatherResponse != null){
                    _currentWeather.value = weatherResponse
                }
            }

            override fun onFailure(call: Call<CurrentWeather>, t: Throwable) {
                    Log.e(ForecastRepository::class.java.simpleName, "error loading current weather" , t)

            }

        })

    }
    private fun getTempDesciption(temp: Float): String{
        return when (temp) {
            in Float.MIN_VALUE.rangeTo(0f) -> "Freezing "
            in 0f.rangeTo(32f)-> "Too cold"
            in 32f.rangeTo(55f)-> "Bring a Coast"
            in 55f.rangeTo(65f)-> "Bring a Jacket"
            in 65f.rangeTo(80f)-> "PERFECT!!!"
            in 80f.rangeTo(90f)-> "Hot"
            in 90f.rangeTo(Float.MAX_VALUE)-> "OMG HOT!"
            else -> "Does not compute"

        }

    }
}