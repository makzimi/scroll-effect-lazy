package com.maxkach.scrolleffects.sample.ui.effects

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.maxkach.scrolleffects.ScrollEffectScope
import com.maxkach.scrolleffects.ScrollingItemData
import com.maxkach.scrolleffects.presets.ElasticStrength

/** Two modes only: [Off] (plain list) and [On] (elastic, using [ElasticStrength.Loose]). */
enum class EffectPreset {
    Off,
    On,
}

fun EffectPreset.asScrollEffect():
        (ScrollEffectScope.(ScrollingItemData) -> Unit)? = when (this) {
    EffectPreset.Off -> null
    EffectPreset.On  -> { item -> elastic(item, ElasticStrength.Loose) }
}

@Composable
fun EffectPresetPicker(
    selected: EffectPreset,
    onSelected: (EffectPreset) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text("Elastic scroll")
        Switch(
            checked = selected == EffectPreset.On,
            onCheckedChange = { checked ->
                onSelected(if (checked) EffectPreset.On else EffectPreset.Off)
            },
        )
    }
}
