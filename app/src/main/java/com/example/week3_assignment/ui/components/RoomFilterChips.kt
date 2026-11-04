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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.week3_assignment.ui.state.RoomChipUiModel

/**
 * Oda Filtreleme Sekmeleri (Compose UI - %100 Stateless)
 *
 * Kural 1 uyarınca hiçbir karşılaştırma veya filtreleme mantığı içermez.
 * ViewModel tarafından hazırlanan RoomChipUiModel listesini doğrudan çizer.
 */
@Composable
fun RoomFilterChips(
    roomChips: List<RoomChipUiModel>,
    onRoomSelect: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 2.dp)
    ) {
        items(
            items = roomChips,
            key = { it.name }
        ) { chip ->
            FilterChip(
                selected = chip.isSelected,
                onClick = { onRoomSelect(chip.name) },
                label = {
                    Text(
                        text = chip.name,
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = if (chip.isSelected) FontWeight.Bold else FontWeight.Medium
                        )
                    )
                },
                shape = RoundedCornerShape(12.dp),
                border = FilterChipDefaults.filterChipBorder(
                    enabled = true,
                    selected = chip.isSelected,
                    borderColor = MaterialTheme.colorScheme.outlineVariant,
                    selectedBorderColor = MaterialTheme.colorScheme.primary
                ),
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                    selectedLabelColor = MaterialTheme.colorScheme.surface,
                    containerColor = MaterialTheme.colorScheme.surface,
                    labelColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
    }
}
