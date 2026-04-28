package com.maxkach.scrolleffects

import androidx.compose.runtime.Immutable

@Immutable
data class ScrollingItemData(
    val index: Int,
    val velocity: ScrollVelocity,
    val distanceFromDragged: Int,
)
