package com.mj.weatherapp.details

import android.accounts.AuthenticatorDescription

data class ForecastDetailsViewState(
    val temp: Float,
    val description: String,
    val date: String,
    val iconUrl: String
)