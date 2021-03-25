package com.mj.weatherapp.details

import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import coil.load
import com.mj.weatherapp.*
import com.mj.weatherapp.databinding.FragmentForecastDetailsBinding
import java.text.SimpleDateFormat
import java.util.*




class ForecastDetailsFragment : Fragment() {
    private val args: ForecastDetailsFragmentArgs by navArgs()

    private lateinit var viewModelFactory: ForecastDetailsViewModelFactory

    private lateinit var tempDisplaySettingManager: TempDisplaySettingManager

    private var _binding: FragmentForecastDetailsBinding? =null
    //this property only valid between onCreateView and on DestroyView
    private val binding get() = _binding!!

    //viewModel
    private val viewModel : ForecastDetailsViewModel by viewModels(
        factoryProducer = { viewModelFactory}
    )



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentForecastDetailsBinding.inflate(inflater, container, false)
        viewModelFactory = ForecastDetailsViewModelFactory(args)
        tempDisplaySettingManager = TempDisplaySettingManager(requireContext())

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewStateObserver= Observer<ForecastDetailsViewState>{viewState->
            //update ui
            binding.tempText.text= formatTempForDisplay(viewState.temp, tempDisplaySettingManager.getTempDisplaySetting())
            binding.descriptionText.text = viewState.description
            binding.dateText.text = viewState.date
            binding.forecastIcon.load(viewState.iconUrl)

        }
        viewModel.viewState.observe(viewLifecycleOwner, viewStateObserver)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }




}