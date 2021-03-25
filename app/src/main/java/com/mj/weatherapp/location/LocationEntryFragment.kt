package com.mj.weatherapp.location

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.mj.weatherapp.Location
import com.mj.weatherapp.LocationRepository
import com.mj.weatherapp.R




class LocationEntryFragment : Fragment() {

    private lateinit var locationRepository: LocationRepository



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        //instance of location repository
        locationRepository = LocationRepository(requireContext())
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_location_entry, container, false)

        val cityEditText: EditText = view.findViewById(R.id.zipcodeEditText)
        val lookupBtn: Button = view.findViewById(R.id.lookup_btn)

        lookupBtn.setOnClickListener {
            val city: String = cityEditText.text.toString()
            if (city.length < 2) {
                Toast.makeText(requireContext(), R.string.city_entry_error, Toast.LENGTH_LONG)
                    .show()
            } else {
                //save location of repository
                    locationRepository.saveLocation(Location.City(city))
                //forecastRepository.loadForecast(city) }
                findNavController().navigateUp()
            }
        }

        return view
    }

}