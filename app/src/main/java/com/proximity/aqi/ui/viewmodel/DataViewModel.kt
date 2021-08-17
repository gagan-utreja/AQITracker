package com.proximity.aqi.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.proximity.aqi.AQITrackerApplication
import com.proximity.aqi.repository.CityRepository
import com.proximity.aqi.sockets.AQIbaseService
import com.proximity.aqi.sockets.models.Results
import com.tinder.scarlet.WebSocket
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import timber.log.Timber
import java.util.concurrent.Executors
import javax.inject.Inject

@HiltViewModel
class DataViewModel @Inject constructor(
    service: AQIbaseService, application: AQITrackerApplication
) : ViewModel() {

    private val cityRepository = CityRepository.getInstance(
        application.applicationContext,
        Executors.newSingleThreadExecutor()
    )
    val cityNameData = cityRepository.getCitName()

    init {
        service.observeWebSocket()
            .flowOn(Dispatchers.IO)
            .onEach { event ->
                if (event !is WebSocket.Event.OnMessageReceived) {
                    Timber.d("Event = ${event::class.java.simpleName}")
                }

            }
            .launchIn(viewModelScope)

        service.observeTicker()
            .flowOn(Dispatchers.IO)
            .map {
                cityRepository.insertCityNameList(it as ArrayList<Results>)
                cityRepository.insertList(it)
                delay(30000)
            }
            .launchIn(viewModelScope)

    }


}
