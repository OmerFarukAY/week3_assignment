package com.example.week3_assignment.data.repository

import com.example.week3_assignment.data.model.Device
import com.example.week3_assignment.data.model.DeviceCategory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class DeviceRepositoryImpl : DeviceRepository {

    // Hocanın verdiği 10 adet ham veri seti
    private val initialDevices = listOf(
        Device(
            id = 1,
            name = "Köşe Lambası",
            category = DeviceCategory.LIGHT,
            room = "Salon",
            isOnline = true,
            isActive = true,
            consumptionWatt = 15.0
        ),
        Device(
            id = 2,
            name = "Tavan Avizesi",
            category = DeviceCategory.LIGHT,
            room = "Salon",
            isOnline = true,
            isActive = false,
            consumptionWatt = 60.0
        ),
        Device(
            id = 3,
            name = "Bahçe Kamerası",
            category = DeviceCategory.CAMERA,
            room = "Bahçe",
            isOnline = true,
            isActive = true,
            consumptionWatt = 8.5
        ),
        Device(
            id = 4,
            name = "Giriş Kamerası",
            category = DeviceCategory.CAMERA,
            room = "Antre",
            isOnline = true,
            isActive = true,
            consumptionWatt = 8.5
        ),
        Device(
            id = 5,
            name = "Merkezi Termostat",
            category = DeviceCategory.THERMOSTAT,
            room = "Salon",
            isOnline = true,
            isActive = true,
            consumptionWatt = 12.0
        ),
        Device(
            id = 6,
            name = "Yatak Odası Kliması",
            category = DeviceCategory.THERMOSTAT,
            room = "Yatak Odası",
            isOnline = true,
            isActive = false,
            consumptionWatt = 1200.0
        ),
        Device(
            id = 7,
            name = "Mutfak Spotları",
            category = DeviceCategory.LIGHT,
            room = "Mutfak",
            isOnline = true,
            isActive = false,
            consumptionWatt = 45.0
        ),
        Device(
            id = 8,
            name = "Roborock S7",
            category = DeviceCategory.VACUUM,
            room = "Koridor",
            isOnline = true,
            isActive = true,
            consumptionWatt = 50.0
        ),
        Device(
            id = 9,
            name = "Balkon Aydınlatması",
            category = DeviceCategory.LIGHT,
            room = "Balkon",
            isOnline = false,
            isActive = false,
            consumptionWatt = 25.0
        ),
        Device(
            id = 10,
            name = "Çalışma Odası Lambası",
            category = DeviceCategory.LIGHT,
            room = "Ofis",
            isOnline = true,
            isActive = true,
            consumptionWatt = 12.0
        )
    )

    private val _devicesFlow = MutableStateFlow(initialDevices)

    override fun getDevices(): Flow<List<Device>> = _devicesFlow.asStateFlow()

    override suspend fun toggleDevice(deviceId: Int) {
        _devicesFlow.update { currentList ->
            currentList.map { device ->
                if (device.id == deviceId) {
                    // Sadece online olan cihazların durumu değiştirilebilir (veya hocanın isterine göre durum tersine döner)
                    // Eğer cihaz çevrimdışıysa durumu değişmez; çevrimiçi ise toggle edilir.
                    if (device.isOnline) {
                        device.copy(isActive = !device.isActive)
                    } else {
                        device
                    }
                } else {
                    device
                }
            }
        }
    }
}
