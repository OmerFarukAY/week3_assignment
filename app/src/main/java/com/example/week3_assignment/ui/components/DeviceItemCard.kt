package com.example.week3_assignment.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.week3_assignment.data.model.DeviceCategory
import com.example.week3_assignment.ui.state.DeviceUiModel
import com.example.week3_assignment.ui.theme.ActiveEmeraldText
import com.example.week3_assignment.ui.theme.CameraCategoryBg
import com.example.week3_assignment.ui.theme.CameraCategoryColor
import com.example.week3_assignment.ui.theme.IosSwitchGreen
import com.example.week3_assignment.ui.theme.LightCategoryBg
import com.example.week3_assignment.ui.theme.LightCategoryColor
import com.example.week3_assignment.ui.theme.OfflineBadgeBg
import com.example.week3_assignment.ui.theme.OfflineGray
import com.example.week3_assignment.ui.theme.ThermostatCategoryBg
import com.example.week3_assignment.ui.theme.ThermostatCategoryColor
import com.example.week3_assignment.ui.theme.VacuumCategoryBg
import com.example.week3_assignment.ui.theme.VacuumCategoryColor

/**
 * Tekil Cihaz Kartı (Compose UI - %100 Stateless)
 *
 * Kural 1 uyarınca hiçbir filtreleme veya matematiksel işlem yapmaz;
 * ViewModel tarafından hazırlanan DeviceUiModel verisini çizer ve
 * Aç/Kapat etkileşimini onToggle(device.id) ile yukarı iletir.
 */
@Composable
fun DeviceItemCard(
    device: DeviceUiModel,
    onToggle: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val categoryColor = when (device.category) {
        DeviceCategory.LIGHT -> LightCategoryColor
        DeviceCategory.CAMERA -> CameraCategoryColor
        DeviceCategory.THERMOSTAT -> ThermostatCategoryColor
        DeviceCategory.VACUUM -> VacuumCategoryColor
    }

    val categoryBgColor = when (device.category) {
        DeviceCategory.LIGHT -> LightCategoryBg
        DeviceCategory.CAMERA -> CameraCategoryBg
        DeviceCategory.THERMOSTAT -> ThermostatCategoryBg
        DeviceCategory.VACUUM -> VacuumCategoryBg
    }

    val categoryIcon: ImageVector = when (device.category) {
        DeviceCategory.LIGHT -> Icons.Default.Lightbulb
        DeviceCategory.CAMERA -> Icons.Default.Videocam
        DeviceCategory.THERMOSTAT -> Icons.Default.Thermostat
        DeviceCategory.VACUUM -> Icons.Default.CleaningServices
    }

    val cardBorderColor = when {
        !device.isOnline -> MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)
        device.isActive -> IosSwitchGreen.copy(alpha = 0.45f)
        else -> MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.55f)
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .alpha(if (device.isOnline) 1.0f else 0.72f),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (device.isActive && device.isOnline) 2.dp else 0.dp
        ),
        border = BorderStroke(1.dp, cardBorderColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Sol: Kategori İkonu ve Cihaz Detayları
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(if (device.isOnline) categoryBgColor else OfflineBadgeBg),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (device.isOnline) categoryIcon else Icons.Default.WifiOff,
                        contentDescription = device.categoryLabel,
                        tint = if (device.isOnline) categoryColor else OfflineGray,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column {
                    Text(
                        text = device.name,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Oda Etiketi
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)
                        ) {
                            Text(
                                text = device.room,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.SemiBold
                                ),
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        // Güç Tüketimi (Watt)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Bolt,
                                contentDescription = null,
                                tint = if (device.isActive && device.isOnline) ActiveEmeraldText else OfflineGray,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = device.formattedWatt,
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = if (device.isActive && device.isOnline) {
                                    ActiveEmeraldText
                                } else {
                                    MaterialTheme.colorScheme.onSurfaceVariant
                                }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(3.dp))

                    // Durum Metni (WCAG AA 4.5:1+ kontrastlı)
                    Text(
                        text = device.statusText,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Medium
                        ),
                        color = when {
                            !device.isOnline -> OfflineGray
                            device.isActive -> ActiveEmeraldText
                            else -> MaterialTheme.colorScheme.onSurfaceVariant
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.width(10.dp))

            // Sağ: Aç / Kapat Anahtarı (Switch)
            Switch(
                checked = device.isActive,
                onCheckedChange = { onToggle(device.id) },
                enabled = device.canToggle,
                modifier = Modifier.semantics {
                    contentDescription = "${device.name} Aç Kapat"
                },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = IosSwitchGreen,
                    uncheckedThumbColor = Color.White,
                    uncheckedTrackColor = MaterialTheme.colorScheme.outlineVariant,
                    uncheckedBorderColor = Color.Transparent,
                    disabledCheckedTrackColor = OfflineGray.copy(alpha = 0.3f),
                    disabledUncheckedTrackColor = OfflineGray.copy(alpha = 0.2f),
                    disabledUncheckedThumbColor = OfflineGray.copy(alpha = 0.5f)
                )
            )
        }
    }
}
