package com.example.week3_assignment.data.model

data class Device(
    val id: Int,
    val name: String,
    val category: DeviceCategory,
    val room: String,
    val isOnline: Boolean,
    val isActive: Boolean,
    val consumptionWatt: Double
)

