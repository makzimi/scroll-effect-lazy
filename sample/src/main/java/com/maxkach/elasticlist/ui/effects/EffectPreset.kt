package com.maxkach.elasticlist.ui.effects

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.maxkach.scrolleffects.ScrollEffectScope
import com.maxkach.scrolleffects.ScrollingItemData
import com.maxkach.scrolleffects.presets.ElasticStrength

enum class EffectPreset(val label: String) {
    None("None"),
    Hard("Hard"),
    Normal("Normal"),
    Loose("Loose"),
}

fun EffectPreset.asScrollEffect():
        (ScrollEffectScope.(ScrollingItemData) -> Unit)? = when (this) {
    EffectPreset.None   -> null
    EffectPreset.Hard   -> { item -> elastic(item, ElasticStrength.Hard) }
    EffectPreset.Normal -> { item -> elastic(item, ElasticStrength.Normal) }
    EffectPreset.Loose   -> { item -> elastic(item, ElasticStrength.Loose) }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EffectPresetPicker(
    selected: EffectPreset,
    onSelected: (EffectPreset) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 8.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        EffectPreset.entries.forEach { preset ->
            FilterChip(
                selected = preset == selected,
                onClick = { onSelected(preset) },
                label = { Text(preset.label) },
            )
        }
    }
}
