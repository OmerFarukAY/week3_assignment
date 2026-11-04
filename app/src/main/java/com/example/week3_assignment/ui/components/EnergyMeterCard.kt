package com.example.week3_assignment.ui.components

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.Sensors
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.week3_assignment.ui.theme.LivePulseGreen
import com.example.week3_assignment.ui.theme.MeterDarkBg
import com.example.week3_assignment.ui.theme.MeterDarkGradientEnd
import com.example.week3_assignment.ui.theme.MeterEyebrowColor
import com.example.week3_assignment.ui.theme.MeterSubtitleColor

/**
 * Reaktif Enerji Sayacı Kartı (Compose UI - %100 Stateless)
 *
 * Kural 1 (Katman Ayrımı): Hiçbir matematiksel toplama/çıkarma veya durum tutma (remember) içermez.
 * Kural 2 (120 FPS Performans): Gereksiz sonsuz animasyon içermez; yalnızca enerji değeri değiştiğinde
 * recompose olur.
 */
@Composable
fun EnergyMeterCard(
    formattedTotalPower: String,
    activeCountSummary: String,
    totalCountSummary: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = MeterDarkBg
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            MeterDarkBg,
                            MeterDarkGradientEnd
                        )
                    )
                )
                .padding(horizontal = 18.dp, vertical = 14.dp)
        ) {
            Column {
                // Üst Başlık ve Canlı Telemetri Rozeti
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF065F46).copy(alpha = 0.55f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Bolt,
                                contentDescription = null,
                                tint = LivePulseGreen,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "CANLI ENERJİ TÜKETİMİ",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    letterSpacing = 1.0.sp,
                                    fontWeight = FontWeight.Bold
                                ),
                                color = MeterEyebrowColor
                            )
                            Text(
                                text = "Yalnızca aktif ve çevrimiçi cihazlar",
                                style = MaterialTheme.typography.bodySmall,
                                color = MeterSubtitleColor
                            )
                        }
                    }

                    // Canlı Durum Rozeti
                    Surface(
                        shape = RoundedCornerShape(50),
                        color = Color(0xFF064E3B).copy(alpha = 0.7f)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(7.dp)
                                    .clip(CircleShape)
                                    .background(LivePulseGreen)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "CANLI",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 0.8.sp
                                ),
                                color = LivePulseGreen
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Ana Watt Değeri
                Text(
                    text = formattedTotalPower,
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontSize = 36.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = (-1.0).sp
                    ),
                    color = Color.White,
                    maxLines = 1
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Alt İstatistik Rozetleri (Eşit genişlikte, tek satır, Apple HIG hiyerarşisinde)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp),
                        color = Color(0xFF334155).copy(alpha = 0.65f)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Sensors,
                                contentDescription = null,
                                tint = LivePulseGreen,
                                modifier = Modifier.size(15.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = activeCountSummary,
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.SemiBold
                                ),
                                color = Color(0xFFF1F5F9),
                                maxLines = 1
                            )
                        }
                    }

                    Surface(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp),
                        color = Color(0xFF334155).copy(alpha = 0.45f)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Devices,
                                contentDescription = null,
                                tint = MeterEyebrowColor,
                                modifier = Modifier.size(15.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = totalCountSummary,
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Medium
                                ),
                                color = MeterEyebrowColor,
                                maxLines = 1
                            )
                        }
                    }
                }
            }
        }
    }
}
