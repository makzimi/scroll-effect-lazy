package com.maxkach.scrolleffects.presets

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.ui.graphics.GraphicsLayerScope
import com.maxkach.scrolleffects.ScrollingItemData
import kotlin.math.abs
import kotlin.math.min

/**
 * Offsets one item along the list's main axis so it lags behind the finger,
 * producing the elastic stretch
 */
fun GraphicsLayerScope.elasticEffect(
    item: ScrollingItemData,
    orientation: Orientation,
    mainAxisItemSpacingPx: Int,
    strength: ElasticStrength = ElasticStrength.Normal,
    maxTranslationDp: Float = 150f,
) {
    val distance = item.distanceFromDragged
    if (distance == 0) return

    val lag = item.velocity.lagPx

    // Only stretch the side being newly revealed. Same sign = leading side,
    // opposite sign = trailing side (skip). Zero lag = no scroll (skip).
    if (distance.toFloat() * lag <= 0f) return

    val absDistance = abs(distance)
    val coefficient = if (absDistance <= 2) {
        strength.closeCoefficient
    } else {
        strength.farCoefficient
    }

    // Cap the lag, smaller wins:
    // - strengthCap: how stretchy this rubber feels.
    // - overlapSafeCap: geometric limit — any higher and close/far neighbors would collide at
    // the coefficient drop.
    val spacing = mainAxisItemSpacingPx.toFloat().coerceAtLeast(1f)
    val strengthCap = strength.capSpacingMultiplier * spacing
    val overlapSafeCap = spacing / strength.maxCoefStep.coerceAtLeast(0.0001f)
    val effectiveCap = min(strengthCap, overlapSafeCap)
    val clampedLag = lag.coerceIn(-effectiveCap, effectiveCap)

    val maxPx = maxTranslationDp * density
    val magnitudePx = (clampedLag * coefficient).coerceIn(-maxPx, maxPx)
    if (orientation == Orientation.Vertical) {
        translationY += magnitudePx
    } else {
        translationX += magnitudePx
    }
}
