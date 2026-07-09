package com.maxkach.scrolleffects

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.lazy.LazyListLayoutInfo
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.GraphicsLayerScope
import com.maxkach.scrolleffects.presets.ElasticStrength
import com.maxkach.scrolleffects.presets.elasticEffect

@Immutable
interface ScrollEffectScope : GraphicsLayerScope {
    val orientation: Orientation
    val layoutInfo: LazyListLayoutInfo

    fun elastic(
        item: ScrollingItemData,
        strength: ElasticStrength = ElasticStrength.Normal,
        maxTranslationDp: Float = 150f,
    )
}

internal class ScrollEffectScopeImpl(
    private val delegate: GraphicsLayerScope,
    override val layoutInfo: LazyListLayoutInfo,
    override val orientation: Orientation,
) : ScrollEffectScope, GraphicsLayerScope by delegate {

    override fun elastic(
        item: ScrollingItemData,
        strength: ElasticStrength,
        maxTranslationDp: Float,
    ) {
        elasticEffect(
            item = item,
            orientation = orientation,
            mainAxisItemSpacingPx = layoutInfo.mainAxisItemSpacing,
            strength = strength,
            maxTranslationDp = maxTranslationDp,
        )
    }
}
