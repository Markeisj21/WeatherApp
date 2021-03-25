package com.mj.weatherapp.forecast

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.mj.weatherapp.*
import com.mj.weatherapp.api.CurrentWeather


class CurrentForecastFragment : Fragment() {
    private lateinit var locationRepository: LocationRepository
    private lateinit var tempDisplaySettingManager: TempDisplaySettingManager
    private val forecastRepository = ForecastRepository()





    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        tempDisplaySettingManager = TempDisplaySettingManager(requireContext())
        val city = arguments?.getString(KEY_ZIP) ?:"oopps...."
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_current_forecast, container, false)

        val locationName: TextView = view.findViewById(R.id.locationName)
        val  tempText : TextView = view.findViewById(R.id.tempText)
        val emptyText:TextView = view.findViewById(R.id.emptyText)
        val progressBar: ProgressBar = view.findViewById(R.id.progressBar)

        val locationEntryBtn: FloatingActionButton = view.findViewById(R.id.locationEntryBtn)
        locationEntryBtn.setOnClickListener(){
            showLocationEntry()
        }

        //create observer which updates the ui response to forecast updates
        val currentWeatherObserver = Observer<CurrentWeather> {weather ->
           locationName.text = weather.name
            progressBar.visibility = View.GONE
            emptyText.visibility = View.GONE
            tempText.text = formatTempForDisplay(weather.forecast.temp,  tempDisplaySettingManager.getTempDisplaySetting())
            locationName.visibility = View.VISIBLE
            tempText.visibility = View.VISIBLE

        }
        forecastRepository.currentWeather.observe(viewLifecycleOwner, currentWeatherObserver)




        locationRepository = LocationRepository(requireContext())
        val savedLocationObserver = Observer<Location> { savedLocation->
            when (savedLocation){
                is Location.City -> {
                    progressBar.visibility = View.VISIBLE
                    forecastRepository.loadCurrentForecast(savedLocation.city)}
            }

        }
        locationRepository.savedLocation.observe(viewLifecycleOwner,savedLocationObserver)




        return view
    }

    private fun  showLocationEntry(){
        val action = CurrentForecastFragmentDirections.actionCurrentForecastFragmentToLocationEntryFragment()
        findNavController().navigate(action)
    }


    companion object{
        const val KEY_ZIP= "key_city"

        fun newInstance(zip:String):CurrentForecastFragment{
            val fragment = CurrentForecastFragment()
             val args = Bundle()
            args.putString(KEY_ZIP, zip)
            fragment.arguments = args
            return fragment
        }
    }


}