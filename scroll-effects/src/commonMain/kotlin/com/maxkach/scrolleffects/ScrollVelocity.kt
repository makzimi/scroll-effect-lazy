package com.maxkach.scrolleffects

import androidx.compose.runtime.Immutable

@Immutable
class ScrollVelocity internal constructor(
    val rawPxPerSec: Float,
    val stretch: Float,
    // Signed cumulative "lag" in pixels, on the list's main axis. Accumulates
    // opposite to scroll direction during active scroll and spring-relaxes to
    // zero after the list settles. Positive when the list has scrolled forward,
    // negative after a backward scroll.
    val lagPx: Float,
    val direction: Int,
    val isDragging: Boolean,
    val isFlinging: Boolean,
)
