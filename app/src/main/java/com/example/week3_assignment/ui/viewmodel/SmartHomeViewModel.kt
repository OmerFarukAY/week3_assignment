package com.example.week3_assignment.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.week3_assignment.data.model.Device
import com.example.week3_assignment.data.repository.DeviceRepository
import com.example.week3_assignment.data.repository.DeviceRepositoryImpl
import com.example.week3_assignment.ui.state.DeviceUiModel
import com.example.week3_assignment.ui.state.SmartHomeUiState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Locale

class SmartHomeViewModel(
    private val repository: DeviceRepository = DeviceRepositoryImpl(),
    private val computationDispatcher: CoroutineDispatcher = Dispatchers.Default
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    private val _selectedRoom = MutableStateFlow("Tümü")

    /**
     * Tüm hesaplama, filtreleme ve durum dönüşümleri computationDispatcher (Dispatchers.Default)
     * arka plan thread havuzunda yürütülür.
     * UI katmanı (Compose) tamamen durumsuz kalır; arayüzde hiçbir .filter veya matematiksel hesaplama yapılmaz.
     */
    val uiState: StateFlow<SmartHomeUiState> = combine(
        repository.getDevices(),
        _searchQuery,
        _selectedRoom
    ) { allDevices, query, room ->
        calculateUiState(allDevices, query, room)
    }.flowOn(computationDispatcher) // 120 FPS akıcılık için hesaplamalar arka plan thread'inde yürütülür
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = SmartHomeUiState()
        )

    private fun calculateUiState(
        allDevices: List<Device>,
        query: String,
        selectedRoom: String
    ): SmartHomeUiState {
        // İster 2: Sadece o an aktif VE online olan cihazların anlık elektrik tüketimlerini topla
        val activeOnlineDevices = allDevices.filter { it.isOnline && it.isActive }
        val totalWatt = activeOnlineDevices.sumOf { it.consumptionWatt }
        val activeOnlineCount = activeOnlineDevices.size
        val totalCount = allDevices.size

        // Dinamik oda listesi ("Tümü" en başta)
        val distinctRooms = listOf("Tümü") + allDevices.map { it.room }.distinct().sorted()

        // İster 1: Arama ve Oda Filtrelemesi
        val trimmedQuery = query.trim()
        val filteredList = allDevices.filter { device ->
            val matchesRoom = (selectedRoom == "Tümü" || device.room.equals(selectedRoom, ignoreCase = true))
            val matchesSearch = trimmedQuery.isEmpty() ||
                    device.name.contains(trimmedQuery, ignoreCase = true) ||
                    device.room.contains(trimmedQuery, ignoreCase = true)
            matchesRoom && matchesSearch
        }

        // UI için hazır, hesaplaması tamamlanmış model listesi (Compose içinde işlem yapılmaması için)
        val uiDeviceList = filteredList.map { device ->
            val statusText = when {
                !device.isOnline -> "Çevrimdışı (Bağlantı Yok)"
                device.isActive -> "Açık • Aktif Tüketim"
                else -> "Kapalı • Beklemede"
            }
            DeviceUiModel(
                id = device.id,
                name = device.name,
                category = device.category,
                room = device.room,
                isOnline = device.isOnline,
                isActive = device.isActive,
                consumptionWatt = device.consumptionWatt,
                formattedWatt = String.format(Locale.US, "%.1f W", device.consumptionWatt),
                statusText = statusText,
                canToggle = device.isOnline
            )
        }

        return SmartHomeUiState(
            totalPowerConsumptionWatt = totalWatt,
            formattedTotalPower = String.format(Locale.US, "%.1f W", totalWatt),
            activeOnlineCount = activeOnlineCount,
            totalDeviceCount = totalCount,
            searchQuery = query,
            selectedRoom = selectedRoom,
            roomList = distinctRooms,
            devices = uiDeviceList,
            isEmptyResult = uiDeviceList.isEmpty()
        )
    }

    fun onSearchQueryChanged(newQuery: String) {
        _searchQuery.value = newQuery
    }

    fun onRoomSelected(room: String) {
        _selectedRoom.value = room
    }

    // İster 3: Butona basıldığında cihaz durumunu değiştir ve StateFlow ile reaktif güncelle
    fun onToggleDevice(deviceId: Int) {
        viewModelScope.launch(computationDispatcher) {
            repository.toggleDevice(deviceId)
        }
    }
}
