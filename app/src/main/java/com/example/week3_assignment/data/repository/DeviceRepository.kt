package com.example.week3_assignment.data.repository

import com.example.week3_assignment.data.model.Device
import kotlinx.coroutines.flow.Flow

interface DeviceRepository {
    fun getDevices(): Flow<List<Device>>
    suspend fun toggleDevice(deviceId: Int)
}

