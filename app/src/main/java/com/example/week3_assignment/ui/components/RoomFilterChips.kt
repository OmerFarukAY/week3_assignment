package com.example.week3_assignment.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Oda Filtreleme Sekmeleri (Compose UI - 100% Stateless)
 *
 * Filtreleme mantığı içermez. Sadece hazır oda listesini çizer ve tıklanan odayı
 * ViewModel'e bildirir.
 */
@Composable
fun RoomFilterChips(
    rooms: List<String>,
    selectedRoom: String,
    onRoomSelect: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 4.dp)
    ) {
        items(
            items = rooms,
            key = { it }
        ) { room ->
            val isSelected = room.equals(selectedRoom, ignoreCase = true)
            FilterChip(
                selected = isSelected,
                onClick = { onRoomSelect(room) },
                label = {
                    Text(
                        text = room,
                        style = MaterialTheme.typography.labelLarge
                    )
                },
                shape = RoundedCornerShape(12.dp),
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                    selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                    labelColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
    }
}

