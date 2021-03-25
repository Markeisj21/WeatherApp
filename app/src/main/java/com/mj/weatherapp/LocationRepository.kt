package com.mj.weatherapp

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

sealed class Location{
    data class City(val city:String ) : Location()
}
 private const val KEY_LOCATION= "key_location"

class LocationRepository(context: Context) {
    private val preferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE)

    private val _savedLocation: MutableLiveData<Location> = MutableLiveData()
    val savedLocation: LiveData<Location> = _savedLocation

    init {
        preferences.registerOnSharedPreferenceChangeListener{
            sharedPreferences, key ->
            if(key != KEY_LOCATION) return@registerOnSharedPreferenceChangeListener
            broadcastSavedCity()
        }

        broadcastSavedCity()
    }

    fun saveLocation(location: Location){
        when(location){
            is Location.City -> preferences.edit().putString(KEY_LOCATION, location.city).apply()
        }
    }

    private fun broadcastSavedCity() {
        val city = preferences.getString(KEY_LOCATION, "")
        if (city != null && city.isNotBlank()) {
            _savedLocation.value = Location.City(city)
        }
    }


}