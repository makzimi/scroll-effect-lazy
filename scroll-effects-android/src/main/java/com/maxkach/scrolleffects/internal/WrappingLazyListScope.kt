package com.maxkach.scrolleffects.internal

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import com.maxkach.scrolleffects.ScrollEffectScope
import com.maxkach.scrolleffects.ScrollEffectScopeImpl
import com.maxkach.scrolleffects.ScrollingItemData

internal class WrappingLazyListScope(
    private val delegate: LazyListScope,
    private val controller: ScrollEffectController,
    private val effect: ScrollEffectScope.(ScrollingItemData) -> Unit,
) : LazyListScope {

    private var cursor = 0

    override fun item(
        key: Any?,
        contentType: Any?,
        content: @Composable LazyItemScope.() -> Unit,
    ) {
        val index = cursor++
        val ctrl = controller
        val fx = effect
        delegate.item(key, contentType) {
            WrapItem(ctrl, fx, index) { content() }
        }
    }

    override fun items(
        count: Int,
        key: ((Int) -> Any)?,
        contentType: (Int) -> Any?,
        itemContent: @Composable LazyItemScope.(Int) -> Unit,
    ) {
        val base = cursor
        cursor += count
        val ctrl = controller
        val fx = effect
        delegate.items(count, key, contentType) { i ->
            WrapItem(ctrl, fx, base + i) { itemContent(i) }
        }
    }

    @ExperimentalFoundationApi
    override fun stickyHeader(
        key: Any?,
        contentType: Any?,
        content: @Composable LazyItemScope.() -> Unit,
    ) {
        cursor++
        // Sticky headers are not wrapped — they should stay visually pinned.
        delegate.stickyHeader(key, contentType, content)
    }
}

@Composable
private fun WrapItem(
    controller: ScrollEffectController,
    effect: ScrollEffectScope.(ScrollingItemData) -> Unit,
    index: Int,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = Modifier.graphicsLayer {
            val scope = ScrollEffectScopeImpl(
                delegate = this,
                layoutInfo = controller.listState.layoutInfo,
                orientation = controller.orientation,
            )
            val velocity = controller.currentVelocity()
            val item = ScrollingItemData(
                index = index,
                velocity = velocity,
                distanceFromDragged = index - controller.draggedIndex(),
            )
            with(scope) { effect(item) }
        },
    ) {
        content()
    }
}
