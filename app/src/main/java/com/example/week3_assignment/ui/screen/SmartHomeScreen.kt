package com.example.week3_assignment.ui.screen

import androidx.compose.foundation.layout.Arrangement
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
 * Akıllı Ev Ana Ekranı (Compose UI - %100 Stateless)
 *
 * MİMARİ KURAL 1 (Katman Ayrımı):
 * Bu ekranda ve alt bileşenlerinde hiçbir .filter, .map veya matematiksel toplama/çıkarma
 * işlemi YER ALMAZ. Tüm durum SmartHomeUiState üzerinden ViewModel tarafından sağlanır.
 *
 * MİMARİ KURAL 2 (Performans & 120 FPS):
 * Üst kontrol paneli (Canlı Enerji Sayacı, Arama Çubuğu ve Oda Filtreleri) ekranın en üstünde
 * sabitlenmiş olup, altındaki LazyColumn 'key = { it.id }' ile 120 FPS akıcılıkla kayar.
 * Böylece listedeki herhangi bir cihaz açılıp kapatıldığında en üstteki enerji sayacının
 * reaktif değişimi her zaman anında görülür.
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
        containerColor = MaterialTheme.colorScheme.background,
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
                            modifier = Modifier.size(22.dp)
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
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            // 1. Ekranın En Üstünde Sabit Reaktif Enerji Sayacı Kartı (İster 2 & 3)
            EnergyMeterCard(
                formattedTotalPower = uiState.formattedTotalPower,
                activeCountSummary = uiState.activeCountSummary,
                totalCountSummary = uiState.totalCountSummary
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 2. Arama Çubuğu (İster 1)
            SearchBarComponent(
                searchQuery = uiState.searchQuery,
                isSearchNotEmpty = uiState.isSearchNotEmpty,
                onSearchQueryChange = onSearchQueryChange
            )

            Spacer(modifier = Modifier.height(10.dp))

            // 3. Oda Filtreleme Sekmeleri (İster 1: Tümü, Salon, Mutfak...)
            RoomFilterChips(
                roomChips = uiState.roomChips,
                onRoomSelect = onRoomSelect
            )

            Spacer(modifier = Modifier.height(10.dp))

            // 4. Cihazlar Başlığı ve Sonuç Özeti
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Cihazlar",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = uiState.filteredCountSummary,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 5. Cihaz Listesi (120 FPS Kaydırılabilir LazyColumn)
            if (uiState.isEmptyResult) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp),
                    shape = RoundedCornerShape(18.dp),
                    color = MaterialTheme.colorScheme.surface
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
                            modifier = Modifier.size(44.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(10.dp))
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
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentPadding = PaddingValues(bottom = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
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
            }
        }
    }
}
