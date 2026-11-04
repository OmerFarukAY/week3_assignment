package com.example.week3_assignment

import com.example.week3_assignment.data.repository.DeviceRepositoryImpl
import com.example.week3_assignment.ui.viewmodel.SmartHomeViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SmartHomeViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var repository: DeviceRepositoryImpl
    private lateinit var viewModel: SmartHomeViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repository = DeviceRepositoryImpl()
        viewModel = SmartHomeViewModel(
            repository = repository,
            computationDispatcher = testDispatcher
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun TestScope.startCollectingUiState() = launch(testDispatcher) {
        viewModel.uiState.collect()
    }

    @Test
    fun initialState_calculatesCorrectTotalWattForOnlyActiveAndOnlineDevices() = runTest(testDispatcher) {
        val collectJob = startCollectingUiState()
        advanceUntilIdle()

        val state = viewModel.uiState.value

        // 10 cihazdan o an aktif VE online olanlar:
        // Köşe Lambası (15.0) + Bahçe Kamerası (8.5) + Giriş Kamerası (8.5) +
        // Merkezi Termostat (12.0) + Roborock S7 (50.0) + Çalışma Odası Lambası (12.0) = 106.0 Watt
        assertEquals(106.0, state.totalPowerConsumptionWatt, 0.01)
        assertEquals("106.0 W", state.formattedTotalPower)
        assertEquals(6, state.activeOnlineCount)
        assertEquals(10, state.totalDeviceCount)
        assertEquals("6 Aktif Cihaz", state.activeCountSummary)
        assertEquals("10 Toplam Cihaz", state.totalCountSummary)
        assertEquals("10 sonuç", state.filteredCountSummary)

        // İlk 3 oda sekmesi: Tümü, Salon, Mutfak
        assertEquals("Tümü", state.roomList[0])
        assertEquals("Salon", state.roomList[1])
        assertEquals("Mutfak", state.roomList[2])

        collectJob.cancel()
    }

    @Test
    fun toggleDevice_updatesDeviceStateAndEnergyReactively() = runTest(testDispatcher) {
        val collectJob = startCollectingUiState()
        advanceUntilIdle()

        // Cihaz 6: "Yatak Odası Kliması" (1200.0 Watt, Online: Evet, Aktif: Hayır)
        viewModel.onToggleDevice(6)
        advanceUntilIdle()

        val stateAfterTurningOn = viewModel.uiState.value
        // Yeni toplam: 106.0 + 1200.0 = 1306.0 Watt
        assertEquals(1306.0, stateAfterTurningOn.totalPowerConsumptionWatt, 0.01)
        assertEquals(7, stateAfterTurningOn.activeOnlineCount)

        val klima = stateAfterTurningOn.devices.first { it.id == 6 }
        assertTrue(klima.isActive)

        // Klimayı tekrar kapatalım
        viewModel.onToggleDevice(6)
        advanceUntilIdle()

        val stateAfterTurningOff = viewModel.uiState.value
        assertEquals(106.0, stateAfterTurningOff.totalPowerConsumptionWatt, 0.01)
        assertEquals(6, stateAfterTurningOff.activeOnlineCount)

        collectJob.cancel()
    }

    @Test
    fun toggleOfflineDevice_doesNotChangeStateNorEnergy() = runTest(testDispatcher) {
        val collectJob = startCollectingUiState()
        advanceUntilIdle()

        // Cihaz 9: "Balkon Aydınlatması" (Online: Hayır, Aktif: Hayır)
        viewModel.onToggleDevice(9)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        // Enerji değişmemeli (106.0 W kalmalı)
        assertEquals(106.0, state.totalPowerConsumptionWatt, 0.01)
        val balkon = state.devices.first { it.id == 9 }
        assertFalse(balkon.isActive)
        assertFalse(balkon.isOnline)
        assertFalse(balkon.canToggle)

        collectJob.cancel()
    }

    @Test
    fun roomFilter_filtersCorrectDevices() = runTest(testDispatcher) {
        val collectJob = startCollectingUiState()
        advanceUntilIdle()

        viewModel.onRoomSelected("Salon")
        advanceUntilIdle()

        val state = viewModel.uiState.value
        // Salonda 3 cihaz var: Köşe Lambası, Tavan Avizesi, Merkezi Termostat
        assertEquals(3, state.devices.size)
        assertEquals("3 sonuç", state.filteredCountSummary)
        assertTrue(state.devices.all { it.room == "Salon" })
        assertTrue(state.roomChips.first { it.name == "Salon" }.isSelected)

        // "Tümü" seçildiğinde 10 cihaz da geri gelmeli
        viewModel.onRoomSelected("Tümü")
        advanceUntilIdle()

        val allState = viewModel.uiState.value
        assertEquals(10, allState.devices.size)

        collectJob.cancel()
    }

    @Test
    fun searchQuery_filtersCorrectlyIncludingTurkishCharacters() = runTest(testDispatcher) {
        val collectJob = startCollectingUiState()
        advanceUntilIdle()

        viewModel.onSearchQueryChanged("Kamera")
        // Arama metni senkron olarak hemen güncellenmeli (TextField karakter kaybını önlemek için)
        assertEquals("Kamera", viewModel.uiState.value.searchQuery)
        assertTrue(viewModel.uiState.value.isSearchNotEmpty)

        advanceUntilIdle()

        val state = viewModel.uiState.value
        // Bahçe Kamerası ve Giriş Kamerası olmak üzere 2 cihaz
        assertEquals(2, state.devices.size)
        assertTrue(state.devices.all { it.name.contains("Kamera", ignoreCase = true) })

        // Türkçe büyük/küçük harf testi ("GİRİŞ" -> "Giriş Kamerası")
        viewModel.onSearchQueryChanged("GİRİŞ")
        advanceUntilIdle()
        assertEquals(1, viewModel.uiState.value.devices.size)
        assertEquals("Giriş Kamerası", viewModel.uiState.value.devices.first().name)

        collectJob.cancel()
    }
}
