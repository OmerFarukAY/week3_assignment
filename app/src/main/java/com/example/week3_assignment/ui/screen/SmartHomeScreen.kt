package com.example.week3_assignment.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.week3_assignment.ui.components.DeviceItemCard
import com.example.week3_assignment.ui.components.EnergyMeterCard
import com.example.week3_assignment.ui.components.RoomFilterChips
import com.example.week3_assignment.ui.components.SearchBarComponent
import com.example.week3_assignment.ui.state.SmartHomeUiState

/**
 * Akıllı Ev Ana Ekranı (Compose UI - 100% Stateless)
 *
 * MİMARİ KURAL 1 (Katman Ayrımı):
 * Bu ekranda ve alt bileşenlerinde hiçbir .filter, .map veya matematiksel toplama/çıkarma
 * işlemi YER ALMAZ. Tüm durum SmartHomeUiState üzerinden ViewModel tarafından sağlanır.
 *
 * MİMARİ KURAL 2 (Performans & 120 FPS):
 * LazyColumn 'key = { it.id }' parametresi ile re-composition maliyetini minimize eder,
 * arayüzün saniyede 120 FPS akıcılıkla çalışmasını sağlar.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SmartHomeScreen(
    uiState: SmartHomeUiState,
    onSearchQueryChange: (String) -> Unit,
    onRoomSelect: (String) -> Unit,
    onToggleDevice: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.size(8.dp))
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "Akıllı Ev Kontrol Merkezi",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            Text(
                                text = "BIL 403 • IoT Kontrol Paneli",
                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 1. Canlı Enerji Sayacı Kartı
            item(key = "energy_meter_card") {
                EnergyMeterCard(
                    formattedTotalPower = uiState.formattedTotalPower,
                    activeOnlineCount = uiState.activeOnlineCount,
                    totalDeviceCount = uiState.totalDeviceCount
                )
            }

            // 2. Arama Barı
            item(key = "search_bar") {
                SearchBarComponent(
                    searchQuery = uiState.searchQuery,
                    onSearchQueryChange = onSearchQueryChange
                )
            }

            // 3. Oda Filtreleme Sekmeleri
            item(key = "room_filter_chips") {
                RoomFilterChips(
                    rooms = uiState.roomList,
                    selectedRoom = uiState.selectedRoom,
                    onRoomSelect = onRoomSelect
                )
            }

            // 4. Cihazlar Başlığı ve Adet Bilgisi
            item(key = "devices_header") {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Cihazlar",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "${uiState.devices.size} sonuç",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // 5. Cihaz Listesi veya Boş Durum (Empty State)
            if (uiState.isEmptyResult) {
                item(key = "empty_state") {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 32.dp),
                        shape = RoundedCornerShape(16.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.SearchOff,
                                contentDescription = null,
                                modifier = Modifier.size(48.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "Eşleşen Cihaz Bulunamadı",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.SemiBold
                                ),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Arama kriterlerinize veya seçili odaya uygun cihaz yok.",
                                style = MaterialTheme.typography.bodySmall,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            } else {
                items(
                    items = uiState.devices,
                    key = { it.id }
                ) { device ->
                    DeviceItemCard(
                        device = device,
                        onToggle = onToggleDevice
                    )
                }
            }

            // Alt Boşluk
            item(key = "bottom_spacer") {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

