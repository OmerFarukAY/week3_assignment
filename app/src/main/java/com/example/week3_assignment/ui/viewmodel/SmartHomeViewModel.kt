package com.example.week3_assignment.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.week3_assignment.data.model.Device
import com.example.week3_assignment.data.repository.DeviceRepository
import com.example.week3_assignment.data.repository.DeviceRepositoryImpl
import com.example.week3_assignment.ui.state.DeviceUiModel
import com.example.week3_assignment.ui.state.RoomChipUiModel
import com.example.week3_assignment.ui.state.SmartHomeUiState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

class SmartHomeViewModel(
    private val repository: DeviceRepository = DeviceRepositoryImpl(),
    private val computationDispatcher: CoroutineDispatcher = Dispatchers.Default
) : ViewModel() {

    private val turkishLocale = Locale.forLanguageTag("tr-TR")

    private val _searchQuery = MutableStateFlow("")
    private val _selectedRoom = MutableStateFlow("Tümü")

    /**
     * Kurumsal MVVM Backing Property:
     * Dış katmana (Compose UI) yalnızca salt-okunur StateFlow açılır.
     * Tüm hesaplama, filtreleme (.filter) ve toplama (.sumOf) işlemleri
     * computationDispatcher (Dispatchers.Default) arka plan thread havuzunda yürütülür.
     */
    private val _uiState = MutableStateFlow(SmartHomeUiState())
    val uiState: StateFlow<SmartHomeUiState> = _uiState.asStateFlow()

    init {
        observeSmartHomeState()
    }

    private fun observeSmartHomeState() {
        viewModelScope.launch {
            combine(
                repository.getDevices(),
                _searchQuery,
                _selectedRoom
            ) { allDevices, query, room ->
                Triple(allDevices, query, room)
            }.collectLatest { (allDevices, query, room) ->
                val computedState = withContext(computationDispatcher) {
                    calculateUiState(allDevices, query, room)
                }
                _uiState.value = computedState
            }
        }
    }

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

        // Odalar: "Tümü" başta, ardından hocanın örneklediği "Salon" ve "Mutfak" önde olacak şekilde tüm odalar
        val priorityRooms = listOf("Salon", "Mutfak")
        val allDistinctRooms = allDevices.map { it.room }.distinct()
        val orderedRooms = listOf("Tümü") +
                priorityRooms.filter { it in allDistinctRooms } +
                allDistinctRooms.filterNot { it in priorityRooms }

        val roomChips = orderedRooms.map { roomName ->
            RoomChipUiModel(
                name = roomName,
                isSelected = roomName.equals(selectedRoom, ignoreCase = true)
            )
        }

        // İster 1: Arama ve Oda Filtrelemesi (Türkçe karakter duyarlı)
        val normalizedQuery = query.trim().lowercase(turkishLocale)
        val filteredList = allDevices.filter { device ->
            val matchesRoom = selectedRoom == "Tümü" ||
                    device.room.equals(selectedRoom, ignoreCase = true)
            val matchesSearch = normalizedQuery.isEmpty() ||
                    device.name.lowercase(turkishLocale).contains(normalizedQuery) ||
                    device.room.lowercase(turkishLocale).contains(normalizedQuery)
            matchesRoom && matchesSearch
        }

        // UI için tamamen hazır model listesi (Compose katmanında hiçbir işlem yapılmaması için)
        val uiDeviceList = filteredList.map { device ->
            val statusText = when {
                !device.isOnline -> "Çevrimdışı • Bağlantı Yok"
                device.isActive -> "Açık • Aktif Tüketim"
                else -> "Kapalı • Beklemede"
            }
            DeviceUiModel(
                id = device.id,
                name = device.name,
                category = device.category,
                categoryLabel = device.category.displayName,
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
            activeCountSummary = "$activeOnlineCount Aktif Cihaz",
            totalCountSummary = "$totalCount Toplam Cihaz",
            filteredCountSummary = "${uiDeviceList.size} sonuç",
            searchQuery = query,
            isSearchNotEmpty = query.isNotEmpty(),
            selectedRoom = selectedRoom,
            roomList = orderedRooms,
            roomChips = roomChips,
            devices = uiDeviceList,
            isEmptyResult = uiDeviceList.isEmpty()
        )
    }

    fun onSearchQueryChanged(newQuery: String) {
        _searchQuery.value = newQuery
        // OutlinedTextField imleç ve karakter kaybını önlemek için metin durumu anında senkronize edilir,
        // filtreleme ise arka planda (computationDispatcher) asenkron yürütülür.
        _uiState.update { current ->
            current.copy(
                searchQuery = newQuery,
                isSearchNotEmpty = newQuery.isNotEmpty()
            )
        }
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
