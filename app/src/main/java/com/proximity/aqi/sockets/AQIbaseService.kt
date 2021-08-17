package com.proximity.aqi.sockets

import com.proximity.aqi.sockets.models.Results
import com.tinder.scarlet.WebSocket
import com.tinder.scarlet.ws.Receive
import kotlinx.coroutines.flow.Flow

interface AQIbaseService {

    @Receive
    fun observeWebSocket(): Flow<WebSocket.Event>

    @Receive
    fun observeTicker(): Flow<List<Results>>
}
