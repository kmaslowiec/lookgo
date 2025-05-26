package com.kmaslowiec.lookgo.stopdisplay.repository

import com.kmaslowiec.lookgo.stopdisplay.model.StopDisplay

interface StopDisplayRepository {

    suspend fun getStopDisplay(stopNumber: String): StopDisplay
}