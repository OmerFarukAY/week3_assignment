package com.example.week3_assignment.ui.state

import androidx.compose.runtime.Immutable
import com.example.week3_assignment.data.model.DeviceCategory

@Immutable
data class RoomChipUiModel(
    val name: String,
    val isSelected: Boolean
)

@Immutable
data class DeviceUiModel(
    val id: Int,
    val name: String,
    val category: DeviceCategory,
    val categoryLabel: String,
    val room: String,
    val isOnline: Boolean,
    val isActive: Boolean,
    val consumptionWatt: Double,
    val formattedWatt: String,
    val statusText: String,
    val canToggle: Boolean
)

@Immutable
data class SmartHomeUiState(
    val totalPowerConsumptionWatt: Double = 0.0,
    val formattedTotalPower: String = "0.0 W",
    val activeOnlineCount: Int = 0,
    val totalDeviceCount: Int = 0,
    val activeCountSummary: String = "0 Aktif Cihaz",
    val totalCountSummary: String = "0 Toplam Cihaz",
    val filteredCountSummary: String = "0 sonuç",
    val searchQuery: String = "",
    val isSearchNotEmpty: Boolean = false,
    val selectedRoom: String = "Tümü",
    val roomList: List<String> = listOf("Tümü"),
    val roomChips: List<RoomChipUiModel> = listOf(RoomChipUiModel("Tümü", true)),
    val devices: List<DeviceUiModel> = emptyList(),
    val isEmptyResult: Boolean = false
)
