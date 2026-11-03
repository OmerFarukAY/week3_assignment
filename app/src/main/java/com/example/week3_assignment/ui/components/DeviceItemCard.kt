package com.example.week3_assignment.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.week3_assignment.data.model.DeviceCategory
import com.example.week3_assignment.ui.state.DeviceUiModel
import com.example.week3_assignment.ui.theme.CameraCategoryColor
import com.example.week3_assignment.ui.theme.ElectricGreen
import com.example.week3_assignment.ui.theme.LightCategoryColor
import com.example.week3_assignment.ui.theme.OfflineGray
import com.example.week3_assignment.ui.theme.ThermostatCategoryColor
import com.example.week3_assignment.ui.theme.VacuumCategoryColor

/**
 * Tekil Cihaz Kartı (Compose UI - 100% Stateless)
 *
 * Kural 1 uyarınca hesaplama yapmaz; hazır DeviceUiModel verisini görüntüler.
 * Aç/Kapat butonuna basıldığında doğrudan onToggle(device.id) callback'i tetiklenir.
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

    val categoryIcon: ImageVector = when (device.category) {
        DeviceCategory.LIGHT -> Icons.Default.Lightbulb
        DeviceCategory.CAMERA -> Icons.Default.Videocam
        DeviceCategory.THERMOSTAT -> Icons.Default.Thermostat
        DeviceCategory.VACUUM -> Icons.Default.CleaningServices
    }

    val cardBorderColor by animateColorAsState(
        targetValue = when {
            !device.isOnline -> Color.Transparent
            device.isActive -> categoryColor.copy(alpha = 0.5f)
            else -> MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)
        },
        animationSpec = tween(durationMillis = 200),
        label = "cardBorderColor"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .alpha(if (device.isOnline) 1.0f else 0.7f),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (device.isActive && device.isOnline) 4.dp else 1.dp
        ),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = androidx.compose.ui.graphics.SolidColor(cardBorderColor)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Sol: Kategori İkonu ve Cihaz Detayları
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Kategori İkonu
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(
                            if (device.isOnline) categoryColor.copy(alpha = 0.15f)
                            else OfflineGray.copy(alpha = 0.15f)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (device.isOnline) categoryIcon else Icons.Default.WifiOff,
                        contentDescription = device.category.displayName,
                        tint = if (device.isOnline) categoryColor else OfflineGray,
                        modifier = Modifier.size(26.dp)
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                // Cihaz Bilgileri
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = device.name,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.SemiBold
                            ),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    // Oda ve Durum Etiketi
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)
                        ) {
                            Text(
                                text = device.room,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Medium
                                ),
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        // Güç Tüketimi
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Bolt,
                                contentDescription = null,
                                tint = if (device.isActive && device.isOnline) ElectricGreen else OfflineGray,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = device.formattedWatt,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontWeight = FontWeight.Medium
                                ),
                                color = if (device.isActive && device.isOnline) ElectricGreen else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = device.statusText,
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                        color = when {
                            !device.isOnline -> OfflineGray
                            device.isActive -> ElectricGreen
                            else -> MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Sağ: Aç / Kapat Butonu (Switch)
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Switch(
                    checked = device.isActive,
                    onCheckedChange = { onToggle(device.id) },
                    enabled = device.canToggle,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = ElectricGreen,
                        uncheckedThumbColor = MaterialTheme.colorScheme.outline,
                        uncheckedTrackColor = MaterialTheme.colorScheme.surfaceVariant,
                        disabledCheckedTrackColor = OfflineGray.copy(alpha = 0.3f),
                        disabledUncheckedTrackColor = OfflineGray.copy(alpha = 0.15f)
                    )
                )
            }
        }
    }
}
