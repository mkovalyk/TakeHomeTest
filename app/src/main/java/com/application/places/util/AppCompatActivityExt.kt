package com.application.places.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import androidx.appcompat.app.AppCompatActivity
import com.application.places.ViewModelFactory

/**
 * Method to retrieve [ViewModel] from Activity using [ViewModelFactory]
 */
fun <T : ViewModel> AppCompatActivity.obtainViewModel(viewModelClass: Class<T>) =
        ViewModelProviders.of(this, ViewModelFactory.getInstance(application)).get(viewModelClass)
