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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.mj.weatherapp.*
import com.mj.weatherapp.api.DailyForecast
import com.mj.weatherapp.api.WeeklyForecast


class WeeklyForecastFragment : Fragment() {
    private lateinit var tempDisplaySettingManager: TempDisplaySettingManager
    private lateinit var locationRepository: LocationRepository
    private val forecastRepository = ForecastRepository()




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        tempDisplaySettingManager = TempDisplaySettingManager(requireContext())
        val city = arguments?.getString(KEY_CITY) ?:"ooppss...."
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_weekly_forecast, container, false)
        val emptyText: TextView = view.findViewById(R.id.emptyText)
        val progressBar: ProgressBar = view.findViewById(R.id.progressBar)
        val locationEntryBtn: FloatingActionButton = view.findViewById(R.id.locationEntryBtn)
        locationEntryBtn.setOnClickListener(){
           showLocationEntry()
        }


        val forecastList: RecyclerView = view.findViewById(R.id.forecastlist)
        forecastList.layoutManager= LinearLayoutManager(requireContext())
        val dailyForecastAdapter = DailyForecastAdapter(tempDisplaySettingManager){forecast ->
            showForecastDetails(forecast)
        }
        forecastList.adapter = dailyForecastAdapter



        val weeklyForecastObserver = Observer<WeeklyForecast>{ weeklyForcast ->
            progressBar.visibility = View.GONE
            emptyText.visibility = View.GONE
            //update our  list adapter
            dailyForecastAdapter.submitList(weeklyForcast.daily)
        }
        forecastRepository.weeklyForecast.observe(viewLifecycleOwner, weeklyForecastObserver)

        locationRepository = LocationRepository(requireContext())
        val savedLocationObserver = Observer<Location>{savedLocation ->
            when(savedLocation){
                is Location.City ->{
                    progressBar.visibility = View.VISIBLE
                    forecastRepository.loadWeeklyForecast(savedLocation.city)}
            }
        }
        locationRepository.savedLocation.observe(viewLifecycleOwner, savedLocationObserver)

        return view
    }
    private fun  showLocationEntry(){
        val action = WeeklyForecastFragmentDirections.actionWeeklyForecastFragmentToLocationEntryFragment()
        findNavController().navigate(action)

    }
    private fun showForecastDetails(forecast: DailyForecast){
        val temp = forecast.temp.max
        val description = forecast.weather[0].description
        val date = forecast.date
        val icon = forecast.weather[0].icon
        val action = WeeklyForecastFragmentDirections.actionWeeklyForecastFragmentToForecastDetailsFragment(temp, description, date, icon)
        findNavController().navigate(action)
    }

    companion object{
        const val KEY_CITY= "key_city"

        fun newInstance(city:String):WeeklyForecastFragment{
            val fragment = WeeklyForecastFragment()
             val args = Bundle()
            args.putString(KEY_CITY, city)
            fragment.arguments = args
            return fragment
        }
    }


}