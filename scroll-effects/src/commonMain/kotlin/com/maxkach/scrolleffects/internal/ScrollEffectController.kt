package com.maxkach.scrolleffects.internal

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.pointer.util.VelocityTracker
import androidx.compose.ui.unit.Velocity
import com.maxkach.scrolleffects.ScrollVelocity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.math.abs
import kotlin.math.absoluteValue
import kotlin.math.max
import kotlin.math.sign
import kotlin.math.tanh
import kotlin.time.Clock

/**
 * Scroll-physics brain for one list. Watches finger velocity, turns it
 * into a spring-animated "lag" value, and exposes it via [currentVelocity]
 * so each item can read it every frame and offset itself.
 */
internal class ScrollEffectController(
    val listState: LazyListState,
    private val coroutineScope: CoroutineScope,
    val orientation: Orientation,
    private val velocityToStretch: (Float) -> Float = DefaultVelocityToStretch,
    private val relaxSpec: AnimationSpec<Float> = DefaultRelaxSpec,
) {
    private val tracker = VelocityTracker()
    private val stretchAnim = Animatable(0f)
    private val lagAnim = Animatable(0f)
    private var rawVelocity by mutableFloatStateOf(0f)
    private var isDragging by mutableStateOf(false)
    private var isFlinging by mutableStateOf(false)
    private var lastDraggedIndex by mutableIntStateOf(UNSET_INDEX)
    private var relaxJob: Job? = null

    // VelocityTracker wants running totals, not per-frame deltas.
    private var cumulativePositionPx = 0f

    fun currentVelocity(): ScrollVelocity = ScrollVelocity(
        rawPxPerSec = rawVelocity,
        stretch = stretchAnim.value,
        lagPx = lagAnim.value,
        direction = rawVelocity.sign.toInt(),
        isDragging = isDragging,
        isFlinging = isFlinging,
    )

    fun onTouchDown(mainAxisPx: Float) {
        val info = listState.layoutInfo
        val hit = info.visibleItemsInfo.firstOrNull {
            mainAxisPx >= it.offset && mainAxisPx < it.offset + it.size
        }
        if (hit != null) lastDraggedIndex = hit.index
    }

    fun draggedIndex(): Int {
        if (lastDraggedIndex != UNSET_INDEX) return lastDraggedIndex
        val info = listState.layoutInfo
        val viewportCenter = (info.viewportStartOffset + info.viewportEndOffset) / 2f
        return info.visibleItemsInfo.minByOrNull {
            abs((it.offset + it.size / 2f) - viewportCenter)
        }?.index ?: 0
    }

    val nestedScrollConnection: NestedScrollConnection = object : NestedScrollConnection {
        override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
            isDragging = source == NestedScrollSource.UserInput
            return Offset.Zero
        }

        override fun onPostScroll(
            consumed: Offset,
            available: Offset,
            source: NestedScrollSource,
        ): Offset {
            val delta = if (orientation == Orientation.Vertical) {
                consumed.y
            } else {
                consumed.x
            }

            cumulativePositionPx += delta
            val nowMs = Clock.System.now().toLocalDateTime(TimeZone.UTC).nanosecond / 1_000_000L
            val position = if (orientation == Orientation.Vertical) {
                Offset(0f, cumulativePositionPx)
            } else {
                Offset(cumulativePositionPx, 0f)
            }
            tracker.addPosition(
                timeMillis = nowMs,
                position = position
            )
            val velocity = tracker.calculateVelocity().let {
                if (orientation == Orientation.Vertical) it.y else it.x
            }
            rawVelocity = velocity
            val target = velocityToStretch(velocity.absoluteValue)
            if (target > stretchAnim.value) {
                relaxJob?.cancel()
                coroutineScope.launch { stretchAnim.snapTo(target) }
            }

            // Pick a target for the spring to chase:
            // - fast scroll -> target near cap -> big gap
            // - slower -> smaller target -> gap shrinks
            // - stopped -> target 0 -> gap decays to rest
            // Sign is flipped so items lag behind the finger.
            // Cap scales with item spacing → matches Elastic.kt's overlap limit.
            val spacing = listState.layoutInfo.mainAxisItemSpacing.toFloat()
            val cap = if (spacing > 0f) spacing * LAG_CAP_SPACING_MULTIPLIER
                      else MAX_LAG_PX_FALLBACK
            val normalized = velocityToStretch(velocity.absoluteValue)
            val targetLag = -sign(delta) * normalized * cap

            // animateTo re-targets the running spring instead of starting a
            // new one, so calling it every frame just nudges the target. The
            // top-level `animate()` burns two frames before its first tick —
            // which would kill lagAnim before it ever moves here.
            coroutineScope.launch {
                lagAnim.animateTo(targetLag, lagRelaxSpec)
            }

            return Offset.Zero
        }

        override suspend fun onPreFling(available: Velocity): Velocity {
            isFlinging = true
            val v = if (orientation == Orientation.Vertical) available.y else available.x
            rawVelocity = v
            val target = velocityToStretch(v.absoluteValue)
            relaxJob?.cancel()
            stretchAnim.snapTo(max(stretchAnim.value, target))
            return Velocity.Zero
        }

        override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity {
            isFlinging = false
            isDragging = false
            rawVelocity = 0f
            tracker.resetTracking()
            cumulativePositionPx = 0f
            relaxJob?.cancel()
            relaxJob = coroutineScope.launch { stretchAnim.animateTo(0f, relaxSpec) }
            // Relax lag back to rest.
            coroutineScope.launch {
                lagAnim.animateTo(0f, lagRelaxSpec)
            }
            return Velocity.Zero
        }
    }
}

private val DefaultRelaxSpec = spring<Float>(
    stiffness = Spring.StiffnessMediumLow,
    dampingRatio = Spring.DampingRatioMediumBouncy,
)

private val lagRelaxSpec = spring<Float>(
    stiffness = Spring.StiffnessMedium,
    dampingRatio = Spring.DampingRatioNoBouncy,
)

private val DefaultVelocityToStretch: (Float) -> Float = { v -> tanh(v / 1500f).absoluteValue }

private const val UNSET_INDEX = Int.MIN_VALUE
// Used when the list has no Arrangement.spacedBy.
private const val MAX_LAG_PX_FALLBACK = 1000f
// Max lag = spacing × this. Matches the overlap ceiling in Elastic.kt.
private const val LAG_CAP_SPACING_MULTIPLIER = 3f
